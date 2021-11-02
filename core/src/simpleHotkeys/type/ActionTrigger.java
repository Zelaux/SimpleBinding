package simpleHotkeys.type;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.KeybindDialog.*;

import static arc.Core.scene;

public abstract class ActionTrigger{
 private transient   Runnable onRemove;
    public abstract boolean trigger();

    protected abstract void rebuild(Table table);

    protected Cell<ImageButton> removeButton(Table table){
        return table.button(Icon.trash, onRemove::run);
    }

    public final void display(Table rootTable, Runnable onRemove){
        this.onRemove = onRemove;
        rootTable.table(Tex.pane, this::rebuild);
    }

}
