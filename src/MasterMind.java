import boardifier.control.StageFactory;
import boardifier.model.Model;
import boardifier.view.View;
import controller.MasterController;
import boardifier.model.GameException;
import javafx.application.Application;
import javafx.stage.Stage;
import model.MasterSettings;
import model.Pawn;
import view.MasterRootPane;
import view.MasterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterMind extends Application {
    private static int mode;

    public static void main(String[] args) {
        try {
            mode = Integer.parseInt(args[0]);
            if ((mode < 0) || (mode > 2)) mode = 0;
        } catch (NumberFormatException ignored) {
            mode = 0;
        }

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

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
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

        // register a single stage for the game
        StageFactory.registerModelAndView("master", "model.MasterStageModel", "view.MasterStageView");
        // create the root pane, using the subclass HoleRootPane
        MasterRootPane rootPane = new MasterRootPane();
        // create the global view
        View view = new MasterView(model, stage, rootPane);
        // create the controllers
        MasterController controller = new MasterController(model, view);
        // set the name of the first stage to create when the game is started
        controller.setFirstStageName("master");
        // set the stage title
        stage.setTitle("Mastermind");
        // show the JavaFx main stage
        stage.show();
    }
}
