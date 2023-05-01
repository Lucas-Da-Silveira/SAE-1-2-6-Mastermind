package controller;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

import java.util.Arrays;
import java.util.Random;

public class MasterDecider extends Decider {
    private static final Random random = new Random();

    public MasterDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();
        String randomLine = generateLine(gameStage, MasterSettings.AI_MODE);

        return MasterController.createActions(randomLine, gameStage, model);
    }

    public String generateLine(MasterStageModel gameStage, int AIMode) {
        // if (AIMode == 0) return firstIAStrategy();
        // if (AIMode == 1) return secondIAStrategy();
        return elseIAStrategy(gameStage);
    }

    public String elseIAStrategy(MasterStageModel gameStage) {
        return generateRandomLine(gameStage);
    }

    public static String generateRandomLine(MasterStageModel gameStage) {
        Object[] colors = Pawn.inputColor.keySet().toArray();
        StringBuilder result = new StringBuilder();
        System.out.println(Arrays.toString(colors));

        for (int i = 0; i < gameStage.getBoard().getNbCols(); i++) {
            int r = random.nextInt(colors.length);
            result.append(colors[r]);
        }

        return result.toString();
    }
}
