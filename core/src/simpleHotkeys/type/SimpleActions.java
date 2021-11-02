package simpleHotkeys.type;

import arc.*;
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
    public void show(Table table){
        Table buttons, cont;
        table.add(cont = new Table()).fillX();
        table.row();
        table.add(buttons = new Table()).fillX();

        cont.defaults().pad(3);
        buttons.defaults().pad(3);

        rebuild = () -> {
            cont.clearChildren();
            for(ActionWithTrigger action : actions){
                cont.table(Tex.pane, t -> {
                    action.display(t, rebuild);

                    t.button("@copy", Icon.copy, new TextButtonStyle(Styles.defaultt){{
                        up=Tex.buttonEdge3;
                    }}, () -> {
                        Log.info("action: @", JsonIO.write(action));
                        actions.insert(actions.indexOf(action), JsonIO.copy(action));
                        rebuild.run();
                    }).size(210f, 64f).bottom().left();
                    t.button("@delete", Icon.trash, new TextButtonStyle(Styles.defaultt){{
                        up=Tex.buttonEdge1;
                    }}, () -> {
                        actions.remove(action);
                        rebuild.run();
                    }).size(210f, 64f).bottom().right();
                });
                cont.row();
            }
        };
        ActionEnum value[] = {ActionEnum.values()[0]};
        int length = ActionEnum.values().length;
        int cols = length < 4 ? length : length / 2;
        buttons.button("@simple-hotkeys.add-trigger", () -> {
            actions.add(value[0].create());
            rebuild.run();
        }).size(210f, 64);
        buttons.button(button -> {
            button.label(() -> value[0].name());
            button.clicked(() -> {
                UIUtils.showSelect(button, ActionEnum.values(), value[0], newValue -> {
                    value[0] = newValue;
                }, cols, cell -> cell.size(220f, 74));
            });
        }, Styles.logict, () -> {
        }).size(210f, 64).left().padLeft(2);
        rebuild.run();
    }
}
