package controller;

import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import javafx.scene.control.MenuItem;
import model.MasterBoard;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import view.CodeBoardLook;
import view.MasterRootPane;
import view.MasterView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MasterControllerUnitTest {
    Model model;
    MasterView view;
    MasterStageModel stageModel;

    @BeforeEach
    public void setup() {
        model = Mockito.spy(new Model());
        view = Mockito.mock(MasterView.class);
        stageModel = Mockito.mock(MasterStageModel.class);
        Mockito.when(model.getGameStage()).thenReturn(stageModel);
    }

    @Test
    public void testNextPlayer() {
        MasterRootPane rootPane = Mockito.mock(MasterRootPane.class);

        Mockito.when(view.getRootPane()).thenReturn(rootPane);
        Mockito.when(view.getMenuStart()).thenReturn(new MenuItem("New game"));
        Mockito.when(view.getMenuSettings()).thenReturn(new MenuItem("Settings"));
        Mockito.when(view.getMenuQuit()).thenReturn(new MenuItem("Quit"));

        MasterController controller = Mockito.spy(new MasterController(model, view));
        rootPane.addController(controller);

        Mockito.when(stageModel.getGameState()).thenReturn(MasterStageModel.GAME_STATE_WIN);

        controller.nextPlayer();
        Mockito.verify(stageModel, times(1)).computePartyResult(true);

        Mockito.when(stageModel.getGameState()).thenReturn(MasterStageModel.GAME_STATE_LOOSE);
        controller.nextPlayer();
        Mockito.verify(stageModel, times(1)).computePartyResult(false);

        /* Player p = Mockito.mock(Player.class);
        Mockito.when(p.getName()).thenReturn("player1");
        Mockito.when(p.getType()).thenReturn(Player.COMPUTER);

        Mockito.when(model.getCurrentPlayer()).thenReturn(p);
        Mockito.when(stageModel.getPlayerName()).thenReturn(new TextElement("p", stageModel));
        MasterBoard board = new MasterBoard("masterboard", 100, MasterSettings.CELL_SIZE + 10, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, stageModel);
        controller.mapElementLook = new HashMap<>();
        MasterBoardLook boardLook = Mockito.spy(new MasterBoardLook(10, board));
        Mockito.when(boardLook.getRootPaneLocationForCellCenter(anyInt(), anyInt())).thenReturn(new Point2D(2, 2));
        Mockito.when(controller.getElementLook(any())).thenReturn(boardLook);
        Mockito.when(model.getGrid(any())).thenReturn(board);
        Mockito.when(model.getGrids()).thenReturn(List.of(board));
        Mockito.when(stageModel.getBoard()).thenReturn(board);

        final int[] i = {0};
        Map<Character, Pawn> colorPawns = new LinkedHashMap<>();
        Map<Pawn.Color, List<Pawn>> colorPot = new LinkedHashMap<>();
        for (Pawn.Color color : Pawn.Color.values()) {
            colorPawns.put(color.name().charAt(0), new Pawn(color, 0, i[0], stageModel));
            colorPawns.get(color.name().charAt(0)).setVisible(false);
            colorPot.put(color, new ArrayList<>());
            i[0]++;
        }
        stageModel.setColorPawns(colorPawns);

        i[0] = 0;
        colorPot.forEach((color, pot) -> {
            for(int j = 0; j < (MasterSettings.NB_ROWS+1)*MasterSettings.NB_COLS; j++) {
                Pawn pawn = new Pawn(color, i[0], j, stageModel);
                pawn.setVisible(false);
                pot.add(pawn);
            }
        });

        Mockito.when(stageModel.getColorPotLists()).thenReturn(colorPot);
        // Mockito.when(controller.createActions(any(), any(), any(), any())).thenReturn(new ActionList(true));
        Mockito.when(stageModel.getRowsCompleted()).thenReturn(0);

        Mockito.when(stageModel.getGameState()).thenReturn(MasterStageModel.GAME_STATE_CONTINUE);
        controller.nextPlayer(); */
    }

    @Test
    public void testCreateActions() {
        final String input = "BYGP";

        MasterRootPane rootPane = Mockito.mock(MasterRootPane.class);

        Mockito.when(view.getRootPane()).thenReturn(rootPane);
        Mockito.when(view.getMenuStart()).thenReturn(new MenuItem("New game"));
        Mockito.when(view.getMenuSettings()).thenReturn(new MenuItem("Settings"));
        Mockito.when(view.getMenuQuit()).thenReturn(new MenuItem("Quit"));

        MasterController controller = Mockito.spy(new MasterController(model, view));
        rootPane.addController(controller);

        Mockito.when(stageModel.getPhase()).thenReturn(MasterStageModel.PHASE_CODE);

        Mockito.when(model.getGameStage()).thenReturn(stageModel);

        final int[] i = {0};
        Map<Character, Pawn> colorPawns = new LinkedHashMap<>();
        Map<Pawn.Color, List<Pawn>> colorPot = new LinkedHashMap<>();
        for (Pawn.Color color : Pawn.Color.values()) {
            colorPawns.put(color.name().charAt(0), new Pawn(color, 0, i[0], stageModel));
            colorPawns.get(color.name().charAt(0)).setVisible(false);
            colorPot.put(color, new ArrayList<>());
            i[0]++;
        }
        stageModel.setColorPawns(colorPawns);

        i[0] = 0;
        colorPot.forEach((color, pot) -> {
            for (int j = 0; j < (MasterSettings.NB_ROWS + 1) * MasterSettings.NB_COLS; j++) {
                Pawn pawn = new Pawn(color, i[0], j, stageModel);
                pawn.setVisible(false);
                pot.add(pawn);
            }
        });

        Mockito.when(stageModel.getColorPotLists()).thenReturn(colorPot);

        MasterBoard codeBoard = new MasterBoard("codeboard", MasterSettings.WINDOW_WIDTH / 2 - MasterSettings.CELL_SIZE * 2 * MasterSettings.NB_COLS / 2, (MasterSettings.WINDOW_HEIGHT - (MasterSettings.CELL_SIZE * 2)) / 2, 1, MasterSettings.NB_COLS, stageModel);
        Mockito.when(stageModel.getCodeBoard()).thenReturn(codeBoard);
        doReturn(new CodeBoardLook(10, 10, codeBoard)).when(controller).getElementLook(any());
        doReturn(codeBoard).when(model).getGrid(anyString());

        ActionList al = MasterController.createActions(input, stageModel, model, controller);

        int j = 0;
        for (List<GameAction> listAction : al.getActions()) {
            MoveAction action = (MoveAction) listAction.get(0);

            // check row
            Assertions.assertEquals(0, 0);
            // check col
            Assertions.assertEquals(action.getColDest(), j);
            // check pawn's color
            Assertions.assertEquals(((Pawn) action.getElement()).getColor(), Pawn.inputColor.get(input.charAt(j)));

            j++;
        }
    }
}
