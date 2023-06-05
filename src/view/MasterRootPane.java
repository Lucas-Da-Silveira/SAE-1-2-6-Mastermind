package view;

import boardifier.view.RootPane;
import controller.MasterSettingsController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.MasterSettings;

public class MasterRootPane extends RootPane {
    private Spinner rowsNumber;
    private Spinner colsNumber;
    private Spinner colorsNumber;
    private ComboBox AIComboBox;
    private Button btn;

    public MasterRootPane() {
        super();
    }

    @Override
    public void createDefaultGroup() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Text text = new Text("Playing to Mastermind | settings");
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(text);

        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setVgap(10);

        rowsNumber = new Spinner(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, MasterSettings.NB_ROWS));
        rowsNumber.setPrefWidth(70);
        fields.add(new Label("Number of rows:"), 0, 0);
        fields.add(rowsNumber, 1, 0);

        colsNumber = new Spinner(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, MasterSettings.NB_COLS));
        colsNumber.setPrefWidth(70);
        fields.add(new Label("Number of cols:"), 0, 1);
        fields.add(colsNumber, 1, 1);

        colorsNumber = new Spinner(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, MasterSettings.NB_COLORS));
        colorsNumber.setPrefWidth(70);
        fields.add(new Label("Number of colors:"), 0, 2);
        fields.add(colorsNumber, 1, 2);

        AIComboBox = new ComboBox(FXCollections.observableArrayList("First AI", "Second AI", "AI plays randomly"));
        AIComboBox.getSelectionModel().select(MasterSettings.AI_MODE);
        fields.add(new Label("AI choice:"), 0, 3);
        fields.add(AIComboBox, 1, 3);

        btn = new Button("Validate");
        btn.setPrefWidth(80);
        btn.setPrefHeight(40);

        root.getChildren().addAll(fields, btn);

        MasterSettingsController settingsController = new MasterSettingsController(this);
        settingsController.addEvents();

        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(root);
    }

    public Spinner getRowsNumber() {
        return rowsNumber;
    }

    public Spinner getColsNumber() {
        return colsNumber;
    }

    public Spinner getColorsNumber() {
        return colorsNumber;
    }

    public ComboBox getAIComboBox() {
        return AIComboBox;
    }

    public Button getBtn() {
        return btn;
    }
}
