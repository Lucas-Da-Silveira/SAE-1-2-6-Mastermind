package model;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import javafx.geometry.Point2D;

import java.util.*;

public class MasterStageModel extends GameStageModel {
    // states
    public final static int STATE_SELECTPAWN = 1; // the player must select a pawn
    public final static int STATE_SELECTDEST = 2; // the player must select a destination
    public final static int PHASE_CODE = 1;
    public final static int PHASE_GAME = 2;
    public final static int GAME_STATE_WIN = 1;
    public final static int GAME_STATE_LOOSE = 2;
    public final static int GAME_STATE_CONTINUE = 3;

    private MasterBoard board;
    private MasterBoard checkBoard;
    private ColorsBoard colors;
    private MasterBoard colorPot;
    private MasterBoard codeBoard;
    private String secretCombination;
    private int rowsCompleted;
    private ArrayList<Pawn> pawns;
    private ArrayList<Pawn> checkPawns;
    private Map<Character, Pawn> colorPawns;
    private Map<Pawn.Color, List<Pawn>> colorPotLists;
    private int nbMatch;
    private int nbCommon;
    public List<Character> answer;
    public List<String> possibleAnswer;
    public int turn;
    private TextElement playerName;
    private int phase;
    private int gameState;

    /**
     * Constructs a new MasterStageModel with the given name and model.
     *
     * @param name  The name of the stage.
     * @param model The model associated with the stage.
     */
    public MasterStageModel(String name, Model model) {
        super(name, model);
        secretCombination = "";
        rowsCompleted = 0;
        pawns = new ArrayList<>();
        checkPawns = new ArrayList<>();
        colorPawns = new HashMap<> ();
        colorPotLists = new HashMap<Pawn.Color, List<Pawn>>();
        answer = new ArrayList<>();
        possibleAnswer = new ArrayList<>();
        turn = 0;
        state = STATE_SELECTPAWN;
        phase = PHASE_CODE;
        gameState = GAME_STATE_CONTINUE;
        setupCallbacks();
    }

    /**
     * Sets up the callbacks for the stage with the given controller.
     *
     */
    public void setupCallbacks() {
        onSelectionChange(() -> {
            if (selected.size() == 0) {
                board.resetReachableCells(false);
                return;
            }
            Pawn pawn = (Pawn) selected.get(0);
            board.setValidCells(getRowsCompleted());
        });

        onPutInGrid(this::putCallback);
    }

    /**
     * Callback triggered when an element is put in a grid
     * @param element the element put in the grid
     * @param gridDest the grid
     * @param rowDest the row position of the element
     * @param colDest the col position of the element
     */
    public void putCallback(GameElement element, GridElement gridDest, int rowDest, int colDest) {
        if (gridDest != board && gridDest != checkBoard && gridDest != codeBoard) return;
        if (this.phase == MasterStageModel.PHASE_CODE) {
            this.secretCombination = this.secretCombination + ((Pawn)element).getColor().name().charAt(0);
            if (secretCombination.length() == MasterSettings.NB_COLS) {
                this.board.setVisible(true);
                this.checkBoard.setVisible(true);
                this.codeBoard.setVisible(false);
                Arrays.stream(Pawn.Color.values()).forEach(c -> {
                    this.colorPotLists.get(c).stream().filter(GameElement::isVisible).forEach(p -> p.setVisible(false));
                });
            }
            return;
        }
        if (((Pawn)element).isInCheckBoard() && gridDest == checkBoard) {
            checkPawns.add((Pawn)element);
            return;
        }

        if (pawns.size() % board.getNbCols() == 0 && !pawns.isEmpty()) {

            int yPos = 0;
            int row = getRowsCompleted();
            StringBuilder secretCombinationTmp = new StringBuilder(getSecretCombination());
            StringBuilder pawnsTmp = new StringBuilder();

            for (Pawn pawn : pawns) {
                pawnsTmp.append(pawn.getColor().name().charAt(0));
            }
            pawnsTmp = new StringBuilder(pawnsTmp.substring(MasterSettings.NB_COLS * row, MasterSettings.NB_COLS * (row + 1)));
            nbMatch = numberCorrectPawns(secretCombinationTmp, pawnsTmp);
            nbCommon = numberCommonPawns(secretCombinationTmp, pawnsTmp);
            this.incrementRowsCompleted();

            if (nbMatch == board.getNbCols()) {
                // win
                this.gameState = GAME_STATE_WIN;
            } else if (rowsCompleted >= board.getNbRows() && nbMatch != board.getNbCols()) {
                // loose
                this.gameState = GAME_STATE_LOOSE;
            }
        }
    }

