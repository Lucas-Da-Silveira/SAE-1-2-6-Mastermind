package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Pawn extends GameElement {

    public Pawn(GameStageModel gameStageModel) {
        super(gameStageModel);
    }

    public Pawn(GameStageModel gameStageModel, int type) {
        super(gameStageModel, type);
    }

    public Pawn(double x, double y, GameStageModel gameStageModel) {
        super(x, y, gameStageModel);
    }

    public Pawn(double x, double y, GameStageModel gameStageModel, int type) {
        super(x, y, gameStageModel, type);
    }
}
