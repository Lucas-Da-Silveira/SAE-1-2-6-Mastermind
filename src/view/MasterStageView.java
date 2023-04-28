package view;

import boardifier.model.GameException;
import boardifier.model.GameStageModel;
import boardifier.view.ElementLook;
import boardifier.view.GameStageView;
import boardifier.view.GridLook;
import model.MasterStageModel;

public class MasterStageView extends GameStageView {
    public MasterStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() throws GameException {   
        MasterStageModel model = (MasterStageModel)gameStageModel;

        addLook(new GridLook(2, 1, model.getBoard(), -1, false));
        addLook(new GridLook(2, 1, model.getCheckBoard(), 1, false));
        addLook(new GridLook(5, 2, model.getColorsBoard(), -1, false));

        for(int i = 0; i < model.getColorPawns().length; i++) {
            addLook(new PawnLook(model.getColorPawns()[i]));
        }
        for(int i = 0; i < model.getColorPawns().length; i++) {
            System.out.println(model.getColorPawns()[i].getColor());
        }

    }
}
