package simpleHotkeys.type;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import simpleHotkeys.type.actions.*;
import simpleHotkeys.type.trigger.*;

public class SimpleActions{
    public Seq<ActionWithTrigger> actions =new Seq<>();
    public static final String settingsKey="simple-hotkeys-actions";
    public static SimpleActions load(){
        return new SimpleActions();
//        return Core.settings.getJson(settingsKey, SimpleActions.class, SimpleActions::new);
    }
    public void check(){
        actions.each(ActionWithTrigger::update);
        save();
    }
    public void save(){
//        Core.settings.putJson(settingsKey,this);
    }

    public void show(Table table){
        Runnable rebuild[]={()->{}};
        rebuild[0]=()->{
            for(ActionWithTrigger hotKeyAction : actions){
                table.table(Tex.pane, hotKeyAction::display);
                table.row();
            }
            table.button("add",()->{
                SendMessageAction new_action = new SendMessageAction().command("new_action");
                new_action.triggers.add(new KeysDownTrigger());
                actions.add(new_action);
//                save();
                rebuild[0].run();
            });
        };
        rebuild[0].run();
    }
}
