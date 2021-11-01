package simpleHotkeys.type.actions;

import arc.*;
import arc.input.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;

public class SendMessageAction extends ActionWithTrigger{
    public String command;

    @Override
    public void invoke(){
        if(command != null && Vars.player != null){
            Call.sendChatMessage(command);
        }
    }

    @Override
    public void display(Table table){
        table.add("command: ");
        table.field(command, this::command).row();

        table.pane(t -> {
            for(ActionTrigger trigger : triggers){
                trigger.display(table,this);
            }
        });
    }

    public SendMessageAction command(String command){
        this.command = command;
        return this;
    }

}
