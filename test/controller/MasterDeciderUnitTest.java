package controller;

import boardifier.model.Model;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.when;

public class MasterDeciderUnitTest {
    protected MasterDecider decider;

    Model model;
    MasterStageModel stageModel;
    MasterController controller;

    @BeforeEach
    public void setup() {
        model = Mockito.spy(new Model());
        stageModel = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
        when(model.getGameStage()).thenReturn(stageModel);
        controller = Mockito.mock(MasterController.class);
        decider = Mockito.spy(new MasterDecider(model, controller));
    }

    @Test
    public void testFirstIAStrategy() {
        List<String> possibleInput = new ArrayList<>();
        possibleInput.add("YYBG");
        possibleInput.add("PGYB");

        when(stageModel.getRowsCompleted()).thenReturn(1);
        when(stageModel.getNbMatch()).thenReturn(1);
        when(stageModel.getNbCommon()).thenReturn(2);

        String result = decider.firstIAStrategy(stageModel);

        Assertions.assertEquals("BBBB", result);
    }

    @Test
    public void testSecondIAStrategy() {
        List<String> possibleInput = new ArrayList<>();
        possibleInput.add("ABC");
        possibleInput.add("DEF");

        when(stageModel.getRowsCompleted()).thenReturn(1);
        when(stageModel.getNbMatch()).thenReturn(1);
        when(stageModel.getNbCommon()).thenReturn(2);

        stageModel.possibleAnswer = Arrays.asList("YBBB", "PPGB");

        String result = decider.secondIAStrategy(stageModel);

        Assertions.assertTrue(result.equals("PPGB") || result.equals("YBBB"));
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
        when(stageModel.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList('C', 'C', null, null, 'C')));
        Assertions.assertEquals(decider.numberCorrectColor(stageModel), 3);

        when(stageModel.getAnswer()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(decider.numberCorrectColor(stageModel), 0);

        when(stageModel.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList('C', 'C', 'C')));
        Assertions.assertEquals(decider.numberCorrectColor(stageModel), 3);

        when(stageModel.getAnswer()).thenReturn(new ArrayList<>(Arrays.asList(null, null, null)));
        Assertions.assertEquals(decider.numberCorrectColor(stageModel), 0);
    }

    @Test
    public void testGetPossibleInputChar() {
        Map<Character, Pawn.Color> inputColor = new LinkedHashMap<>();
        inputColor.put('Y', Pawn.Color.YELLOW);

        Assertions.assertEquals(decider.getPossibleInputChar(inputColor), new ArrayList<>(List.of('Y')));

        inputColor.put('G', Pawn.Color.GREEN);
        inputColor.put('B', Pawn.Color.BLUE);

        Assertions.assertEquals(decider.getPossibleInputChar(inputColor), new ArrayList<>(List.of('Y', 'G', 'B')));
    }

    private static int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }
}
