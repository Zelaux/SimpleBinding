package simpleHotkeys.type;

import arc.scene.ui.layout.*;

public interface ActionTrigger{
    boolean trigger();
    void display(Table table, Runnable onRemove);
}
