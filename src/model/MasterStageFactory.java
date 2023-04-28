package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

public class MasterStageFactory extends StageElementsFactory {
    private MasterStageModel stageModel;

    public MasterStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MasterStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        stageModel.setBoard(new MasterBoard(0, 0, 12, 4, stageModel));
        stageModel.setCheckBoard(new MasterBoard(18, 0, 12, 4, stageModel));
        ColorsBoard colorsBoard = new ColorsBoard(0, 14, 1, 4, stageModel);
        stageModel.setColorsBoard(colorsBoard);
        final int[] i = {0};
        for (Pawn.Color color : Pawn.Color.values()) {
            stageModel.getColorPawns()[i[0]] = new Pawn(color, 0, i[0], stageModel);
            i[0]++;
        }
        for(int j = 0; j < stageModel.getColorPawns().length - 2; j++) {
            stageModel.getColorsBoard().putElement(stageModel.getColorPawns()[j], 0, j);
        }
    }
}
