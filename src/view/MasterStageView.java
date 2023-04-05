package view;

import boardifier.model.GameException;
import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;

public class MasterStageView extends GameStageView {
    public MasterStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() throws GameException {}
}
