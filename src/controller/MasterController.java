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
        MasterStageModel stageModel = (MasterStageModel)model.getGameStage();
        if (stageModel.getGameState() == MasterStageModel.GAME_STATE_WIN) {
            stageModel.computePartyResult(true);
            return;
        } else if (stageModel.getGameState() == MasterStageModel.GAME_STATE_LOOSE) {
            stageModel.computePartyResult(false);
            return;
        }
        if (stageModel.getPhase() == MasterStageModel.PHASE_CODE && stageModel.getSecretCombination().length() == MasterSettings.NB_COLS) {
            model.setNextPlayer();
           stageModel.setPhase(MasterStageModel.PHASE_GAME);
        }
        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        stageModel.getPlayerName().setText(p.getName());
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MasterDecider decider = new MasterDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
            try  {
                play.join(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        if (stageModel.getPhase() == MasterStageModel.PHASE_GAME && stageModel.getPawns().size()%MasterSettings.NB_COLS == 0 && !stageModel.getPawns().isEmpty()) {
            int nbMatch = stageModel.getNbMatch();
            int nbCommon = stageModel.getNbCommon();
            int yPos;
            GameAction move;
            Pawn pawn;
            GridLook lookBoard = (GridLook) this.getElementLook(stageModel.getCheckBoard());
            Point2D center;
            ActionList actions = new ActionList(false);
            int row = stageModel.getRowsCompleted() - 1;
            for (yPos = 0; yPos < nbMatch; yPos++) {
                pawn = stageModel.getColorPotLists().get(Pawn.Color.RED).get(row * MasterSettings.NB_COLS + yPos);
                pawn.setVisible(true);
                center = lookBoard.getRootPaneLocationForCellCenter(row, yPos);
                move = new MoveAction(model, pawn, stageModel.getCheckBoard().getName(), row, yPos, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
                actions.addSingleAction(move);

            }
            for (; yPos < nbCommon + nbMatch; yPos++) {
                pawn = stageModel.getColorPotLists().get(Pawn.Color.WHITE).get(row * MasterSettings.NB_COLS + yPos);
                pawn.setVisible(true);
                center = lookBoard.getRootPaneLocationForCellCenter(row, yPos);
                move = new MoveAction(model, pawn, stageModel.getCheckBoard().getName(), row, yPos, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
                actions.addSingleAction(move);
            }
            ActionPlayer run = new ActionPlayer(model, this, actions);
            run.start();
            try  {
                run.join(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
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
            Pawn p;
            GridLook look;
            Point2D center;
            GameAction move;
            if (gameStage.getPhase() == MasterStageModel.PHASE_CODE) {
                p = gameStage.getColorPotLists().get(color).get(MasterSettings.NB_ROWS * MasterSettings.NB_COLS + i);
                p.setVisible(true);
                gameStage.getCodeBoard().putElement(p, 0, i);
                look = (GridLook) control.getElementLook(gameStage.getCodeBoard());
                center = look.getRootPaneLocationForCellCenter(0, i);
                move = new MoveAction(model, p, "codeboard", 0, i, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
            } else {
                look = (GridLook) control.getElementLook(gameStage.getBoard());
                p = gameStage.getColorPotLists().get(color).get(gameStage.getRowsCompleted() * MasterSettings.NB_COLS + i);
                p.setVisible(true);
                gameStage.getPawns().add(p);
                gameStage.getBoard().putElement(p, row, i);
                center = look.getRootPaneLocationForCellCenter(row, i);
                move = new MoveAction(model, p, "masterboard", row, i, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
            }
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
