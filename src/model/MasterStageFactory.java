package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

import java.util.Arrays;

public class MasterStageFactory extends StageElementsFactory {
    private MasterStageModel stageModel;

    public MasterStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MasterStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        stageModel.setBoard(new MasterBoard(0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        stageModel.setCheckBoard(new MasterBoard(18, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        ColorsBoard colorsBoard = new ColorsBoard(0, 14, 1, MasterSettings.NB_COLORS, stageModel);
        stageModel.setColorsBoard(colorsBoard);

        final int[] i = {0};

        for (Pawn.Color color : Pawn.Color.values()) {
            stageModel.getColorPawns()[i[0]] = new Pawn(color, 0, i[0], stageModel);
            i[0]++;
        }

        for (int j = 0; j < stageModel.getColorPawns().length; j++) {
            if (j >= MasterSettings.NB_COLORS) break;
            if (stageModel.getColorPawns()[j].getColor() == Pawn.Color.RED || stageModel.getColorPawns()[j].getColor() == Pawn.Color.WHITE) continue;
            stageModel.getColorsBoard().putElement(stageModel.getColorPawns()[j], 0, j);
        }
    }
}
