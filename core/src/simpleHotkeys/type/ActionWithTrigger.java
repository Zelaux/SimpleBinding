package simpleHotkeys.type;

import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.ui.*;
import simpleHotkeys.gen.*;
import simpleHotkeys.tools.*;

public abstract class ActionWithTrigger{
    public Seq<ActionTrigger> triggers = new Seq<>();

    public void update(){
        if(triggers != null && triggers.any() && triggers.count(ActionTrigger::trigger) == triggers.size){
            invoke();
        }
    }

    public abstract void displayOwn(Table table);

    public void display(Table table, Runnable onRemove){
        Runnable rebuild[] = {null};
        displayOwn(table);

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
        TriggerEnum value[] = {TriggerEnum.keyActionTrigger};
        int length = TriggerEnum.values().length;
        int cols = length < 4 ? length : length / 2;
        table.button("@simple-hotkeys.add-trigger", () -> {
            triggers.add(value[0].create());
            onAdded.run();
        }).size(210f, 50);
        table.button(button -> {
            button.label(() -> value[0].name());
            button.clicked(() -> {
                UIUtils.showSelect(button, TriggerEnum.values(), value[0], newValue -> {
                    value[0] = newValue;
                }, cols, cell -> cell.size(220f, 60));
            });
        }, Styles.logict, () -> {
        }).size(210f, 50).left().padLeft(2);
    }

    public abstract void invoke();
}
