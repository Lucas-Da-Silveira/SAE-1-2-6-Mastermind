package model;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;

import java.util.*;

public class MasterStageModel extends GameStageModel {
    private MasterBoard board;
    private MasterBoard checkBoard;
    private ColorsBoard colors;
    private MasterBoard colorPot;
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
    }

    public void setupCallbacks(Controller control) {
        onPutInGrid((element, gridDest, rowDest, colDest) -> {
            if (gridDest != board && gridDest != checkBoard) return;
            if (((Pawn)element).isInCheckBoard() && gridDest == checkBoard) {
                checkPawns.add((Pawn)element);
                return;
            }
            pawns.add((Pawn)element);
            if (pawns.size() % board.getNbCols() == 0) {
                ActionList actions = new ActionList(true);

                int yPos = 0;
                int row = getRowsCompleted();
                StringBuilder secretCombinationTmp = new StringBuilder(secretCombination);
                StringBuilder pawnsTmp = new StringBuilder();
                Pawn p;
                GameAction move;

                for (int i = 0; i < pawns.size(); i++) {
                    pawnsTmp.append(pawns.get(i).getColor().name().charAt(0));
                }

                pawnsTmp = new StringBuilder(pawnsTmp.substring(MasterSettings.NB_COLS * row, MasterSettings.NB_COLS * (row + 1)));
                nbMatch = numberCorrectPawns(secretCombinationTmp, pawnsTmp);
                nbCommon = numberCommonPawns(secretCombinationTmp, pawnsTmp);

                for (yPos = 0; yPos < nbMatch; yPos++) {
                    p = colorPotLists.get(Pawn.Color.RED).get(row * MasterSettings.NB_COLS + yPos);
                    p.setVisible(true);
                    checkBoard.putElement(p, row, yPos);
                    move = new MoveAction(model, p, "checkboard", row, yPos);
                    actions.addSingleAction(move);
                }

                for (; yPos < nbCommon + nbMatch; yPos++) {
                    p = colorPotLists.get(Pawn.Color.WHITE).get(row * MasterSettings.NB_COLS + yPos);
                    p.setVisible(true);
                    checkBoard.putElement(p, row, yPos);
                    move = new MoveAction(model, p, "checkboard", row, yPos);
                    actions.addSingleAction(move);
                }

                if (nbMatch == board.getNbCols()) {
                    // win
                    computePartyResult(true);
                }

                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();

                this.incrementRowsCompleted();

                if (rowsCompleted >= board.getNbRows() && nbMatch != board.getNbCols()) {
                    // loose
                    computePartyResult(false);
                }
            }
        });
    }

    private void computePartyResult(boolean win) {
        model.setIdWinner(win ? 1 : 0);
        model.stopStage();
    }

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

    public MasterBoard getBoard() {
        return this.board;
    }

    public void setBoard(MasterBoard _board) {
        this.board = _board;
        addGrid(this.board);
    }

    public MasterBoard getCheckBoard() {
        return this.checkBoard;
    }

    public void setCheckBoard(MasterBoard _checkBoard) {
        this.checkBoard = _checkBoard;
        addGrid(this.checkBoard);
    }

    public ColorsBoard getColorsBoard() { return this.colors; }

    public void setColorsBoard(ColorsBoard _colors) {
        this.colors = _colors;
        addGrid(this.colors);
    }
    public MasterBoard getColorPot() { return this.colorPot; }

    public void setColorPot(MasterBoard _colorPot) {
        this.colorPot = _colorPot;
        addGrid(this.colorPot);
    }

    public Map<Pawn.Color, List<Pawn>> getColorPotLists() { return this.colorPotLists; }

    public void setColorPotLists(Map<Pawn.Color, List<Pawn>> potLists) {
        this.colorPotLists = potLists;
        potLists.forEach((color, list) -> {
            list.forEach(this::addElement);
        });
    }

    public Map getColorPawns() { return this.colorPawns; }

    public void setColorPawns(Map colorPawns) {
        this.colorPawns = colorPawns;
        colorPawns.forEach((key, value) -> {
            addElement((Pawn)value);
        });
    }

    public String getSecretCombination() {
        return secretCombination;
    }

    public void setSecretCombination(String input) {
        secretCombination = input;
    }

    public int getRowsCompleted() {
        return this.rowsCompleted;
    }

    public void incrementRowsCompleted() {
        this.rowsCompleted++;
    }

    public ArrayList<Pawn> getPawns() {
        return this.pawns;
    }

    public void setPawns(ArrayList<Pawn> _pawns) {
        this.pawns = _pawns;
        for (Pawn p : _pawns) {
            addElement(p);
        }
    }

    public void addPawn(Pawn _pawn) {
        if (_pawn.isInMasterBoard()) {
            this.pawns.add(_pawn);
        } else {
            this.checkPawns.add(_pawn);
        }
    }

    public List<Character> getAnswer() {
        return answer;
    }

    public ArrayList<Pawn> getCheckPawns() {
        return this.checkPawns;
    }

    public void setCheckPawns(ArrayList<Pawn> _checkPawns) {
        this.checkPawns = _checkPawns;
        for (Pawn cp : _checkPawns) {
            addElement(cp);
        }
    }

    public int getNbMatch() { return this.nbMatch; }

    public int getNbCommon() { return this.nbCommon; }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MasterStageFactory(this);
    }
}
