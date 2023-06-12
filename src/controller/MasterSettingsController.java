package controller;

import boardifier.model.Model;
import model.MasterSettings;
import view.MasterRootPane;


public class MasterSettingsController {
    MasterRootPane rootPane;
    Model model;

    public MasterSettingsController(MasterRootPane _rootPane, Model _model) {
        rootPane = _rootPane;
        model = _model;
    }

    public void addEvents() {
        rootPane.getBtn().setOnAction(actionEvent -> {
            if (actionEvent.getSource() != rootPane.getBtn()) return;

            MasterSettings.NB_ROWS = (int) rootPane.getRowsNumber().getValue();

            MasterSettings.NB_COLS = (int) rootPane.getColsNumber().getValue();

            MasterSettings.NB_COLORS = (int) rootPane.getColorsNumber().getValue();

            MasterSettings.MODE = rootPane.getModeComboBox().getSelectionModel().getSelectedIndex();
            model.getPlayers().clear();
            if (MasterSettings.MODE == 0) {
                model.addHumanPlayer("player1");
                model.addHumanPlayer("player2");
            } else if (MasterSettings.MODE == 1) {
                model.addComputerPlayer("computer");
                model.addHumanPlayer("player");
            } else if (MasterSettings.MODE == 2) {
                model.addComputerPlayer("computer1");
                model.addComputerPlayer("computer2");
            }

            MasterSettings.AI_MODE = rootPane.getAIComboBox().getSelectionModel().getSelectedIndex();
        });

        rootPane.getModeComboBox().setOnAction(event -> {
            if (event.getSource() != rootPane.getModeComboBox()) return;
            // if mode selected is doesn't involve the AI, then don't show the AI choice field
            if (rootPane.getModeComboBox().getSelectionModel().getSelectedIndex() != 2) {
                rootPane.getAIComboBox().setVisible(false);
            } else {
                rootPane.getAIComboBox().setVisible(true);
            }
        });
    }
}
