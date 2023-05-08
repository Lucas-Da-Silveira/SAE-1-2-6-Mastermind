package model;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.view.View;
import controller.MasterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;


public class MasterModelUnitTest {
    Controller controller;
    MasterStageModel gameStage;
    MasterBoard board;
    MasterBoard checkBoard;
    Model model;
    MasterStageModel masterStageModel;


    @BeforeEach
    public void setup() {
        model = new Model();
        controller = new MasterController(model, new View(model));

        // used Mockito's spy method so that other functions in the class are initialized
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
        board = Mockito.spy(new MasterBoard(0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, gameStage));
        Mockito.when(gameStage.getBoard()).thenReturn(board);

        Pawn.adjustPawnColors();
        new MasterStageFactory(gameStage).setup();
    }

    @Test
    public void testNumberCommonPawns() {
        StringBuilder code = new StringBuilder("YGPB");
        StringBuilder answer = new StringBuilder("GYBP");

        Assertions.assertEquals(gameStage.numberCommonPawns(code, answer), 4);

        code = new StringBuilder("GGGG");
        answer = new StringBuilder("BBBB");

        Assertions.assertEquals(gameStage.numberCommonPawns(code, answer), 0);

        Assertions.assertEquals(gameStage.numberCommonPawns(new StringBuilder(""), new StringBuilder("")), 0);

        code = new StringBuilder("GBYP");
        answer = new StringBuilder("YKKK");
        Assertions.assertEquals(gameStage.numberCommonPawns(code, answer), 1);
    }

    @Test
    public void testNumberCorrectPawns() {
        StringBuilder code = new StringBuilder("YGPB");
        StringBuilder answer = new StringBuilder("GYBP");

        Assertions.assertEquals(gameStage.numberCorrectPawns(code, answer), 0);

        code = new StringBuilder("YBGB");
        answer = new StringBuilder("GYGY");

        Assertions.assertEquals(gameStage.numberCorrectPawns(new StringBuilder(code), new StringBuilder(answer)), 1);
        Assertions.assertEquals(gameStage.numberCorrectPawns(answer, code), 1);
        Assertions.assertEquals(code.toString(), "YBXB");
        Assertions.assertEquals(answer.toString(), "GYXY");

        Assertions.assertEquals(gameStage.numberCorrectPawns(new StringBuilder("XXXX"), new StringBuilder("XXXX")), 0);
        Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> { gameStage.numberCorrectPawns(new StringBuilder("XXX"), new StringBuilder("XX")); });
    }
    @Test
    public void testAddPawn() {
        gameStage.addPawn(new Pawn(Pawn.Color.WHITE, 0, 0, gameStage));
        gameStage.addPawn(new Pawn(Pawn.Color.RED, 0, 1, gameStage));

        Assertions.assertEquals(gameStage.getCheckPawns().size(), 2);
        Assertions.assertEquals(gameStage.getPawns().size(), 0);

        gameStage.addPawn(new Pawn(Pawn.Color.GREEN, 0, 2, gameStage));
        gameStage.addPawn(new Pawn(Pawn.Color.BLUE, 0, 3, gameStage));

        Assertions.assertEquals(gameStage.getCheckPawns().size(), 2);
        Assertions.assertEquals(gameStage.getPawns().size(), 2);

        Assertions.assertEquals(gameStage.getCheckPawns().get(0).getColor(), Pawn.Color.WHITE);
        Assertions.assertEquals(gameStage.getCheckPawns().get(1).getColor(), Pawn.Color.RED);
        Assertions.assertEquals(gameStage.getPawns().get(0).getColor(), Pawn.Color.GREEN);
        Assertions.assertEquals(gameStage.getPawns().get(1).getColor(), Pawn.Color.BLUE);
    }
}
