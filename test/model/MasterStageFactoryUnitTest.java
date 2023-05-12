package model;

import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterStageFactoryUnitTest {
    MasterStageModel gameStage;
    MasterStageFactory factory;

    @BeforeEach
    public void setup() {
        gameStage = new MasterStageModel("model.MasterStageModel", new Model());
        factory = new MasterStageFactory(gameStage);
    }

    @Test
    public void testSetup() {
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
    }
}
