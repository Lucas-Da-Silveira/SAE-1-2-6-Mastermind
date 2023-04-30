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

import model.MasterSettings;
import model.MasterStageModel;
import model.Pawn;

public class MasterController extends Controller {
    BufferedReader consoleIn;

    public MasterController(Model model, View view) {
        super(model, view);
    }

    @Override
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));

        MasterStageModel gameStage = (MasterStageModel) model.getGameStage();

        gameStage.setAIMode(MasterSettings.AI_MODE);
        new Thread(() -> gameStage.setupCallbacks(this)).start();

        String line = "";
        if (model.getCurrentPlayer().getType() == Player.COMPUTER) {
            line = MasterDecider.generateRandomLine(gameStage);
            System.out.println(model.getCurrentPlayer().getName() + ", chose the colors");
        } else {
            boolean ok = false;
            while (!ok) {
                try {
                    System.out.print(model.getCurrentPlayer().getName() + ", choose the colors : ");
                    line = consoleIn.readLine().toUpperCase();
                    ok = verifyLine(line, gameStage);
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException ignored) {}
            }
        }

        gameStage.setSecretCombination(line);

        model.setNextPlayer();

        update();
        while (!model.isEndStage()) {
            nextPlayer(new MasterDecider(model, this), consoleIn);
            update();
        }
        stopStage();
        endGame();
    }

    public void nextPlayer(MasterDecider decider, BufferedReader _consoleIn) {
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = _consoleIn.readLine().toUpperCase();
                    ok = analyseAndPlay(line, (MasterStageModel) model.getGameStage(), model);
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException ignored) {}
            }
        }
    }

    public boolean analyseAndPlay(String line, MasterStageModel gameStage, Model _model) {
        if (!verifyLine(line, gameStage)) return false;

        ActionList actions = createActions(line, gameStage, _model);

        ActionPlayer play = new ActionPlayer(_model, this, actions);
        play.start();

        return true;
    }

    public static ActionList createActions(String line, MasterStageModel gameStage, Model model) {
        ActionList actions = new ActionList(true);

        for (int i = 0; i < line.length(); i++) {
            Pawn.Color color = Pawn.inputColor.get(line.charAt(i));
            int row = gameStage.getRowsCompleted();

            Pawn p = new Pawn(color, row, i, gameStage);

            gameStage.getBoard().putElement(p, row, i);
            GameAction move = new MoveAction(model, p, "masterboard", row, i);

            actions.addSingleAction(move);
        }

        return actions;
    }

    public boolean verifyLine(String line, MasterStageModel gameStage) {
        if (line.length() != gameStage.getBoard().getNbCols()) return false;

        for (int i = 0; i < line.length(); i++) {
            char l = line.charAt(i);
            if (Pawn.inputColor.get(l) == null) return false;
        }

        return true;
    }
}
