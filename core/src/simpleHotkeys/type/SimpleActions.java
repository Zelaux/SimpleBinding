package simpleHotkeys.type;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.ui.*;
import simpleHotkeys.gen.*;
import simpleHotkeys.tools.*;
import simpleHotkeys.type.actions.*;
import simpleHotkeys.type.trigger.*;

public class SimpleActions{
    public static final String settingsKey = "simple-hotkeys-actions";
    public Seq<ActionWithTrigger> actions = new Seq<>();
  transient   Runnable rebuild = null;

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
        actions.each(ActionWithTrigger::update);
//        save();
    }

    public SimpleActions save(){
        Core.settings.put(settingsKey,JsonIO.write(this));
        return this;
    }
    public void show(Dialog table){

    }
}
