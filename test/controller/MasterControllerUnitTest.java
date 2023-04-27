package controller;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;
import model.MasterBoard;
import model.MasterStageFactory;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class MasterControllerUnitTest {
    Model model;
    MasterController controller;
    MasterStageModel gameStage;
    MasterBoard board;

    @BeforeEach
    public void setup() {
        model = new Model();
        controller = new MasterController(model, new View(model));

        gameStage = Mockito.mock(MasterStageModel.class);
        board = Mockito.mock(MasterBoard.class);
        Mockito.when(gameStage.getBoard()).thenReturn(board);
    }

    @Test
    public void testVerifyLine() {
        Mockito.when(board.getNbCols()).thenReturn(4);

        // valid input
        Assertions.assertTrue(controller.verifyLine("BYGP", gameStage));

        // invalid input (line.length() != 4)
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
    }
}
