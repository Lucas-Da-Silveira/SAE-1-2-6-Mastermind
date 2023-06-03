package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.MasterSettings;

public class PawnPotLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[] cells;

    public PawnPotLook(int width, int height, GameElement element) {
        super(width, height, (width - 20)/MasterSettings.NB_COLORS, MasterSettings.CELL_SIZE * 2 - 20, 10, "0X000000", element);
        cells = new Rectangle[MasterSettings.NB_COLORS];

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Rectangle(cellWidth, cellHeight, Color.WHITE);
            cells[i].setStrokeWidth(3);
            cells[i].setStrokeMiterLimit(10);
            cells[i].setStrokeType(StrokeType.CENTERED);
            cells[i].setStroke(Color.valueOf("0x333333"));
            cells[i].setX(i * cellWidth + borderWidth);
            cells[i].setY(borderWidth);
            addShape(cells[i]);
        }
    }

    @Override
    public void onChange() {
    }
}
