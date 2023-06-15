package controller;

import boardifier.model.Model;
import javafx.scene.input.MouseEvent;
import model.MasterStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import view.MasterRootPane;
import view.MasterView;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

public class MasterControllerMouseUnitTest {
    Model model;
    MasterStageModel stageModel;

    @BeforeEach
    public void setup() {
        model = Mockito.spy(new Model());
        stageModel = Mockito.mock(MasterStageModel.class);
        Mockito.when(model.getGameStage()).thenReturn(stageModel);
    }

    @Test
    public void testHandle() {
        doReturn(true).when(model).isCaptureMouseEvent();
        MasterController controller = Mockito.mock(MasterController.class);

        Mockito.when(controller.elementsAt(any())).thenReturn(List.of(new Pawn(Pawn.Color.BLUE, 0, 0, stageModel)));

        Mockito.when(stageModel.getState()).thenReturn(MasterStageModel.STATE_SELECTPAWN);

        MasterView view = Mockito.mock(MasterView.class);
        Mockito.when(view.getRootPane()).thenReturn(Mockito.mock(MasterRootPane.class));

        MasterControllerMouse controllerMouse = new MasterControllerMouse(model, view, controller);
        MouseEvent event = Mockito.mock(MouseEvent.class);

        controllerMouse.handle(event);

        Mockito.verify(stageModel, times(1)).getState();
    }
}
