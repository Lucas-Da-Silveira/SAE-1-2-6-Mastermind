package controller;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

import java.security.SecureRandom;
import java.util.*;

public class MasterDecider extends Decider {
    private static final Random random = new Random();
    private final List<Character> possibleInput;

    public MasterDecider(Model model, Controller control) {
        super(model, control);
        this.possibleInput = getPossibleInputChar(Pawn.inputColor);
    }

    /**
     * Generates a random line of colors and creates a corresponding list of actions based on the game stage and model.
     *
     * @return An ActionList object containing the created actions.
     */
    @Override
    public ActionList decide() {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();
        String randomLine = generateLine(gameStage, MasterSettings.AI_MODE);

        return MasterController.createActions(randomLine, gameStage, model);
    }

    /**
     * Generates a random line of colors and creates a corresponding list of actions based on the game stage and model.
     *
     * @return An ActionList object containing the created actions.
     */
    public String generateLine(MasterStageModel gameStage, int AIMode) {
        if (AIMode == 0) return firstIAStrategy(gameStage);
        if (AIMode == 1) return secondIAStrategy(gameStage);
        return elseIAStrategy(gameStage);
    }

    /**
     * Implements the strategy for the "else" case in the AI decision-making process.
     * Generates a random line of colors based on the given game stage.
     *
     * @param gameStage The MasterStageModel object representing the game stage.
     * @return A randomly generated line of colors.
     */
    public String elseIAStrategy(MasterStageModel gameStage) {
        return generateRandomLine(gameStage);
    }

    /**
     * Generates a random line of colors based on the given game stage.
     *
     * @param gameStage The MasterStageModel object representing the game stage.
     * @return A randomly generated line of colors.
     */
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

    /**
     * Implements the strategy for the first case in the AI decision-making process.
     *
     * @param gameStage The MasterStageModel object representing the game stage.
     * @return The generated line of colors based on the strategy.
     */
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
            gameStage.answer.forEach(test::append);
            gameStage.possibleAnswer = getPermutation(test.toString());
            gameStage.possibleAnswer = new ArrayList<>(new HashSet<>(gameStage.possibleAnswer));
            result = gameStage.possibleAnswer.get(gameStage.turn);
            gameStage.turn++;
        }

        return result;
    }

    /**
     * Implements the strategy for the second case in the AI decision-making process.
     *
     * @param gameStage The MasterStageModel object representing the game stage.
     * @return The generated line of colors based on the strategy.
     */
    private String secondIAStrategy(MasterStageModel gameStage) {
        String result;
        SecureRandom rand = new SecureRandom();
        StringBuilder temp = new StringBuilder();
        int randomIndex;

        if(gameStage.getRowsCompleted() == 0) {
            gameStage.possibleAnswer = getAllPossibilities(this.possibleInput, MasterSettings.NB_COLS);
            randomIndex = rand.nextInt(gameStage.possibleAnswer.size());
            for (int i = 0; i < gameStage.possibleAnswer.get(randomIndex).length(); i++) {
                gameStage.answer.add(gameStage.possibleAnswer.get(randomIndex).charAt(i));
            }
            result = gameStage.possibleAnswer.get(randomIndex);
        } else {
            if (gameStage.getNbCommon() + gameStage.getNbMatch() == MasterSettings.NB_COLS) {
                if (gameStage.turn == 0) {
                    gameStage.answer.forEach(temp::append);
                    gameStage.possibleAnswer = getPermutation(temp.toString());
                }
                result = gameStage.possibleAnswer.get(gameStage.turn);
                gameStage.turn++;
            } else {
                gameStage.answer.forEach(temp::append);
                gameStage.possibleAnswer.removeAll(getPermutation(temp.toString()));
                gameStage.answer = new ArrayList<>();
                randomIndex = rand.nextInt(gameStage.possibleAnswer.size());
                for (int i = 0; i < gameStage.possibleAnswer.get(randomIndex).length(); i++) {
                    gameStage.answer.add(gameStage.possibleAnswer.get(randomIndex).charAt(i));
                }
                result = gameStage.possibleAnswer.get(randomIndex);
            }
        }

        return result;
    }

    /**
     * Retrieves a list of possible input characters from the given input-color mapping.
     *
     * @param inputColor The input-color mapping.
     * @return A list of possible input characters.
     */
    public List<Character> getPossibleInputChar(Map<Character, Pawn.Color> inputColor) {
        List<Character> input = new ArrayList<>();

        inputColor.forEach(((character, color) -> {
            input.add(character);
        }));

        return input;
    }

    /**
     * Generates all possible combinations of characters from the given list of possible characters.
     *
     * @param possibleCharacters The list of possible characters.
     * @param size              The size of the combinations to generate.
     * @return A list of all possible combinations.
     */
    public List<String> getAllPossibilities(List<Character> possibleCharacters, int size) {
        List<String> result = new ArrayList<>();
        generatePossibility(result, possibleCharacters, "", size);
        return result;
    }

    /**
     * Recursively generates all possible combinations of characters from the given list of possible characters.
     *
     * @param list              The list to store the generated combinations.
     * @param possibleCharacters The list of possible characters.
     * @param str               The current combination being constructed.
     * @param size              The remaining size of the combinations to generate.
     */
    public void generatePossibility(List<String> list, List<Character> possibleCharacters, String str, int size) {
        if (size == 0) {
            list.add(str);
            return;
        }

        for (int i = 0; i < possibleCharacters.size(); i++) {
            generatePossibility(list, possibleCharacters, str + possibleCharacters.get(i).toString(), size - 1);
        }
    }

    /**
     * Calculates the number of correct colors in the current answer of the game stage.
     *
     * @param gameStage The MasterStageModel object representing the game stage.
     * @return The number of correct colors in the answer.
     */
    public int numberCorrectColor(MasterStageModel gameStage) {
        int result = 0;
        for (int i = 0; i < gameStage.getAnswer().size(); i++) {
            result += gameStage.getAnswer().get(i) == null ? 0 : 1;
        }
        return result;
    }

    /**
     * Generates all possible permutations of the characters in the given string.
     *
     * @param str The input string.
     * @return An ArrayList containing all possible permutations.
     */
    public ArrayList<String> getPermutation(String str) {
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
