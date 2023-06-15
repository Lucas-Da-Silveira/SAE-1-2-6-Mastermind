package model;

import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static model.MasterStageModel.PHASE_CODE;
import static model.MasterStageModel.PHASE_GAME;


public class MasterModelUnitTest {
    protected MasterStageModel gameStage;
    protected Model model;


    @BeforeEach
    public void setup() {
        model = new Model();
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", model));
    }

    @Test
    public void testPutCallback() {
        Mockito.doReturn("player1").when(gameStage).getCurrentPlayerName();
        new MasterStageFactory(gameStage).setup();

        gameStage.setPhase(PHASE_CODE);

        gameStage.putCallback(new Pawn(Pawn.Color.PURPLE, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.putCallback(new Pawn(Pawn.Color.YELLOW, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.putCallback(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.putCallback(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);

        Assertions.assertEquals("PYBG", gameStage.getSecretCombination());

        gameStage.setPhase(PHASE_GAME);

        gameStage.putCallback(new Pawn(Pawn.Color.WHITE, 0, 0, gameStage), gameStage.getCheckBoard(), 0, 0);

        Assertions.assertEquals(1, gameStage.getCheckPawns().size());

        /* win */

        // manually adding pawn in the list
        // good code
        gameStage.addPawn(new Pawn(Pawn.Color.PURPLE, 0, 0, gameStage));
        gameStage.putCallback(new Pawn(Pawn.Color.PURPLE, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.addPawn(new Pawn(Pawn.Color.YELLOW, 0, 0, gameStage));
        gameStage.putCallback(new Pawn(Pawn.Color.YELLOW, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.addPawn(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage));
        gameStage.putCallback(new Pawn(Pawn.Color.BLUE, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);
        gameStage.addPawn(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage));
        gameStage.putCallback(new Pawn(Pawn.Color.GREEN, 0, 0, gameStage), gameStage.getCodeBoard(), 0, 0);

        Assertions.assertEquals(4, gameStage.getNbMatch());
        Assertions.assertEquals(0, gameStage.getNbCommon());
        Assertions.assertEquals(MasterStageModel.GAME_STATE_WIN, gameStage.getGameState());
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
        Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> {
            gameStage.numberCorrectPawns(new StringBuilder("XXX"), new StringBuilder("XX"));
        });
    }
}
