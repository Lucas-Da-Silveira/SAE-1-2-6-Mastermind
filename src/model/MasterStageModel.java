package model;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;

import java.util.ArrayList;

public class MasterStageModel extends GameStageModel {
    private MasterBoard board;
    private MasterBoard checkBoard;
    private ColorsBoard colors;
    private String secretCombination;
    private int rowsCompleted;
    private ArrayList<Pawn> pawns;
    private ArrayList<Pawn> checkPawns;
    private Pawn[] colorPawns;
    private int AIMode;

    public MasterStageModel(String name, Model model) {
        super(name, model);
        secretCombination = "";
        rowsCompleted = 0;
        pawns = new ArrayList<>();
        checkPawns = new ArrayList<>();
        colorPawns = new Pawn[Pawn.Color.values().length];
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

                int yPlacement = 0;
                int nbMatch = 0;
                StringBuilder secretCombinationTmp = new StringBuilder(secretCombination);

                for (int i = pawns.size() - board.getNbCols(), j = 0; i < pawns.size(); i++, j++) {
                    int matchingIndex = secretCombinationTmp.indexOf(Character.toString(pawns.get(i).getColor().name().charAt(0)));

                    if (pawns.get(i).getColor().name().charAt(0) == secretCombinationTmp.charAt(j) || matchingIndex != -1) {
                        int row = getRowsCompleted();

                        Pawn p;
                        if (pawns.get(i).getColor().name().charAt(0) == secretCombinationTmp.charAt(j)) {
                            p = new Pawn(Pawn.Color.RED, row, yPlacement, this);
                            secretCombinationTmp.setCharAt(matchingIndex, ' ');
                            nbMatch++;
                        } else {
                            p = new Pawn(Pawn.Color.WHITE, row, yPlacement, this);
                            secretCombinationTmp.setCharAt(j, ' ');
                        }

                        checkBoard.putElement(p, row, yPlacement);
                        GameAction move = new MoveAction(model, p, "checkboard", row, yPlacement);

                        actions.addSingleAction(move);
                        yPlacement++;
                    }
                }

                if (nbMatch == board.getNbCols()) {
                    // win
                    computePartyResult(true);
                }

                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();

                this.incrementRowsCompleted();

                if (rowsCompleted >= board.getNbRows()) {
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

    public Pawn[] getColorPawns() { return this.colorPawns; }

    public void setColorPawns(Pawn[] colorPawns) {
        this.colorPawns = colorPawns;
        for (int i = 0; i < colorPawns.length; i++) {
            addElement(colorPawns[i]);
        }
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

    public ArrayList<Pawn> getCheckPawns() {
        return this.checkPawns;
    }

    public void setCheckPawns(ArrayList<Pawn> _checkPawns) {
        this.checkPawns = _checkPawns;
        for (Pawn cp : _checkPawns) {
            addElement(cp);
        }
    }

    public int getAIMode() {
        return this.AIMode;
    }

    public void setAIMode(int mode) {
        this.AIMode = mode;
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MasterStageFactory(this);
    }
}
