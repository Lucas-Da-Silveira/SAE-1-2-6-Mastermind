package model;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import controller.MasterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
        model = Mockito.spy(new Model());
        controller = new MasterController(model, new View(model));

        // used Mockito's spy method so that other functions in the class are initialized
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
        board = Mockito.spy(new MasterBoard("masterboard", 0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, gameStage));
        Mockito.when(gameStage.getBoard()).thenReturn(board);

        checkBoard = new MasterBoard("checkboard", 0, 0, MasterSettings.NB_ROWS, MasterSettings.NB_COLS, gameStage);

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

    @Test
    public void testSetupCallbacksWin() {
        String secret = "GGGG";

        Model m = Mockito.mock(Model.class);

        gameStage.setBoard(gameStage.getBoard());

        Mockito.when(m.isEndStage()).thenReturn(true);
        Mockito.when(m.getCurrentPlayer()).thenReturn(Player.createComputerPlayer("computer1"));
        Mockito.when(m.getGameStage()).thenReturn(gameStage);
        Mockito.when(gameStage.getGrids()).thenReturn(new ArrayList<>(Arrays.asList(board, checkBoard)));
        Mockito.when(model.getGrids()).thenReturn(new ArrayList<>(Arrays.asList(board, checkBoard)));

        MasterController c = Mockito.spy(new MasterController(m, new View(m)));

        BufferedReader consoleIn = Mockito.mock(BufferedReader.class);
        try {
            Mockito.when(consoleIn.readLine()).thenReturn(secret, secret);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doNothing().when(c).update();
        doNothing().when(c).setElementLocationToCellCenter(any());
        doNothing().when(c).endGame();
        doNothing().when(c).stopStage();

        c.stageLoop(consoleIn);

        Mockito.verify(gameStage, times(1)).setupCallbacks(any());

        gameStage.putInGrid(new Pawn(Pawn.Color.RED, 0, 0, gameStage), gameStage.getCheckBoard(), 0, 0);
        gameStage.putInGrid(new Pawn(Pawn.Color.RED, 0, 0, gameStage), gameStage.getCheckBoard(), 0, 1);
        gameStage.putInGrid(new Pawn(Pawn.Color.RED, 0, 0, gameStage), gameStage.getCheckBoard(), 0, 2);
        gameStage.putInGrid(new Pawn(Pawn.Color.RED, 0, 0, gameStage), gameStage.getCheckBoard(), 0, 3);
        Assertions.assertEquals(gameStage.getCheckPawns().size(), 4);
        Assertions.assertEquals(gameStage.getCheckPawns().get(0).getColor(), Pawn.Color.RED);

        Mockito.when(gameStage.getSecretCombination()).thenReturn(secret);
        gameStage.putInGrid(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getBoard(), 0, 0);
        gameStage.putInGrid(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getBoard(), 0, 1);
        gameStage.putInGrid(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getBoard(), 0, 2);
        gameStage.putInGrid(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getBoard(), 0, 3);

        Mockito.verify(gameStage, times(1)).computePartyResult(eq(true));

        for (Pawn p : gameStage.getCheckPawns()) {
            Assertions.assertEquals(p.getColor(), Pawn.Color.RED);
        }
        for (Pawn p : gameStage.getPawns()) {
            Assertions.assertEquals(p.getColor(), Pawn.Color.GREEN);
        }
    }

    @Test
    public void testSetupCallbacksLoose() {
        String secret = "GGGY";

        Model m = Mockito.mock(Model.class);

        gameStage.setBoard(gameStage.getBoard());

        Mockito.when(m.isEndStage()).thenReturn(true);
        Mockito.when(m.getCurrentPlayer()).thenReturn(Player.createComputerPlayer("computer1"));
        Mockito.when(m.getGameStage()).thenReturn(gameStage);
        Mockito.when(gameStage.getGrids()).thenReturn(new ArrayList<>(Arrays.asList(board, checkBoard)));
        Mockito.when(model.getGrids()).thenReturn(new ArrayList<>(Arrays.asList(board, checkBoard)));

        MasterController c = Mockito.spy(new MasterController(m, new View(m)));

        BufferedReader consoleIn = Mockito.mock(BufferedReader.class);
        try {
            Mockito.when(consoleIn.readLine()).thenReturn(secret, secret);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doNothing().when(c).update();
        doNothing().when(c).setElementLocationToCellCenter(any());
        doNothing().when(c).endGame();
        doNothing().when(c).stopStage();

        c.stageLoop(consoleIn);

        Mockito.verify(gameStage, times(1)).setupCallbacks(any());

        for (int i = 0; i < 11; i++) {
            // we fill the game with only wrong try
            gameStage.putInGrid(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getBoard(), 0, 0);
            gameStage.putInGrid(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getBoard(), 0, 1);
            gameStage.putInGrid(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getBoard(), 0, 2);
            gameStage.putInGrid(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getBoard(), 0, 3);
        }

        Mockito.when(gameStage.getSecretCombination()).thenReturn(secret);
        gameStage.putInGrid(new Pawn(Pawn.Color.YELLOW, 0, 0, gameStage), gameStage.getBoard(), 0, 0);
        gameStage.putInGrid(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getBoard(), 0, 1);
        gameStage.putInGrid(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getBoard(), 0, 2);
        gameStage.putInGrid(new Pawn(Pawn.Color.PURPLE, 0, 0, gameStage), gameStage.getBoard(), 0, 3);

        Mockito.verify(gameStage, times(1)).computePartyResult(eq(false));

        Assertions.assertTrue(gameStage.getCheckPawns().get(0).getColor() == Pawn.Color.RED || gameStage.getCheckPawns().get(0).getColor() == Pawn.Color.WHITE);
        Assertions.assertTrue(gameStage.getCheckPawns().get(1).getColor() == Pawn.Color.RED || gameStage.getCheckPawns().get(1).getColor() == Pawn.Color.WHITE);

        for (int i = 0; i < board.getNbCols()*11; i++) {
            Assertions.assertEquals(gameStage.getPawns().get(i).getColor(), Pawn.Color.BLUE);
        }
    }
}
