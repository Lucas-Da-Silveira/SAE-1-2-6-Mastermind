package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class MasterBoard extends GridElement {

    public MasterBoard(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super(name, x, y, nbRows, nbCols, gameStageModel);
    }

    public MasterBoard(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel, int type) {
        super(name, x, y, nbRows, nbCols, gameStageModel, type);
    }

}
