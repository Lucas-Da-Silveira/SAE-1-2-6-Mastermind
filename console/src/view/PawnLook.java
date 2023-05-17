package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Pawn;

public class PawnLook extends ElementLook {

    /**
     * Constructs a new instance of the PawnLook class with the given game element.
     *
     * @param element  the game element associated with the look
     */
    public PawnLook(GameElement element) {
        super(element, 1, 1);
        String backgroundColor;

        switch (((Pawn)element).getColor()) {
            case RED:
                backgroundColor = ConsoleColor.RED_BACKGROUND;
                break;
            case BLUE:
                backgroundColor = ConsoleColor.BLUE_BACKGROUND;
                break;
            case GREEN:
                backgroundColor = ConsoleColor.GREEN_BACKGROUND;
                break;
            case WHITE:
                backgroundColor = ConsoleColor.WHITE_BACKGROUND;
                break;
            case PURPLE:
                backgroundColor = ConsoleColor.PURPLE_BACKGROUND;
                break;
            case YELLOW:
                backgroundColor = ConsoleColor.YELLOW_BACKGROUND;
                break;
            case CYAN:
                backgroundColor = ConsoleColor.CYAN_BACKGROUND;
                break;
            default:
                backgroundColor = ConsoleColor.BLACK_BACKGROUND;
        }

        shape[0][0] = ConsoleColor.BLACK + backgroundColor + ((Pawn) element).getColor().name().charAt(0) + ConsoleColor.RESET;
    }

    /**
     * Overrides the onLookChange() method to update the look when a change occurs.
     */
    @Override
    public void onLookChange() {
        String backgroundColor;

        switch (((Pawn) element).getColor()) {
            case RED:
                backgroundColor = ConsoleColor.RED_BACKGROUND;
                break;
            case BLUE:
                backgroundColor = ConsoleColor.BLUE_BACKGROUND;
                break;
            case GREEN:
                backgroundColor = ConsoleColor.GREEN_BACKGROUND;
                break;
            case WHITE:
                backgroundColor = ConsoleColor.WHITE_BACKGROUND;
                break;
            case PURPLE:
                backgroundColor = ConsoleColor.PURPLE_BACKGROUND;
                break;
            case YELLOW:
                backgroundColor = ConsoleColor.YELLOW_BACKGROUND;
                break;
            case CYAN:
                backgroundColor = ConsoleColor.CYAN_BACKGROUND;
                break;
            default:
                backgroundColor = ConsoleColor.BLACK_BACKGROUND;
        }

        shape[0][0] = ConsoleColor.BLACK + backgroundColor + ((Pawn) element).getColor().name().charAt(0) + ConsoleColor.RESET;
    }

    /**
     * Returns the color of the pawn.
     *
     * @return The color of the pawn.
     */
    public Pawn.Color getColor() { return ((Pawn)element).getColor(); }

}
