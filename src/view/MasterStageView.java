package view;

import boardifier.model.GameException;
import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.GridLook;
import boardifier.view.TextLook;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

public class MasterStageView extends GameStageView {

    /**
     * Constructs a new instance of the MasterStageView class with the given name and game stage model.
     *
     * @param name           the name of the stage view
     * @param gameStageModel the game stage model associated with the stage view
     */
    public MasterStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        width = MasterSettings.WINDOW_WIDTH;
        height = MasterSettings.WINDOW_HEIGHT;
    }

    /**
     * Overrides the createLooks() method to create looks for the Master stage view.
     *
     * @throws GameException if an error occurs during the creation of looks
     */
    @Override
    public void createLooks() throws GameException {
        MasterStageModel model = (MasterStageModel) gameStageModel;
        addLook(new MasterBoardLook(MasterSettings.CELL_SIZE * MasterSettings.NB_ROWS, model.getBoard()));
        addLook(new CheckBoardLook(MasterSettings.CELL_SIZE * MasterSettings.NB_ROWS, model.getCheckBoard()));
        addLook(new PawnPotLook(MasterSettings.CELL_SIZE * 2 * MasterSettings.NB_COLORS, MasterSettings.CELL_SIZE * 2, model.getColorsBoard()));
        addLook(new MasterBoardLook(400, model.getColorPot()));
        addLook(new CodeBoardLook(MasterSettings.CELL_SIZE * 2 * MasterSettings.NB_COLS, MasterSettings.CELL_SIZE * 2, model.getCodeBoard()));

        model.getColorPawns().forEach((key, value) -> {
                addLook(new PawnLook(MasterSettings.CELL_SIZE/3, (Pawn) value));
        });

        model.getColorPotLists().forEach((color, list) -> {
            list.forEach(p -> {
                addLook(new PawnLook(MasterSettings.CELL_SIZE/3, p));
            });
        });

        addLook(new TextLook(24, "0x000000", model.getPlayerName()));
    }
}
