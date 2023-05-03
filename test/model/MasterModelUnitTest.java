package model;

import boardifier.control.Controller;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;
import controller.MasterController;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class MasterModelUnitTest {
    Controller controller;
    MasterStageModel gameStage;
    MasterBoard board;
    MasterBoard checkBoard;
    Model model;


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
    public void testPawn() {
        Pawn pawn = new Pawn(Pawn.Color.BLUE, 0, 0, gameStage);
        Assertions.assertEquals(pawn.getColor(), Pawn.Color.BLUE);
        Assertions.assertEquals(pawn.getRow(), 0);
        Assertions.assertEquals(pawn.getCol(), 0);
    }

    @Test
    public void testCorrectPawn() {
        StringBuilder code = new StringBuilder("YGPB");
        StringBuilder answer = new StringBuilder("YGPB");
        Assertions.assertEquals("YGPB", code.toString());
        Assertions.assertEquals("YGPB", answer.toString());
        Assertions.assertEquals(4, code.length());
        Assertions.assertEquals(4, answer.length());

    }
}
