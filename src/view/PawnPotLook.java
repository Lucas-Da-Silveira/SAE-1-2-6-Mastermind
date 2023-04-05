package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;

public class PawnPotLook extends GridLook {
    public PawnPotLook(int cellWidth, int cellHeight, GridElement gridElement) {
        super(cellWidth, cellHeight, gridElement, -1, false);
    }

    @Override
    protected void createShape() {}
}
