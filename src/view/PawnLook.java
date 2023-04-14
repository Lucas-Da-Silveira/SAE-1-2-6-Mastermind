package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import model.Pawn;

public class PawnLook extends ElementLook {
    public PawnLook(GameElement element) {
        super(element, 1, 1);
    }

    @Override
    public void onLookChange() {}
}
