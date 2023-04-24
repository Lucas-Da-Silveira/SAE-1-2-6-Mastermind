package controller;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import model.MasterStageModel;
import model.Pawn;

import java.util.Random;

public class MasterDecider extends Decider {
    private static final Random random = new Random();

    public MasterDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();
        String randomLine = generateRandomLine(gameStage);

        ActionList actions = new ActionList(true);
        for (int i = 0; i < randomLine.length(); i++) {
            Pawn.Color color = Pawn.inputColor.get(randomLine.charAt(i));
            int row = gameStage.getRowsCompleted();

            Pawn p = new Pawn(color, row, i, gameStage);

            gameStage.getBoard().putElement(p, row, i);

            GameAction move = new MoveAction(model, p, "masterboard", row, i);
            actions.addSingleAction(move);
        }

        return actions;
    }

    private String generateRandomLine(MasterStageModel gameStage) {
        Object[] colors = Pawn.inputColor.keySet().toArray();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < gameStage.getBoard().getNbCols(); i++) {
            int r = random.nextInt(colors.length);
            result.append(colors[r]);
        }

        return result.toString();
    }
}
