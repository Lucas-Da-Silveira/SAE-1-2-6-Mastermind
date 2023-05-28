import boardifier.control.StageFactory;
import boardifier.model.Model;
import boardifier.view.View;
import controller.MasterController;
import boardifier.model.GameException;
import model.MasterSettings;
import model.Pawn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterMind {
    public static void main(String[] args) {
        int mode = 0;

        try {
            mode = Integer.parseInt(args[0]);
            if ((mode < 0) || (mode > 2)) mode = 0;
        } catch (NumberFormatException ignored) {}

        String pattern = "--(colors|rows|cols|aimode)=(\\d+)";
        for (String arg : args) {
            if (arg.matches(pattern)) {
                Matcher matcher = Pattern.compile(pattern).matcher(arg);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    int value = Integer.parseInt(matcher.group(2));
                    if (name.equals("colors") && value >= 1) {
                        MasterSettings.NB_COLORS = Math.min(value, (Pawn.Color.values().length - 2));
                    } else if (name.equals("rows") && value >= 1) {
                        MasterSettings.NB_ROWS = value;
                    } else if (name.equals("cols") && value >= 1) {
                        MasterSettings.NB_COLS = value;
                    } else if (name.equals("aimode")) {
                        MasterSettings.AI_MODE = value;
                    }
                }
            }
        }

        Pawn.adjustPawnColors();

        Model model = new Model();
        if (mode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        } else if (mode == 1) {
            model.addComputerPlayer("computer");
            model.addHumanPlayer("player");
        } else if (mode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }

        StageFactory.registerModelAndView("master", "model.MasterStageModel", "view.MasterStageView");
        View masterView = new View(model);
        MasterController control = new MasterController(model, masterView);
        control.setFirstStageName("master");
        try {
            control.startGame();
            control.stageLoop();
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort.");
        }
    }
}