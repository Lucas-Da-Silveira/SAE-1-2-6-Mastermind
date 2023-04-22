package controller;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import model.MasterStageModel;
import model.Pawn;

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
                    if (line.length() == ((MasterStageModel)(model.getGameStage())).getBoard().getNbCols()) {
                        ok = analyseAndPlay(line.toUpperCase());
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch(IOException e) {}
            }
        }
    }

    public boolean analyseAndPlay(String line) {
        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();
        for (int i = 0; i < line.length(); i++) {
            char l = line.charAt(i);
            if (Pawn.inputColor.get(l) == null) return false;
        }
        if (gameStage.getRowsCompleted() >= gameStage.getBoard().getNbRows()) return false;

        ActionList actions = new ActionList(true);
        for (int i = 0; i < line.length(); i++) {
            Pawn.Color color = Pawn.inputColor.get(line.charAt(i));
            int row = gameStage.getRowsCompleted();
            GameAction move = new MoveAction(model, new Pawn(color, row, i, gameStage), "masterboard", row, i);
            actions.addSingleAction(move);
        }

        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }
}
