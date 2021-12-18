package simpleHotkeys.type;

import arc.*;
import arc.scene.ui.*;
import arc.struct.*;
import mindustry.io.*;

public class SimpleActions{
    public static final String settingsKey = "simple-hotkeys-actions";
    public Seq<Action> actions = new Seq<>();
    public Seq<ButtonObject> buttons = new Seq<>();
  transient   Runnable rebuild = null;
public ButtonObject buttonsById(String id){
    return buttons.find(b->b.id.equals(id));
}
    public static SimpleActions load(){
//        Core.settings.remove(settingsKey);
//        return new SimpleActions();
        if (!Core.settings.has(settingsKey)){
            return new SimpleActions().save();
        }
        try{
            return JsonIO.read(SimpleActions.class, Core.settings.getString(settingsKey));
        }catch(Exception e){
            e.printStackTrace();
            return new SimpleActions().save();
        }
//        return Core.settings.getJson(settingsKey, SimpleActions.class, SimpleActions::new);
    }

    public void check(){
        actions.each(Action::update);
//        save();
    }

    public SimpleActions save(){
        Core.settings.put(settingsKey,JsonIO.write(this));
        return this;
    }
    public void show(Dialog table){

    }
}
