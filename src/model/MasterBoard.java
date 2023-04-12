package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class MasterBoard extends GridElement {

    public MasterBoard(int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super("masterboard", x, y, nbRows, nbCols, gameStageModel);
    }
}
