package controller;

import boardifier.model.Model;
import boardifier.view.View;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

public class MasterDeciderUnitTest extends ControllerUnitTest {
    protected MasterDecider decider;

    @BeforeEach
    public void setup() {
        super.init();
        decider = Mockito.spy(new MasterDecider(model, controller));
    }

    @Test
    public void testGetPermutation() {
        // getPermutation's complexity : O(n!)
        for (int i = 1; i <= 8; i++) {
            Assertions.assertEquals(decider.getPermutation("A".repeat(i)).size(), factorial(i));
        }
    }

    @Test
    public void testNumberCorrectColor() {
        Mockito.when(gameStage.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList('C', 'C', null, null, 'C')));
        Assertions.assertEquals(decider.numberCorrectColor(gameStage), 3);

        Mockito.when(gameStage.getAnswer()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(decider.numberCorrectColor(gameStage), 0);

        Mockito.when(gameStage.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList('C', 'C', 'C')));
        Assertions.assertEquals(decider.numberCorrectColor(gameStage), 3);

        Mockito.when(gameStage.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList(null, null, null)));
        Assertions.assertEquals(decider.numberCorrectColor(gameStage), 0);
    }

    @Test
    public void testGetPossibleInputChar() {
        Map<Character, Pawn.Color>  inputColor = new LinkedHashMap<>();
        inputColor.put('Y', Pawn.Color.YELLOW);

        Assertions.assertEquals(decider.getPossibleInputChar(inputColor), new ArrayList<>(List.of('Y')));

        inputColor.put('G', Pawn.Color.GREEN);
        inputColor.put('B', Pawn.Color.BLUE);

        Assertions.assertEquals(decider.getPossibleInputChar(inputColor), new ArrayList<>(List.of('Y', 'G', 'B')));
    }
}
