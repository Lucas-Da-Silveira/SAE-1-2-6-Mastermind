package view;

import boardifier.model.GameException;
import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.GridLook;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

public class MasterStageView extends GameStageView {

    /**
     * Constructs a new instance of the MasterStageView class with the given name and game stage model.
     *
     * @param name            the name of the stage view
     * @param gameStageModel  the game stage model associated with the stage view
     */
    public MasterStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    /**
     * Overrides the createLooks() method to create looks for the Master stage view.
     *
     * @throws GameException if an error occurs during the creation of looks
     */
    @Override
    public void createLooks() throws GameException {
        MasterStageModel model = (MasterStageModel)gameStageModel;

        addLook(new GridLook(2, 1, model.getBoard(), -1, false));
        addLook(new GridLook(2, 1, model.getCheckBoard(), 1, false));
        addLook(new GridLook(5, 2, model.getColorsBoard(), -1, false));
        addLook(new GridLook(2, 1, model.getColorPot(), -1, false));

        final int[] i = {0};
        model.getColorPawns().forEach((key, value) -> {
            Pawn.Color color = ((Pawn)value).getColor();
            if(i[0] < MasterSettings.NB_COLORS && !(color == Pawn.Color.RED || color == Pawn.Color.WHITE)) {
                addLook(new PawnLook((Pawn)value));
                System.out.println(color + " (" + color.name().charAt(0) +")");
                i[0]++;
            }
        });

        model.getColorPotLists().forEach((color, list) -> {
            list.forEach(p -> {
                addLook(new PawnLook(p));
            });
        });

    }
}
