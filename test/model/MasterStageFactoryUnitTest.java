package model;

import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MasterStageFactoryUnitTest {
    MasterStageModel gameStage;
    MasterStageFactory factory;

    @BeforeEach
    public void setup() {
        gameStage = Mockito.spy(new MasterStageModel("model.MasterStageModel", new Model()));
        factory = new MasterStageFactory(gameStage);
    }

    @Test
    public void testSetup() {
        Mockito.doReturn("player1").when(gameStage).getCurrentPlayerName();

        Assertions.assertNull(gameStage.getBoard());
        Assertions.assertNull(gameStage.getCheckBoard());
        Assertions.assertNull(gameStage.getColorsBoard());
        Assertions.assertNull(gameStage.getColorPot());
        Assertions.assertEquals(gameStage.getColorPawns().size(), 0);
        Assertions.assertEquals(gameStage.getColorPotLists().size(), 0);

        factory.setup();

        Assertions.assertNotNull(gameStage.getBoard());
        Assertions.assertNotNull(gameStage.getCheckBoard());
        Assertions.assertNotNull(gameStage.getColorsBoard());
        Assertions.assertNotNull(gameStage.getColorPot());
        Assertions.assertEquals(gameStage.getColorPawns().size(), Pawn.Color.values().length);
        Assertions.assertEquals(gameStage.getColorPotLists().size(), Pawn.Color.values().length);

        Assertions.assertFalse(gameStage.getColorPot().isVisible());
        Assertions.assertFalse(gameStage.getBoard().isVisible());
        Assertions.assertFalse(gameStage.getCheckBoard().isVisible());

        Assertions.assertEquals(Pawn.Color.values().length, gameStage.getColorPotLists().size());
        Assertions.assertEquals(Pawn.Color.values().length, gameStage.getColorPawns().size());
    }
}
