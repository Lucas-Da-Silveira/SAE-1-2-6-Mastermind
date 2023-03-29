package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class CheckBoard extends GridElement {

    public CheckBoard(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super(name, x, y, nbRows, nbCols, gameStageModel);
    }

    public CheckBoard(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel, int type) {
        super(name, x, y, nbRows, nbCols, gameStageModel, type);
    }

}
