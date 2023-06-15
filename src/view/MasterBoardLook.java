package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.MasterBoard;
import model.MasterSettings;

public class MasterBoardLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[][] cells;

    public MasterBoardLook(int size, GameElement element) {
        super((size / MasterSettings.NB_ROWS) * MasterSettings.NB_COLS, size, ((size / MasterSettings.NB_ROWS) * MasterSettings.NB_COLS - 20) / MasterSettings.NB_COLS, (size - 20) / MasterSettings.NB_ROWS, 10, "0X000000", element);
        cells = new Rectangle[MasterSettings.NB_ROWS][MasterSettings.NB_COLS];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Color c;
                if ((i) % 2 == 0) {
                    c = Color.valueOf("FFC26F");
                } else {
                    c = Color.valueOf("0xC38154");
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
        MasterBoard board = (MasterBoard) element;
        boolean[][] reach = board.getReachableCells();
        for (int i = 0; i < MasterSettings.NB_ROWS; i++) {
            for (int j = 0; j < MasterSettings.NB_COLS; j++) {
                if (i < reach.length && reach[i][j]) {
                    cells[i][j].setStrokeWidth(3);
                    cells[i][j].setStrokeMiterLimit(10);
                    cells[i][j].setStrokeType(StrokeType.CENTERED);
                    cells[i][j].setStroke(Color.valueOf("0x333333"));
                } else {
                    cells[i][j].setStrokeWidth(0);
                }
            }
        }
    }
}
