package model;

import boardifier.model.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PawnUnitTest {
    @BeforeEach
    public void setup() {
        MasterSettings.NB_COLORS = 4;
    }

    @AfterEach
    public void end() {
        MasterSettings.NB_COLORS = 4;
        Pawn.initInputColor();
    }

    @Test
    public void testAdjustPawnColors() {
        Assertions.assertEquals(Pawn.inputColor.size(), 5);

        Pawn.adjustPawnColors();
        Assertions.assertEquals(Pawn.inputColor.size(), 4);

        MasterSettings.NB_COLORS = 2;
        Pawn.adjustPawnColors();
        Assertions.assertEquals(Pawn.inputColor.size(), 2);

        MasterSettings.NB_COLORS = 0;
        Pawn.adjustPawnColors();
        Assertions.assertEquals(Pawn.inputColor.size(), 0);

        MasterSettings.NB_COLORS = -1;
        Pawn.adjustPawnColors();
        Assertions.assertEquals(Pawn.inputColor.size(), 0);
    }

    @Test
    public void testIsInCheckBoard() {
        Pawn.inputColor.forEach((key, val) -> {
            Pawn p = new Pawn(val, 0, 0, new MasterStageModel("model.MasterStageModel", new Model()));
            Assertions.assertFalse(p.isInCheckBoard());
            Assertions.assertTrue(p.isInMasterBoard());
        });

        Pawn pRed = new Pawn(Pawn.Color.RED, 0, 0, new MasterStageModel("model.MasterStageModel", new Model()));
        Assertions.assertTrue(pRed.isInCheckBoard());
        Assertions.assertFalse(pRed.isInMasterBoard());

        Pawn pWhite = new Pawn(Pawn.Color.WHITE, 0, 0, new MasterStageModel("model.MasterStageModel", new Model()));
        Assertions.assertTrue(pWhite.isInCheckBoard());
        Assertions.assertFalse(pWhite.isInMasterBoard());
    }
}
