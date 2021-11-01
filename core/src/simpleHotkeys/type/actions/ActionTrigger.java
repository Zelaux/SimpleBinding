package simpleHotkeys.type.actions;

import arc.scene.ui.layout.*;
import simpleHotkeys.type.*;

public interface ActionTrigger{
    boolean trigger();
    void display(Table table, ActionWithTrigger action);
}
