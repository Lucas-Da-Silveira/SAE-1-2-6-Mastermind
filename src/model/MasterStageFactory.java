package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

import java.util.*;

public class MasterStageFactory extends StageElementsFactory {
    private MasterStageModel stageModel;

    public MasterStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MasterStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        stageModel.setBoard(new MasterBoard(0, 2, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        stageModel.setCheckBoard(new MasterBoard(MasterSettings.NB_COLS + 10, 2, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        stageModel.setColorsBoard(new ColorsBoard(0, MasterSettings.NB_ROWS + 4, 1, MasterSettings.NB_COLORS, stageModel));
        stageModel.setColorPot(new MasterBoard(2 * MasterSettings.NB_COLS + 20, 2, Pawn.Color.values().length, MasterSettings.NB_ROWS*MasterSettings.NB_COLS, stageModel));
        stageModel.getColorPot().setVisible(false);

        final int[] i = {0};
        Map<Character, Pawn> colorPawns = new LinkedHashMap<>();
        Map<Pawn.Color, List<Pawn>> colorPot = new LinkedHashMap<>();
        for (Pawn.Color color : Pawn.Color.values()) {
            colorPawns.put(color.name().charAt(0), new Pawn(color, 0, i[0], stageModel));
            colorPot.put(color, new ArrayList<>());
            i[0]++;
        }
        stageModel.setColorPawns(colorPawns);

        i[0] = 0;
        colorPot.forEach((color, pot) -> {
            for(int j = 0; j < MasterSettings.NB_ROWS*MasterSettings.NB_COLS; j++) {
                Pawn p = new Pawn(color, i[0], j, stageModel);
                p.setVisible(false);
                pot.add(p);
            }
        });
        stageModel.setColorPotLists(colorPot);

        i[0] = 0;
        stageModel.getColorPawns().forEach((key, value) -> {
            if (i[0] < MasterSettings.NB_COLORS && !(((Pawn)value).getColor() == Pawn.Color.RED || ((Pawn)value).getColor() == Pawn.Color.WHITE)){
                stageModel.getColorsBoard().putElement((Pawn)value, 0, i[0]);
                i[0]++;
            }
        });

        i[0] = 0;
        final int[] j = {0};
        stageModel.getColorPotLists().forEach((color, list) -> {
            list.forEach(p -> {
                stageModel.getColorPot().putElement(p, i[0], j[0]);
                j[0]++;
            });
            j[0] = 0;
            i[0]++;
        });

    }
}
