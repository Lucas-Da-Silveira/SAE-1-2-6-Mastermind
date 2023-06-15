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
            Pawn pawn;
            GridLook lookBoard = (GridLook) this.getElementLook(stageModel.getCheckBoard());
            Point2D center;
            int row = stageModel.getRowsCompleted() - 1;
            for (yPos = 0; yPos < nbMatch; yPos++) {
                pawn = stageModel.getColorPotLists().get(Pawn.Color.RED).get(row * MasterSettings.NB_COLS + yPos);
                pawn.setVisible(true);
                center = lookBoard.getRootPaneLocationForCellCenter(row, yPos);
                stageModel.getCheckBoard().putElement(pawn, row, yPos, false);
                pawn.setLocation(center.getX()-MasterSettings.CELL_SIZE/3, center.getY()-MasterSettings.CELL_SIZE/3);
                //move = new MoveAction(model, pawn, stageModel.getCheckBoard().getName(), row, yPos, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
                //actions.addSingleAction(move);

            }
            for (; yPos < nbCommon + nbMatch; yPos++) {
                pawn = stageModel.getColorPotLists().get(Pawn.Color.WHITE).get(row * MasterSettings.NB_COLS + yPos);
                pawn.setVisible(true);
                center = lookBoard.getRootPaneLocationForCellCenter(row, yPos);
                stageModel.getCheckBoard().putElement(pawn, row, yPos, false);
                pawn.setLocation(center.getX()-MasterSettings.CELL_SIZE/3, center.getY()-MasterSettings.CELL_SIZE/3);
                //move = new MoveAction(model, pawn, stageModel.getCheckBoard().getName(), row, yPos, AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
                //actions.addSingleAction(move);
            }
        }
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
}
