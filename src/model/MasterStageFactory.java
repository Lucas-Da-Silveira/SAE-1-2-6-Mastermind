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
        stageModel.setColorsBoard(new ColorsBoard(0, 14, 1, 4, stageModel));
    }
}
