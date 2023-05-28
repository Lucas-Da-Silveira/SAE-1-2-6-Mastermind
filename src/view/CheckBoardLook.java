package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.MasterSettings;

public class CheckBoardLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[][] cells;

    public CheckBoardLook(int size, GameElement element) {
        super(size, size, (size - 20) / 3, (size - 20) / 3, 10, "0X000000", element);
        cells = new Rectangle[MasterSettings.NB_ROWS][MasterSettings.NB_COLS];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Color c;
                if ((i + j) % 2 == 0) {
                    c = Color.BEIGE;
                } else {
                    c = Color.DARKGRAY;
                }
                cells[i][j] = new Rectangle(cellWidth, cellHeight, c);
                cells[i][j].setX(j * cellWidth + borderWidth);
                cells[i][j].setY(i * cellHeight + borderWidth);
                addShape(cells[i][j]);
            }
        }
    }

    @Override
    public void onChange() {
    }
}
