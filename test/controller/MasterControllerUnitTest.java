package controller;

import boardifier.model.GridElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;
import model.MasterBoard;
import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

public class MasterControllerUnitTest {
    Model model;
    MasterController controller;
    MasterStageModel gameStage;
    MasterBoard board;

    @BeforeEach
    public void setup() {
        model = new Model();
        controller = new MasterController(model, new View(model));

        // used Mockito's spy method so that other functions in the class are initialized
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
        board = Mockito.spy(new MasterBoard(0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, gameStage));
        Mockito.when(gameStage.getBoard()).thenReturn(board);
    }

    @Test
    public void testVerifyLine() {
        // Mockito.when(board.getNbCols()).thenReturn(MasterSettings.NB_COLS);

        // valid input
        Assertions.assertTrue(controller.verifyLine("BYGP", gameStage));

        // invalid input (line.length() != board.getNbCols())
        Assertions.assertFalse(controller.verifyLine("BYGPP", gameStage));

        // invalid input ('W' is not a color)
        Assertions.assertFalse(controller.verifyLine("BYGW", gameStage));
    }

    @Test
    public void testCreateActions() {
        final int currentRow = 5;
        final String input = "BYGP";
        Mockito.when(gameStage.getRowsCompleted()).thenReturn(currentRow);

        ActionList al = MasterController.createActions(input, gameStage, model);

        int i = 0;
        for (List<GameAction> listAction : al.getActions()) {
            MoveAction action = (MoveAction) listAction.get(0);

            // check row
            Assertions.assertEquals(action.getRowDest(), currentRow);
            // check col
            Assertions.assertEquals(action.getColDest(), i);
            // check pawn's color
            Assertions.assertEquals(((Pawn)action.getElement()).getColor(), Pawn.inputColor.get(input.charAt(i)));

            i++;
        }

        al = MasterController.createActions("Z", gameStage, model);
        // check that pawn's color of an invalid color is null
        // the check that the input is valid is performed before the execution of this function
        Assertions.assertNull(((Pawn) al.getActions().get(0).get(0).getElement()).getColor());
    }

    @Test
    public void testAnalyseAndPlay() {
        int currentRow = 5;

        Mockito.when(gameStage.getGrids()).thenReturn(new ArrayList<GridElement>(List.of(board)));

        model.startGame(gameStage);
        Mockito.when(gameStage.getRowsCompleted()).thenReturn(currentRow);

        // valid move
        Assertions.assertTrue(controller.analyseAndPlay("GGGG", gameStage, model));

        // check that pawns put are all green
        for (int i = 0; i < board.getNbCols(); i++) {
            Assertions.assertEquals(((Pawn) board.getElement(gameStage.getRowsCompleted(), i)).getColor(), Pawn.Color.GREEN);
        }

        // false car input length > board.getNbCols()
        Assertions.assertFalse(controller.analyseAndPlay("GGGGG", gameStage, model));

        // false car 'W' not a valid color (only used as check pawn)
        Assertions.assertFalse(controller.analyseAndPlay("GGWG", gameStage, model));
    }

    @Test
    public void testNextPlayer() {
        String input = "YYPG";
        Model m = Mockito.mock(Model.class);

        Mockito.when(m.getCurrentPlayer()).thenReturn(Player.createComputerPlayer("computer1"));
        Mockito.when(m.getGameStage()).thenReturn(gameStage);
        
        MasterController c = Mockito.spy(new MasterController(m, new View(m)));

        Mockito.when(gameStage.getAIMode()).thenReturn(2);
        MasterDecider decider = Mockito.spy(new MasterDecider(m, c));

        BufferedReader consoleIn = Mockito.mock(BufferedReader.class);
        try {
            Mockito.when(consoleIn.readLine()).thenReturn(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        c.nextPlayer(decider, consoleIn);

        // gameStage.getBoard().getNbCols() is called in generateRandomLine in MasterDecider
        // when gameStage.getAIMode() != 0 && != 1
        Mockito.verify(decider, times(1)).elseIAStrategy(gameStage);

        Mockito.when(m.getCurrentPlayer()).thenReturn(Player.createHumanPlayer("player1"));

        c.nextPlayer(decider, consoleIn);

        // if not a computer, then it calls analyseAndPlay with valid human input "YYPG"
        Mockito.verify(c, times(1)).analyseAndPlay(input, gameStage, m);
    }
}
