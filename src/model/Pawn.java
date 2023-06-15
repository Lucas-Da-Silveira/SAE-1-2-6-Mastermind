package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.animation.AnimationStep;
import boardifier.view.GridGeometry;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a pawn in the game.
 * Extends the GameElement class.
 */
public class Pawn extends GameElement {
    /**
     * Enum representing the color of the pawn.
     * The colors YELLOW, BLUE, GREEN, PURPLE, and CYAN represent pawns in the main board.
     * The colors RED and WHITE represent pawns in the check board.
     */
    public enum Color {
        // pawn in the main board
        YELLOW, BLUE, GREEN, PURPLE, CYAN,
        // pawn in the check board
        RED, WHITE
    }

    /**
     * Map that maps characters to pawn colors for user input.
     */
    public static Map<Character, Color> inputColor;

    static {
        initInputColor();
    }

    public static void initInputColor() {
        inputColor = new LinkedHashMap<>();
        for (Color c : Color.values()) {
            if (c == Color.RED || c == Color.WHITE) continue;
            inputColor.put(c.name().charAt(0), c);
        }
    }

    private final Color color;
    private final int row;
    private final int col;

    /**
     * Constructor for creating a Pawn object.
     *
     * @param color          The color of the pawn.
     * @param row            The row position of the pawn.
     * @param col            The column position of the pawn.
     * @param gameStageModel The game stage model associated with the pawn.
     */
    public Pawn(Color color, int row, int col, GameStageModel gameStageModel) {
        super(gameStageModel);
        ElementTypes.register("pawn", 50);
        type = ElementTypes.getType("pawn");

        this.color = color;
        this.row = row;
        this.col = col;
    }

    /**
     * Adjusts the pawn colors based on the maximum number of colors allowed.
     * Any excess colors beyond the maximum limit will be removed from the inputColor map.
     */
    public static void adjustPawnColors() {
        int counter = 0;
        for (Color c : Color.values()) {
            if (c == Color.RED || c == Color.WHITE) continue;
            if (counter >= MasterSettings.NB_COLORS) {
                inputColor.remove(c.name().charAt(0));
            }

            counter++;
        }
    }

    @Override
    public void update(double width, double height, GridGeometry gridGeometry) {
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step != null) {
                setLocation(step.getInt(0), step.getInt(1));
            } else {
                animation = null;
            }
        }
    }

    /**
     * Returns the color of the pawn.
     *
     * @return The color of the pawn.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the row position of the pawn.
     *
     * @return The row position of the pawn.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column position of the pawn.
     *
     * @return The column position of the pawn.
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Checks if the pawn is in the master board.
     *
     * @return {@code true} if the pawn is in the master board, {@code false} otherwise.
     */
    public boolean isInMasterBoard() {
        return !this.isInCheckBoard();
    }

    /**
     * Checks if the pawn is in the check board.
     *
     * @return {@code true} if the pawn is in the check board, {@code false} otherwise.
     */
    public boolean isInCheckBoard() {
        return (this.color == Color.WHITE || this.color == Color.RED);
    }
}
