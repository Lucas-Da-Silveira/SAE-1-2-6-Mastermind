package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;

import java.util.ArrayList;

public class MasterStageModel extends GameStageModel {
    private MasterBoard board;
    private MasterBoard checkBoard;
    private int rowsCompleted;
    private ArrayList<Pawn> pawns;
    private ArrayList<Pawn> checkPawns;

    public MasterStageModel(String name, Model model) {
        super(name, model);
        rowsCompleted = 0;
        pawns = new ArrayList<>();
        checkPawns = new ArrayList<>();
        setupCallbacks();
    }

    private void setupCallbacks() {
        onPutInGrid((element, gridDest, rowDest, colDest) -> {
            onPutInGrid( (element, gridDest, rowDest, colDest) -> {
                if (gridDest != board && gridDest != checkBoard) return;
                if (((Pawn)element).isInMasterBoard() && gridDest == board) {
                    pawns.add((Pawn)element);
                } else {
                    checkPawns.add((Pawn)element);
                }
                this.incrementRowsCompleted();
                if (rowsCompleted >= 12) {
                    computePartyResult();
                }
            });
        });
    }

    private void computePartyResult() {
        return
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

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MasterStageFactory(this);
    }
}
