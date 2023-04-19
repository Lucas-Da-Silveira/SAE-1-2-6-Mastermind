package controller;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

public class MasterDecider extends Decider {
    public MasterDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {}
}
