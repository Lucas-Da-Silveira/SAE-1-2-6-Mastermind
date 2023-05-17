package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class ColorsBoard extends GridElement {
    public ColorsBoard(int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super("colorsboard", x, y, nbRows, nbCols, gameStageModel);
    }

    public ColorsBoard(int x, int y, GameStageModel gameStageModel) {
        super("colorsboard", x, y, 1, MasterSettings.NB_COLORS, gameStageModel);
    }
}
