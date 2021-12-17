package simpleHotkeys.type.trigger;

import arc.func.*;
import arc.input.*;
import arc.scene.ui.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.KeybindDialog.*;
import simpleHotkeys.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.tools.*;
import simpleHotkeys.type.*;

import static arc.Core.*;
@Trigger("keyAction")
public class KeyActionTrigger extends ActionTrigger{
    protected static KeybindDialogStyle style;
    public KeyCode keyCode = KeyCode.unknown;
    public KeyAction keyAction = KeyAction.down;
    public boolean anyNumber=false;
    private transient Runnable onRemove = () -> {
    };

    public KeyActionTrigger(){
    }

    @Override
    public boolean trigger(){
        boolean locked = Vars.control.input.locked();
        if(scene.getKeyboardFocus() instanceof TextField) return false;
        if(scene.hasDialog()) return false;
        if(scene.hasField()) return false;
        if(locked) return false;
        if (anyNumber){
            for(int i = 0; i < 10; i++){
                if(keyAction.trigger(KeyCode.valueOf("num"+i)) || keyAction.trigger(KeyCode.valueOf("numpad"+i))){
                    return true;
                }
            }
            return false;
        }
        return keyAction.trigger(keyCode);
    }


    protected void rebuild(Table table){
        if(style == null){
            style = scene.getStyle(KeybindDialogStyle.class);
        }
        TextButtonStyle tstyle = Styles.defaultt;

        table.clearChildren();
        table.defaults().left();
        table.add(keyCode.toString(), style.keyColor).left().minWidth(90).padRight(20);

        table.button("@settings.rebind", tstyle, () -> {
            SHVars.sbUI.rebindDialog.show(key -> {
                keyCode = key;
                rebuild(table);
            });
        }).width(130f);
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

    public enum KeyAction{
        down(k -> input.keyDown(k)), tap(k -> input.keyTap(k)), release(k -> input.keyRelease(k));
        final Boolf<KeyCode> action;

        KeyAction(Boolf<KeyCode> action){
            this.action = action;
        }

        public boolean trigger(KeyCode key){
            return action.get(key);
        }
    }
}
