package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class Pawn extends GameElement {
    public enum Color {
        // pawn in the main board
        YELLOW, BLUE, GREEN, PURPLE, CYAN,
        // pawn in the check board
        RED, WHITE
    }

    public static final Map<Character, Pawn.Color> inputColor;
    static {
        inputColor = new LinkedHashMap<>();
        for (Color c : Color.values()) {
            if (c == Color.RED || c == Color.WHITE) continue;
            inputColor.put(c.name().charAt(0), c);
        }
    }

    private Color color;
    private int row;
    private int col;

    public Pawn(Color color, int row, int col, GameStageModel gameStageModel) {
        super(gameStageModel);
        ElementTypes.register("pawn",50);
        type = ElementTypes.getType("pawn");

        this.color = color;
        this.row = row;
        this.col = col;
    }

    public Color getColor() {
        return this.color;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public boolean isInMasterBoard() {
        return !this.isInCheckBoard();
    }

    public boolean isInCheckBoard() {
        return (this.color == Color.WHITE || this.color == Color.RED);
    }
}
