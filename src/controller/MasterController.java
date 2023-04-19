package controller;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MasterController extends Controller {
    BufferedReader consoleIn;
    boolean firstPlayer;

    public MasterController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
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

    public void nextPlayer() {
        if (!firstPlayer) {
            model.setNextPlayer();
        } else {
            firstPlayer = false;
        }

        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MasterDecider decider = new MasterDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch(IOException e) {}
            }
        }
    }

    public boolean analyseAndPlay(String line) {
        // jouer l'input de l'utilisateur
    }
}
