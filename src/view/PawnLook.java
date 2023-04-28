package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Pawn;

public class PawnLook extends ElementLook {
    public PawnLook(GameElement element) {
        super(element, 1, 1);
        Pawn pawn = (Pawn)element;
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
            default:
                backgroundColor = ConsoleColor.BLACK_BACKGROUND;
        }
        shape[0][0] = ConsoleColor.BLACK + backgroundColor + ((Pawn) element).getColor().name().charAt(0) + ConsoleColor.RESET;
    }

    @Override
    public void onLookChange() {}

    public Pawn.Color getColor() { return ((Pawn)element).getColor(); }

}