    /**
     * Computes the result of the game party based on the specified win status.
     *
     * @param win A boolean indicating whether the player won the game.
     */
    public void computePartyResult(boolean win) {
        model.setIdWinner(win ? 1 : 0);
        this.getPlayerName().setText(model.getPlayers().get(model.getIdWinner()).getName() + " wins");
        model.stopStage();
    }

    /**
     * Calculates the number of correct pawns in the specified code and answer.
     *
     * @param code   The code to compare against.
     * @param answer The answer to check.
     * @return The number of correct pawns.
     */
    public int numberCorrectPawns(StringBuilder code, StringBuilder answer) {
        int result = 0;
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == answer.charAt(i) && code.charAt(i) != 'X' && answer.charAt(i) != 'X') {
                code.replace(i, i+1, "X");
                answer.replace(i, i+1, "X");
                result++;
            }
        }
        return result;
    }

    /**
     * Calculates the number of common pawns between the specified code and answer.
     *
     * @param code   The code to compare against.
     * @param answer The answer to check.
     * @return The number of common pawns.
     */
    public int numberCommonPawns(StringBuilder code, StringBuilder answer) {
        int result = 0;
        for (int i = 0; i < code.length(); i++) {
            for (int j = 0; j < answer.length(); j++) {
                if(code.charAt(i) == answer.charAt(j) && code.charAt(i) != 'X' && answer.charAt(j) != 'X') {
                    result++;
                    answer.replace(j, j+1, "X");
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns the game board.
     *
     * @return The game board.
     */
    public MasterBoard getBoard() {
        return this.board;
    }

    /**
     * Sets the game board.
     *
     * @param _board The game board to set.
     */
    public void setBoard(MasterBoard _board) {
        this.board = _board;
        addGrid(this.board);
    }

    /**
     * Returns the check board.
     *
     * @return The check board.
     */
    public MasterBoard getCheckBoard() {
        return this.checkBoard;
    }

    /**
     * Sets the check board.
     *
     * @param _checkBoard The check board to set.
     */
    public void setCheckBoard(MasterBoard _checkBoard) {
        this.checkBoard = _checkBoard;
        addGrid(this.checkBoard);
    }

    public MasterBoard getCodeBoard() {
        return this.codeBoard;
    }

    public void setCodeBoard(MasterBoard codeBoard) {
        this.codeBoard = codeBoard;
        addGrid(this.codeBoard);
    }

    /**
     * Returns the colors board.
     *
     * @return The colors board.
     */
    public ColorsBoard getColorsBoard() { return this.colors; }

    /**
     * Sets the colors board.
     *
     * @param _colors The colors board to set.
     */
    public void setColorsBoard(ColorsBoard _colors) {
        this.colors = _colors;
        addGrid(this.colors);
    }

    /**
     * Returns the color pot.
     *
     * @return The color pot.
     */
    public MasterBoard getColorPot() { return this.colorPot; }

    /**
     * Sets the color pot.
     *
     * @param _colorPot The color pot to set.
     */
    public void setColorPot(MasterBoard _colorPot) {
        this.colorPot = _colorPot;
        addGrid(this.colorPot);
    }

    /**
     * Returns the color pot lists.
     *
     * @return The color pot lists.
     */
    public Map<Pawn.Color, List<Pawn>> getColorPotLists() { return this.colorPotLists; }

    /**
     * Sets the color pot lists.
     *
     * @param potLists The color pot lists to set.
     */
    public void setColorPotLists(Map<Pawn.Color, List<Pawn>> potLists) {
        this.colorPotLists = potLists;
        potLists.forEach((color, list) -> {
            list.forEach(this::addElement);
        });
    }

    /**
     * Returns the map of color pawns.
     *
     * @return The map of color pawns.
     */
    public Map getColorPawns() { return this.colorPawns; }

    /**
     * Sets the map of color pawns.
     *
     * @param colorPawns The map of color pawns to set.
     */
    public void setColorPawns(Map colorPawns) {
        this.colorPawns = colorPawns;
        colorPawns.forEach((key, value) -> {
            addElement((Pawn)value);
        });
    }

    /**
     * Returns the secret combination.
     *
     * @return The secret combination.
     */
    public String getSecretCombination() {
        return secretCombination;
    }

    /**
     * Sets the secret combination.
     *
     * @param input The input string representing the secret combination.
     */
    public void setSecretCombination(String input) {
        secretCombination = input;
    }

    /**
     * Returns the number of rows completed.
     *
     * @return The number of rows completed.
     */
    public int getRowsCompleted() {
        return this.rowsCompleted;
    }

    /**
     * Increments the number of rows completed by one.
     */
    public void incrementRowsCompleted() {
        this.rowsCompleted++;
    }

    /**
     * Returns the list of pawns placed on the board.
     *
     * @return The list of pawns placed on the board.
     */
    public ArrayList<Pawn> getPawns() {
        return this.pawns;
    }

    /**
     * Sets the list of pawns placed on the board.
     *
     * @param _pawns The list of pawns to set.
     */
    public void setPawns(ArrayList<Pawn> _pawns) {
        this.pawns = _pawns;
        for (Pawn p : _pawns) {
            addElement(p);
        }
    }

    /**
     * Adds a pawn to the appropriate list based on its location.
     *
     * @param _pawn The pawn to add.
     */
    public void addPawn(Pawn _pawn) {
        if (_pawn.isInMasterBoard()) {
            this.pawns.add(_pawn);
        } else {
            this.checkPawns.add(_pawn);
        }
    }

    /**
     * Returns the list of characters representing the answer.
     *
     * @return The list of characters representing the answer.
     */
    public List<Character> getAnswer() {
        return answer;
    }

    /**
     * Returns the list of pawns in the check board.
     *
     * @return The list of pawns in the check board.
     */
    public ArrayList<Pawn> getCheckPawns() {
        return this.checkPawns;
    }

    /**
     * Sets the list of pawns in the check board.
     *
     * @param _checkPawns The list of pawns in the check board.
     */
    public void setCheckPawns(ArrayList<Pawn> _checkPawns) {
        this.checkPawns = _checkPawns;
        for (Pawn cp : _checkPawns) {
            addElement(cp);
        }
    }

    /**
     * Returns the number of pawns in the secret combination that match both in color and position with the current guess.
     *
     * @return The number of matching pawns.
     */
    public int getNbMatch() { return this.nbMatch; }

    /**
     * Returns the number of pawns in the secret combination that match in color but not in position with the current guess.
     *
     * @return The number of common pawns.
     */
    public int getNbCommon() { return this.nbCommon; }

    public TextElement getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(TextElement _playerName) {
        playerName = _playerName;
    }

    /**
     * Returns the default element factory for the MasterStageModel.
     * Overrides the method from the StageModel class.
     *
     * @return The default element factory for creating stage elements.
     */
    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MasterStageFactory(this);
    }

    /**
     * Returns the current phase of the game
     *
     * @return The current phase of the game
     */
    public int getPhase() {
        return this.phase;
    }

    /**
     * Sets the current phase of the game with the param phase
     *
     * @param phase The new phase of the game
     */
    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * Returns the current state of the game
     *
     * @return The current state of the game
     */
    public int getGameState() {
        return this.gameState;
    }

}
