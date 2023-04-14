package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class CodeBoard extends GridElement {

    public CodeBoard(int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        super("codeboard", x, y, nbRows, nbCols, gameStageModel);
    }
    
    public CodeBoard(int x, int y, GameStageModel gameStageModel) {
        super("codeboard", x, y, 1, 4, gameStageModel);
    }

}
