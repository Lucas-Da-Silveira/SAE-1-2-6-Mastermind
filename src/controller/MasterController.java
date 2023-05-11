package controller;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

public class MasterController extends Controller {
    public MasterController(Model model, View view) {
        super(model, view);
    }

    /**
     * Executes the main loop of the game stage.
     * Reads input from the system console.
     * This method overrides the stageLoop() method from the superclass.
     */
    @Override
    public void stageLoop() {
        stageLoop(new BufferedReader(new InputStreamReader(System.in)));
    }

    /**
     * Executes the main loop of the game stage.
     *
     * @param consoleIn The BufferedReader object to read input from the console.
     */
    public void stageLoop(BufferedReader consoleIn) {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();

        new Thread(() -> gameStage.setupCallbacks(this)).start();

        String line = "";
        if (model.getCurrentPlayer().getType() == Player.COMPUTER) {
            line = MasterDecider.generateRandomLine(gameStage);
            System.out.println(model.getCurrentPlayer().getName() + ", chose the colors");
        } else {
            boolean ok = false;
            while (!ok) {
                try {
                    System.out.print(model.getCurrentPlayer().getName() + ", choose the colors : ");
                    line = consoleIn.readLine().toUpperCase();
                    ok = verifyLine(line, gameStage);
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException ignored) {}
            }
        }

        gameStage.setSecretCombination(line);

        model.setNextPlayer();

        update();
        while (!model.isEndStage()) {
            nextPlayer(new MasterDecider(model, this), consoleIn);
            update();
        }
        stopStage();
        endGame();
    }

    /**
     * Determines the next player's turn and prompts for their input or initiates computer play.
     *
     * @param decider     The MasterDecider object used for computer player's decision-making.
     * @param consoleIn   The BufferedReader object to read input from the console.
     */
    public void nextPlayer(MasterDecider decider, BufferedReader consoleIn) {
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine().toUpperCase();
                    ok = analyseAndPlay(line, (MasterStageModel) model.getGameStage(), model);
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException ignored) {}
            }
        }
    }

    /**
     * Analyzes the input line and plays the corresponding actions if the line is valid.
     *
     * @param line       The input line to analyze.
     * @param gameStage  The MasterStageModel object representing the game stage.
     * @param _model     The Model object representing the game model.
     * @return True if the line is valid and the actions are played successfully, False otherwise.
     */
    public boolean analyseAndPlay(String line, MasterStageModel gameStage, Model _model) {
        if (!verifyLine(line, gameStage)) return false;

        ActionList actions = createActions(line, gameStage, _model);

        ActionPlayer play = new ActionPlayer(_model, this, actions);
        play.start();

        return true;
    }

    /**
     * Creates a list of actions based on the input line and game stage information.
     *
     * @param line       The input line representing the colors to create actions for.
     * @param gameStage  The MasterStageModel object representing the game stage.
     * @param model      The Model object representing the game model.
     * @return An ActionList object containing the created actions.
     */
    public static ActionList createActions(String line, MasterStageModel gameStage, Model model) {
        ActionList actions = new ActionList(true);

        for (int i = 0; i < line.length(); i++) {
            Pawn.Color color = Pawn.inputColor.get(line.charAt(i));
            int row = gameStage.getRowsCompleted();

            Pawn p = gameStage.getColorPotLists().get(color).get(gameStage.getRowsCompleted() * MasterSettings.NB_COLS + i);
            p.setVisible(true);

            gameStage.getBoard().putElement(p, row, i);
            GameAction move = new MoveAction(model, p, "masterboard", row, i);

            actions.addSingleAction(move);
        }

        return actions;
    }

    /**
     * Verifies if the input line is valid.
     *
     * @param line       The input line to analyze.
     * @param gameStage  The MasterStageModel object representing the game stage.
     * @return True if the line is valid, False otherwise.
     */
    public boolean verifyLine(String line, MasterStageModel gameStage) {
        if (line.length() != gameStage.getBoard().getNbCols()) return false;

        for (int i = 0; i < line.length(); i++) {
            char l = line.charAt(i);
            if (Pawn.inputColor.get(l) == null) return false;
        }

        return true;
    }
}
