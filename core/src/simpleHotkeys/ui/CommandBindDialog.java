package simpleHotkeys.ui;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import simpleHotkeys.*;
import simpleHotkeys.gen.*;
import simpleHotkeys.tools.*;
import simpleHotkeys.type.*;

import java.util.*;

public class CommandBindDialog extends BaseDialog{
    static SortByClass sortByClass = new SortByClass();
    private ButtonsDialog buttonsDialog=new ButtonsDialog();
    Runnable rebuild = () -> {
    };

    public CommandBindDialog(){
        super("@dialog.simple-hotkeys-edit.title");
        shown(this::rebuild);
        hidden(() -> {
            SHVars.simpleActions.save();
//            SHVars.simpleActions.hide();
        });
    }

    private void rebuild(){
        cont.clearChildren();
        buttons.clearChildren();


        Seq<ActionWithTrigger> actions = SHVars.simpleActions.actions;
        cont.pane(actionTable -> {
            rebuild = () -> {
                actionTable.clearChildren();
                for(ActionWithTrigger action : actions){
                    action.triggers.sort(sortByClass);
                    actionTable.table(Tex.pane, t -> {
                        action.display(t, rebuild);

                        t.table(tb->{
                            tb.button( Icon.copy, () -> {
                                actions.insert(actions.indexOf(action), JsonIO.copy(action));
                                rebuild.run();
                            }).size(64f, 64f).bottom().right();
                            tb.button( Icon.trash, () -> {
                                actions.remove(action);
                                rebuild.run();
                            }).size(64f, 64f).bottom().right();
                        }).colspan(2).right().margin(0).marginBottom(-8f).marginRight(-8f).pad(0);
                    });
                    actionTable.row();
                }
            };
        }).fillX();
        ActionList value[] = {ActionList.values()[0]};
        int length = ActionList.values().length;
        int cols = length < 4 ? length : length / 2;
        buttons.button("@simple-hotkeys.add-action", () -> {
            actions.add(value[0].create());
            rebuild.run();
        }).size(210f, 64);
        buttons.button(button -> {
            button.label(() -> value[0].name());
            button.clicked(() -> {
                UIUtils.showSelect(button, ActionList.values(), value[0], newValue -> {
                    value[0] = newValue;
                }, cols, cell -> cell.size(220f, 74));
            });
        }, Styles.logict, () -> {
        }).size(210f, 64).left().padLeft(2);

        buttons.button("@dialog.edit-buttons",()->buttonsDialog.show()).size(210f, 64f).row();
        rebuild.run();

        buttons.defaults().colspan(3);
        addCloseButton();
        buttons.defaults().colspan(1);
//        SHVars.simpleActions.show(this);
//        cont.pane(t->{
//        }).fill().get().setScrollingDisabled(true,false);

//        Core.scene.setKeyboardFocus(this);
//        Core.scene.setScrollFocus(this);
//        Core.scene.unfocus(this);
    }

    public static class SortByClass implements Comparator<Object>{

        @Override
        public int compare(Object o1, Object o2){
            if(o1 == null || o2 == null || o1 == o2){
                return 0;
            }
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }
}
