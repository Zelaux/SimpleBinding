package simpleHotkeys.type.actions;

import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import simpleHotkeys.type.*;
import simpleHotkeys.type.trigger.*;

public class SendMessageAction extends ActionWithTrigger{
    public String command;

    @Override
    public void invoke(){
        if(command != null && Vars.player != null){
            Call.sendChatMessage(command);
        }
    }

    @Override
    public void displayOwn(Table table){
        table.table(t -> {
            t.add("command: ").left();
            t.field(command, this::command).maxTextLength(Vars.maxTextLength).right().growX().fillX();
        }).fillX().colspan(2).row();
    }

    public SendMessageAction command(String command){
        this.command = command;
        return this;
    }

}
