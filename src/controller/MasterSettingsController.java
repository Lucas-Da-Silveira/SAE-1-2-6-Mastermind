package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.MasterSettings;
import view.MasterRootPane;

public class MasterSettingsController {
    MasterRootPane rootPane;

    public MasterSettingsController(MasterRootPane _rootPane) {
        rootPane = _rootPane;
    }

    public void addEvents() {
        rootPane.getBtn().setOnAction(actionEvent -> {
            if (actionEvent.getSource() != rootPane.getBtn()) return;

            MasterSettings.NB_ROWS = (int) rootPane.getRowsNumber().getValue();
            MasterSettings.NB_COLS = (int) rootPane.getColsNumber().getValue();
            MasterSettings.NB_COLORS = (int) rootPane.getColorsNumber().getValue();
            MasterSettings.AI_MODE = rootPane.getAIComboBox().getSelectionModel().getSelectedIndex();
        });
    }
}
