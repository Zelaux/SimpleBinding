package simpleHotkeys.type.trigger;

import arc.input.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.ui.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.tools.*;
import simpleHotkeys.type.*;

import static arc.Core.scene;

@RegisterTrigger("numberAction")
public class NumberActionTrigger extends ActionTrigger{
    public KeyAction keyAction = KeyAction.down;
    public NumberActionTrigger(){
    }

    @Override
    public boolean trigger(){
        boolean locked = Vars.control.input.locked();
        if(scene.getKeyboardFocus() instanceof TextField) return false;
        if(scene.hasDialog()) return false;
        if(scene.hasField()) return false;
        if(locked) return false;
        for(int i = 0; i < 10; i++){
            if(keyAction.trigger(KeyCode.valueOf("num" + i)) || keyAction.trigger(KeyCode.valueOf("numpad" + i))){
                return true;
            }
        }
        return false;
    }


    protected void rebuild(Table table){

        table.clearChildren();
        table.defaults().left();
        table.add(triggerList().name()).width(90 + 20 + 130);
//        table.add(keyCode.toString(), style.keyColor).left().minWidth(90).padRight(20);

//        table.button("@settings.rebind", tstyle, () -> {
//            SHVars.sbUI.rebindDialog.show(key -> {
//                keyCode = key;
//                rebuild(table);
//            });
//        }).width(130f);
        table.button(button -> {
            button.label(() -> keyAction.name());
            button.clicked(() -> UIUtils.showSelect(button, KeyAction.values(), keyAction, k -> {
                keyAction = k;
                rebuild(table);
            }, 3, cell -> {
                cell.size(100, 50);
            }));
        }, Styles.logict, () -> {
        }).size(90, 40).left().padLeft(2);
        removeButton(table).grow();
    }

}
