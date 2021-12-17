package simpleHotkeys.type.actions;

import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.type.*;

@Action("chatMessage")
public class SendMessageAction extends ActionWithTrigger{
    public String command;

    @Override
    public void invoke(){
        if(command != null && Vars.player != null){
            Call.sendChatMessage(TextFormat.format(command));
        }
    }

    @Override
    public void displayOwn(Table table){
        table.add("@send-message.message").left();
        table.field(command, this::command).maxTextLength(Vars.maxTextLength).right().growX().fillX();
    }

    public SendMessageAction command(String command){
        this.command = command;
        return this;
    }

}
