package simpleHotkeys.type;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.gen.*;
@RootTrigger
public abstract class ActionTrigger{
    private transient Runnable onRemove;

    public abstract boolean trigger();

    protected abstract void rebuild(Table table);

    protected Cell<ImageButton> removeButton(Table table){
        return table.button(Icon.trash, onRemove::run);
    }

    public final void display(Table rootTable, Runnable onRemove){
        this.onRemove = onRemove;
        rootTable.table(Tex.pane, this::rebuild);
    }

    public TriggerList triggerList(){
        return Structs.find(TriggerList.values(), t -> t.referenceType == clazz());
    }

    public Class clazz(){
        Class clazz = getClass();
        return clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz;
    }

}
