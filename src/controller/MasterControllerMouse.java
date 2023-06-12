package controller;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import model.*;

import java.util.List;

public class MasterControllerMouse extends ControllerMouse implements EventHandler<MouseEvent> {

    public MasterControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        if (!model.isCaptureMouseEvent()) return;
        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Point2D clic = new Point2D(event.getSceneX(), event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);

        System.out.println("click in "+event.getSceneX()+","+event.getSceneY());
        /*
        for(GameElement element : list) {
            System.out.println(element);
            if(element instanceof Pawn) {
                System.out.println(((Pawn)element).getColor().name());
                System.out.println(model.getGameStage().getState());
            }
        }
        */

        MasterStageModel stageModel = (MasterStageModel) model.getGameStage();
        if (stageModel.getState() == MasterStageModel.STATE_SELECTPAWN) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    Pawn pawn = (Pawn) element;
                    if (!stageModel.getPawns().contains(pawn) && !stageModel.getCheckPawns().contains(pawn)) {
                        element.toggleSelected();
                        stageModel.setState(MasterStageModel.STATE_SELECTDEST);
                        return;
                    }
                }
            }
        } else if (stageModel.getState() == MasterStageModel.STATE_SELECTDEST) {
            // first check if the click is on the current selected pawn. In this case, unselect it
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(MasterStageModel.STATE_SELECTPAWN);
                    return;
                }
                if (element.getType() == ElementTypes.getType("pawn") && stageModel.getColorsBoard().contains(element)) {
                    stageModel.unselectAll();
                    element.toggleSelected();
                }
            }
            // secondly, search if the board has been clicked. If not just return
            boolean boardClicked = false;
            for (GameElement element : list) {
                if ((element == stageModel.getBoard() && stageModel.getPhase() == MasterStageModel.PHASE_GAME) || (element == stageModel.getCodeBoard() && stageModel.getPhase() == MasterStageModel.PHASE_CODE)) {
                    boardClicked = true;
                    break;
                }
            }
            if (!boardClicked) return;
            // get the board, pot,  and the selected pawn to simplify code in the following
            MasterBoard board;
            if (stageModel.getPhase() == MasterStageModel.PHASE_CODE) {
                board = stageModel.getCodeBoard();
            } else {
                board = stageModel.getBoard();
            }
            ColorsBoard pot = stageModel.getColorsBoard();
            GameElement pawn = model.getSelected().get(0);

            // thirdly, get the clicked cell in the main board
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(clic);
            // get the cell in the pot that owns the selected pawn
            int[] from = pot.getElementCell(pawn);
            System.out.println("try to move pawn from pot " + from[0] + "," + from[1] + " to board " + dest[0] + "," + dest[1]);

            // if the destination cell is valid for the selected pawn
            if (board.canReachCell(dest[0], dest[1])) {
                // build the list of actions to do, and pass to the next player when done
                ActionList actions = new ActionList(true);
                // determine the destination point in the root pane
                Point2D center = lookBoard.getRootPaneLocationForCellCenter(dest[0], dest[1]);
                // create an action with a linear move animation, with 10 pixel/frame
                Pawn pawnToMove;
                if (stageModel.getPhase() == MasterStageModel.PHASE_CODE) {
                    pawnToMove = stageModel.getColorPotLists().get(((Pawn)pawn).getColor()).get(MasterSettings.NB_ROWS * MasterSettings.NB_COLS + dest[1]);
                } else {
                    pawnToMove = stageModel.getColorPotLists().get(((Pawn)pawn).getColor()).get(dest[0] * MasterSettings.NB_COLS + dest[1]);
                    stageModel.getPawns().add(pawnToMove);
                }
                GameAction move;
                move = new MoveAction(model, pawnToMove, board.getName(), dest[0], dest[1], AnimationTypes.MOVETELEPORT_NAME, center.getX(), center.getY(), 10);
                pawnToMove.setVisible(true);
                // add the action to the action list.
                if (model.getCurrentPlayer().getType() != Player.COMPUTER) {
                    actions.addSingleAction(move);
                }
                stageModel.unselectAll();
                stageModel.setState(MasterStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();
            }
        }
    }
}