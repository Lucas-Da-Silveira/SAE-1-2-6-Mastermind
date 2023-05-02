package controller;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

import java.util.*;

public class MasterDecider extends Decider {
    private static final Random random = new Random();
    private List<Character> possibleInput;

    public MasterDecider(Model model, Controller control) {
        super(model, control);
        this.possibleInput = getPossibleInputChar();
    }

    @Override
    public ActionList decide() {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();
        String randomLine = generateLine(gameStage, MasterSettings.AI_MODE);

        return MasterController.createActions(randomLine, gameStage, model);
    }

    public String generateLine(MasterStageModel gameStage, int AIMode) {
        if (AIMode == 0) return firstIAStrategy(gameStage);
        if (AIMode == 1) return secondIAStrategy(gameStage);
        return elseIAStrategy(gameStage);
    }

    public String elseIAStrategy(MasterStageModel gameStage) {
        return generateRandomLine(gameStage);
    }

    public static String generateRandomLine(MasterStageModel gameStage) {
        Object[] colors = Pawn.inputColor.keySet().toArray();
        StringBuilder result = new StringBuilder();

        int nbCols = gameStage.getBoard().getNbCols();
        for (int i = 0; i < nbCols; i++) {
            int r = random.nextInt(colors.length);
            result.append(colors[r]);
        }

        return result.toString();
    }

    private String firstIAStrategy(MasterStageModel gameStage) {
        String result;

        if(gameStage.getRowsCompleted() != 0 && numberCorrectColor(gameStage) < MasterSettings.NB_COLS && gameStage.getNbMatch() + gameStage.getNbCommon() > 0) {
            gameStage.answer.addAll(Collections.nCopies(gameStage.getNbMatch() + gameStage.getNbCommon(), this.possibleInput.get(gameStage.getRowsCompleted() - 1)));
            if(gameStage.answer.size() == MasterSettings.NB_COLS) {
                gameStage.turn = 0;
            }
        }

        if(numberCorrectColor(gameStage) != MasterSettings.NB_COLS && gameStage.getRowsCompleted() < MasterSettings.NB_COLORS) {
            result = this.possibleInput.get(gameStage.getRowsCompleted()).toString().repeat(MasterSettings.NB_COLS);
        } else {
            StringBuilder test = new StringBuilder();
            gameStage.answer.forEach(c -> test.append(c));
            gameStage.possibleAnswer = getPermutation(test.toString());
            gameStage.possibleAnswer = new ArrayList<>(new HashSet<>(gameStage.possibleAnswer));
            result = gameStage.possibleAnswer.get(gameStage.turn);
            gameStage.turn++;
        }

        return result;
    }

    private String secondIAStrategy(MasterStageModel gameStage) {
        String result;

        if(gameStage.getRowsCompleted() != 0 && numberCorrectColor(gameStage) < MasterSettings.NB_COLS && gameStage.getNbMatch() + gameStage.getNbCommon() > 0) {
            gameStage.answer.addAll(Collections.nCopies(gameStage.getNbMatch() + gameStage.getNbCommon(), this.possibleInput.get(gameStage.getRowsCompleted() - 1)));
            if(gameStage.answer.size() == MasterSettings.NB_COLS) {
                gameStage.turn = 0;
            }
        }

        if(numberCorrectColor(gameStage) != MasterSettings.NB_COLS && gameStage.getRowsCompleted() < MasterSettings.NB_COLORS) {
            result = this.possibleInput.get(gameStage.getRowsCompleted()).toString().repeat(MasterSettings.NB_COLS);
        } else {
            StringBuilder test = new StringBuilder();
            gameStage.answer.forEach(c -> test.append(c));
            gameStage.possibleAnswer = getPermutation(test.toString());
            gameStage.possibleAnswer = new ArrayList<>(new HashSet<>(gameStage.possibleAnswer));
            result = gameStage.possibleAnswer.get(gameStage.turn);
            gameStage.turn++;
        }

        return result;
    }

    private List<Character> getPossibleInputChar() {
        List<Character> input = new ArrayList<>();

        Pawn.inputColor.forEach(((character, color) -> {
            input.add(character);
        }));

        return input;
    }

    private int numberCorrectColor(MasterStageModel gameStage) {
        int result = 0;
        for (int i = 0; i < gameStage.answer.size(); i++) {
            result += gameStage.answer.get(i) == null ? 0 : 1;
        }
        return result;
    }

    private ArrayList<String> getPermutation(String str) {
        if (str.length() == 0) {
            ArrayList<String> empty = new ArrayList<>();
            empty.add("");
            return empty;
        }

        char c = str.charAt(0);
        String subStr = str.substring(1);
        ArrayList<String> prevResult = getPermutation(subStr);
        ArrayList<String> res = new ArrayList<>();

        for (String val : prevResult) {
            for (int i = 0; i <= val.length(); i++) {
                res.add(val.substring(0, i) + c + val.substring(i));
            }
        }

        return res;

    }

}
