package simpleHotkeys.type.actions;

import arc.*;
import arc.input.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.KeybindDialog.*;
import simpleHotkeys.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.tools.*;
import simpleHotkeys.type.*;

import static arc.Core.scene;

@RegisterAction("invokeKey")
public class InvokeKeyAction extends Action{
    protected static KeybindDialogStyle style;
    KeyCode keyCode = KeyCode.unknown;
    KeyAction keyAction = KeyAction.down;

    @Override
    public void displayOwn(Table table){

        if(style == null){
            style = scene.getStyle(KeybindDialogStyle.class);
        }
        TextButtonStyle tstyle = Styles.defaultt;

        table.clearChildren();
        table.defaults().left();

        if(!Vars.mobile){
            table.add(keyCode.toString(), style.keyColor).left().minWidth(90).padRight(20);
            table.button("@settings.rebind", tstyle, () -> {
                SHVars.sbUI.rebindDialog.show(key -> {
                    keyCode = key;
                    displayOwn(table);
                });
            }).width(130f);
        } else {
//            table.add(keyCode.toString(), style.keyColor).left().width(90+20+130f);
            table.button(button -> {
                button.label(() -> keyCode.name());
                button.clicked(() -> UIUtils.showSelect(button, KeyCode.all, keyCode, k -> {
                    keyCode = k;
                    displayOwn(table);
                }, 3, cell -> {
                    cell.size(100, 50);
                }));
            }, Styles.logict, () -> {
            }).size(90, 40).width(90+20+130f);
        }
        table.button(button -> {
            button.label(() -> keyAction.name());
            button.clicked(() -> UIUtils.showSelect(button, KeyAction.values(), keyAction, k -> {
                keyAction = k;
                displayOwn(table);
            }, 3, cell -> {
                cell.size(100, 50);
            }));
        }, Styles.logict, () -> {
        }).size(90, 40).left().padLeft(2);
    }

    @Override
    public void invoke(){
        KeyboardDevice keyboard = Core.input.getKeyboard();
        preUpdate.post(()->{
            if(keyAction.down() || keyAction.tap()){

                keyboard.keyDown(keyCode);
            }
            if(keyAction.release() || keyAction.tap()){

                keyboard.keyUp(keyCode);
            }
        });
    }
}
