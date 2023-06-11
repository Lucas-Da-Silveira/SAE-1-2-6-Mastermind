package controller;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.geometry.Point2D;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

public class MasterController extends Controller {
    public MasterController(Model model, View view) {
        super(model, view);
        setControlMouse(new MasterControllerMouse(model, view, this));
        setControlAction(new MasterControllerAction(model, view, this));

    }

    /**
     * Executes the main loop of the game stage.
     * Reads input from the system console.
     * This method overrides the stageLoop() method from the superclass.
     */
    /* @Override
    public void stageLoop() {
        stageLoop(new BufferedReader(new InputStreamReader(System.in)));
    } */

    /**
     * Executes the main loop of the game stage.
     *
     * @param consoleIn The BufferedReader object to read input from the console.
     */
    /* public void stageLoop(BufferedReader consoleIn) {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();

        new Thread(() -> gameStage.setupCallbacks(this)).start();

        String line = "";
        if (model.getCurrentPlayer().getType() == Player.COMPUTER) {
            line = MasterDecider.generateRandomLine(gameStage);
            System.out.println(model.getCurrentPlayer().getName() + " chose the colors");
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
    } */

    public void nextPlayer() {
        // use the default method to compute next player
        if ((((MasterStageModel)model.getGameStage()).getPawns().size()%MasterSettings.NB_COLS != 0)) {
            return;
        }
        if (((MasterStageModel)model.getGameStage()).getPhase() == MasterStageModel.PHASE_CODE) {
            model.setNextPlayer();
        }

        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        MasterStageModel stageModel = (MasterStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MasterDecider decider = new MasterDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
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

        ActionList actions = createActions(line, gameStage, _model, this);

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
    public static ActionList createActions(String line, MasterStageModel gameStage, Model model, MasterController control) {
        ActionList actions = new ActionList(true);

        for (int i = 0; i < line.length(); i++) {
            Pawn.Color color = Pawn.inputColor.get(line.charAt(i));
            int row = gameStage.getRowsCompleted();

            Pawn p = gameStage.getColorPotLists().get(color).get(gameStage.getRowsCompleted() * MasterSettings.NB_COLS + i);
            p.setVisible(true);

            gameStage.getBoard().putElement(p, row, i);

            GridLook look = (GridLook) control.getElementLook(gameStage.getBoard());
            Point2D center = look.getRootPaneLocationForCellCenter(row, i);
            GameAction move = new MoveAction(model, p, "masterboard", row, i, AnimationTypes.MOVELINEARPROP_NAME, center.getX(), center.getY(), 10);

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
