package simpleHotkeys.type.actions;

import arc.scene.ui.layout.*;
import arc.struct.*;

public abstract class ActionWithTrigger{
    public Seq<ActionTrigger> triggers;

    public void update(){
        if (triggers!=null && triggers.count(ActionTrigger::trigger)==triggers.size){
            invoke();
        }
    }

    public abstract void display(Table table);
    public abstract void invoke();
}
