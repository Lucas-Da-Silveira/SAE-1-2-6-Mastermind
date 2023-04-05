package controller;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MasterController extends Controller {
    BufferedReader consoleIn;
    boolean firstPlayer;

    public MasterController(Model model, View view) {
        super(model, view);
    }

    @Override
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        update();
        while (!model.isEndStage()) {
            nextPlayer();
            update();
        }
        stopStage();
        endGame();
    }
}
