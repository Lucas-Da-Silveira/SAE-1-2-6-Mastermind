package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Pawn;

public class PawnLook extends ElementLook {
    private Circle circle;

    /**
     * Constructs a new instance of the PawnLook class with the given game element.
     *
     * @param element the game element associated with the look
     */
    public PawnLook(int radius, GameElement element) {
        super(element);
        Pawn pawn = (Pawn) getElement();
        circle = new Circle();
        circle.setRadius(radius);

        switch (pawn.getColor()) {
            case RED:
                circle.setFill(Color.RED);
                break;
            case BLUE:
                circle.setFill(Color.BLUE);
                break;
            case GREEN:
                circle.setFill(Color.GREEN);
                break;
            case WHITE:
                circle.setFill(Color.WHITE);
                break;
            case PURPLE:
                circle.setFill(Color.PURPLE);
                break;
            case YELLOW:
                circle.setFill(Color.YELLOW);
                break;
            case CYAN:
                circle.setFill(Color.CYAN);
                break;
            default:
                circle.setFill(Color.BLACK);
        }

        circle.setCenterX(radius);
        circle.setCenterY(radius);
        addShape(circle);

        Text text = new Text(String.valueOf(pawn.getColor().name().charAt(0)));
        text.setFont(new Font(24));
        text.setStyle("-fx-text-fill: black; -fx-stroke: white; -fx-stroke-width: 2px;");

        Bounds bt = text.getBoundsInLocal();
        text.setX(radius - bt.getWidth() / 2);
        text.setY(text.getBaselineOffset() + bt.getHeight() / 2 - 3);
        addShape(text);
    }

    @Override
    public void onSelectionChange() {
        Pawn pawn = (Pawn) getElement();
        if (pawn.isSelected()) {
            circle.setStrokeWidth(3);
            circle.setStrokeMiterLimit(10);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.valueOf("0x333333"));
        } else {
            circle.setStrokeWidth(0);
        }
    }

    @Override
    public void onChange() {
    }

    /**
     * Returns the color of the pawn.
     *
     * @return The color of the pawn.
     */
    public Pawn.Color getColor() {
        return ((Pawn) element).getColor();
    }
}
