import boardifier.control.StageFactory;
import boardifier.model.Model;
import boardifier.view.View;
import controller.MasterController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.MasterSettings;
import model.Pawn;
import view.MasterRootPane;
import view.MasterView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.MasterSettings.MODE;

public class MasterMind extends Application {
    public static void main(String[] args) {
        try {
            MODE = Integer.parseInt(args[0]);
            if ((MODE < 0) || (MODE > 2)) MODE = 0;
        } catch (NumberFormatException ignored) {
            MODE = 0;
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
        if (MODE == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        } else if (MODE == 1) {
            model.addComputerPlayer("computer");
            model.addHumanPlayer("player");
        } else if (MODE == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }

        // register a single stage for the game
        StageFactory.registerModelAndView("master", "model.MasterStageModel", "view.MasterStageView");
        // create the root pane, using the subclass HoleRootPane
        MasterRootPane rootPane = new MasterRootPane(model);
        // create the global view
        View view = new MasterView(model, stage, rootPane);
        // create the controllers
        MasterController controller = new MasterController(model, view);
        rootPane.addController(controller);
        // set the name of the first stage to create when the game is started
        controller.setFirstStageName("master");
        // set the stage title
        stage.setTitle("Mastermind");
        // show the JavaFx main stage
        stage.show();

        view.resetView();
    }
}
