package controller;

import boardifier.model.Model;
import boardifier.view.View;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class ControllerUnitTest {
    protected Model model;
    protected MasterController controller;
    protected MasterStageModel gameStage;
    protected MasterBoard board;

    public void init() {
        model = new Model();
        controller = new MasterController(model, new View(model));

        // used Mockito's spy method so that other functions in the class are initialized
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
        board = Mockito.spy(new MasterBoard(0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, gameStage));
        Mockito.when(gameStage.getBoard()).thenReturn(board);

        Pawn.adjustPawnColors();
        new MasterStageFactory(gameStage).setup();
    }

    protected static int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }
}
