package simpleHotkeys.ui;

import mindustry.ui.dialogs.*;
import simpleHotkeys.*;

public class CommandBindDialog extends BaseDialog{
    public CommandBindDialog(){
        super("@dialog.commands-bind.title");
        shown(this::rebuild);
    }

    private void rebuild(){
        cont.clearChildren();
        buttons.clearChildren();

        cont.pane(t->{
            SHVars.simpleActions.show(t);
        }).fill().get().setScrollingDisabled(true,false);


        addCloseButton();
        addCloseListener();
    }
}
