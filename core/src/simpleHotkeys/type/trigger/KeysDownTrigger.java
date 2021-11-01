package simpleHotkeys.type.trigger;

import arc.*;
import arc.input.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.KeybindDialog.*;
import simpleHotkeys.*;
import simpleHotkeys.type.actions.*;

import static arc.Core.scene;

public class KeysDownTrigger implements ActionTrigger{
    protected static KeybindDialogStyle style;
    Seq<KeyCode> keyCodes = new Seq<>();

    public static Seq<KeyCode> validKeyCodes(){
        return Seq.with(KeyCode.values()).select(c -> !c.axis);
    }

    @Override
    public boolean trigger(){
        if(keyCodes.isEmpty()) return false;
        return keyCodes.count(t -> Core.input.keyDown(t)) == keyCodes.size;
    }

    @Override
    public void display(Table rootTable, ActionWithTrigger action){
        if(style == null){
            scene.getStyle(KeybindDialogStyle.class);
        }
        TextButtonStyle tstyle = Styles.defaultt;
        Runnable rebuild[]={null};
        rootTable.table(table->{
            rebuild[0]=()->{
                table.clearChildren();
                for(int i = 0; i < keyCodes.size; i++){
                    final int fixedI = i;
                    KeyCode keyCode = keyCodes.get(i);
                    table.table(Tex.pane, t -> {
                        t.add(keyCode.toString(),
                        style.keyColor).left().minWidth(90).padRight(20);

                        t.button("@settings.rebind", tstyle, () -> {
                            SHVars.shUI.rebindDialog.show(key -> {
                                keyCodes.set(fixedI, key);
                                rebuild[0].run();
                            });
//                    rebindAxis = false;
//                    rebindMin = false;
//                    openDialog(section, keybind);
                        }).width(130f);
                    });
                    if((i + 1) % 2 == 0) table.row();
                }
                if (keyCodes.isEmpty()){
                    table.add("@none").row();
                }
                table.button("@add",()->{
                    keyCodes.add(KeyCode.unknown);
                    rebuild[0].run();
                });
            };
            rebuild[0].run();
        });
    }
}
