package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

import java.awt.*;
import java.util.ArrayList;

import static model.MasterSettings.NB_COLS;
import static model.MasterSettings.NB_ROWS;

public class MasterBoard extends GridElement {

    public MasterBoard(int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super("masterboard", x, y, nbRows, nbCols, gameStageModel);
    }
    
    public MasterBoard(int x, int y, GameStageModel gameStageModel) {
        super("masterboard", x, y, NB_ROWS, NB_COLS, gameStageModel);
    }

    public void setValidCells(int currentRow) {
        resetReachableCells(false);
        ArrayList<Point> valid = computeValidCells(currentRow);
        if (valid != null) {
            for (Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
        lookChanged = true;
    }

    private ArrayList<Point> computeValidCells(int currentRow) {
        ArrayList<Point> result = new ArrayList<>();

        for (int i = 0; i < NB_COLS; i++) {
            result.add(new Point(i, currentRow));
        }

        return result;
    }
}
