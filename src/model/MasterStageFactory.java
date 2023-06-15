package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MasterStageFactory extends StageElementsFactory {
    private final MasterStageModel stageModel;

    public MasterStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MasterStageModel) gameStageModel;
        MasterSettings.WINDOW_HEIGHT = MasterSettings.CELL_SIZE * (MasterSettings.NB_ROWS + 4);
        MasterSettings.WINDOW_WIDTH = MasterSettings.CELL_SIZE * (MasterSettings.NB_COLS * 2 + 4);
    }

    /**
     * Sets up the initial configuration of the game stage.
     * Overrides the setup method from the parent class.
     */
    @Override
    public void setup() {
        int spaceBetweenGrid = (MasterSettings.WINDOW_WIDTH - 2 * MasterSettings.NB_COLS * MasterSettings.CELL_SIZE) / 3;
        stageModel.setBoard(new MasterBoard("masterboard", spaceBetweenGrid, MasterSettings.CELL_SIZE + 10, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        stageModel.setCheckBoard(new MasterBoard("checkboard", MasterSettings.NB_COLS * MasterSettings.CELL_SIZE + 2 * spaceBetweenGrid, MasterSettings.CELL_SIZE + 10, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel));
        stageModel.setColorsBoard(new ColorsBoard(MasterSettings.WINDOW_WIDTH / 2 - MasterSettings.CELL_SIZE * 2 * MasterSettings.NB_COLORS / 2, MasterSettings.WINDOW_HEIGHT - (MasterSettings.CELL_SIZE * 2 + 10), 1, MasterSettings.NB_COLORS, stageModel));
        stageModel.setColorPot(new MasterBoard("colorpot", 2 * MasterSettings.NB_COLS + 20, 2, Pawn.Color.values().length, (MasterSettings.NB_ROWS + 1) * MasterSettings.NB_COLS + MasterSettings.CELL_SIZE, stageModel));
        stageModel.getColorPot().setVisible(false);
        stageModel.getBoard().setVisible(false);
        stageModel.getCheckBoard().setVisible(false);
        stageModel.setCodeBoard(new MasterBoard("codeboard", MasterSettings.WINDOW_WIDTH / 2 - MasterSettings.CELL_SIZE * 2 * MasterSettings.NB_COLS / 2, (MasterSettings.WINDOW_HEIGHT - (MasterSettings.CELL_SIZE * 2)) / 2, 1, MasterSettings.NB_COLS, stageModel));

        final int[] i = {0};
        Map<Character, Pawn> colorPawns = new LinkedHashMap<>();
        Map<Pawn.Color, List<Pawn>> colorPot = new LinkedHashMap<>();
        for (Pawn.Color color : Pawn.Color.values()) {
            colorPawns.put(color.name().charAt(0), new Pawn(color, 0, i[0], stageModel));
            colorPawns.get(color.name().charAt(0)).setVisible(false);
            colorPot.put(color, new ArrayList<>());
            i[0]++;
        }
        stageModel.setColorPawns(colorPawns);

        i[0] = 0;
        colorPot.forEach((color, pot) -> {
            for (int j = 0; j < (MasterSettings.NB_ROWS + 1) * MasterSettings.NB_COLS; j++) {
                Pawn p = new Pawn(color, i[0], j, stageModel);
                p.setVisible(false);
                pot.add(p);
            }
        });
        stageModel.setColorPotLists(colorPot);

        i[0] = 0;
        stageModel.getColorPawns().forEach((key, value) -> {
            if (i[0] < MasterSettings.NB_COLORS && !(((Pawn) value).getColor() == Pawn.Color.RED || ((Pawn) value).getColor() == Pawn.Color.WHITE)) {
                stageModel.getColorsBoard().putElement((Pawn) value, 0, i[0]);
                ((Pawn) value).setVisible(true);
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

        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(10, 30);
        text.setLocationType(GameElement.LOCATION_TOPLEFT);
        stageModel.setPlayerName(text);
    }
}
