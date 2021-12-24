package simpleHotkeys.type;

import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ui.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.gen.*;
import simpleHotkeys.tools.*;
@RootAction
public abstract class Action{
    public final static TaskQueue preUpdate=new TaskQueue();
    public Seq<ActionTrigger> triggers = new Seq<>();

    public void update(){
        if(triggers != null && triggers.any() && triggers.count(ActionTrigger::trigger) == triggers.size){
            invoke();
        }
    }

    public abstract void displayOwn(Table table);

    public void display(Table table, Runnable onRemove){
        Runnable rebuild[] = {null};
        table.add(actionList().name()).colspan(2).row();
        table.table(this::displayOwn).fillX().colspan(2).row();

        table.pane(t -> {
            rebuild[0] = () -> {
                t.clearChildren();
                int counter = 0;
                for(ActionTrigger trigger : triggers){
                    counter++;
                    trigger.display(t, ()->{
                        triggers.remove(trigger);
                        rebuild[0].run();
                    });
                    if(counter % 2 == 0){
                        t.row();
                    }


                } if(counter == 0){
                    t.add("@none").colspan(2).row();
                }else if(counter % 2 != 0){
                    t.row();
                }
                addTriggerButton(t, rebuild[0]);
            };
            rebuild[0].run();
        }).colspan(2).row();
    }

    protected void addTriggerButton(Table table, Runnable onAdded){
        TriggerList value[] = {TriggerList.keyAction};
        int length = TriggerList.values().length;
        int cols = length < 4 ? length : length / 2;
        table.button("@simple-hotkeys.add-trigger", () -> {
            triggers.add(value[0].create());
            onAdded.run();
        }).size(210f, 50);
        table.button(button -> {
            button.label(() -> value[0].name());
            button.clicked(() -> {
                UIUtils.showSelect(button, TriggerList.values(), value[0], newValue -> {
                    value[0] = newValue;
                }, cols, cell -> cell.size(220f, 60));
            });
        }, Styles.logict, () -> {
        }).size(210f, 50).left().padLeft(2);
    }

    public abstract void invoke();
    public Class clazz(){
        Class clazz = getClass();
        return clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz;
    }
    public ActionList actionList(){
        return Structs.find(ActionList.values(), t -> t.referenceType == clazz());
    }
}
