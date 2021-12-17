package simpleHotkeys.type.actions;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.ui.fragments.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.type.*;

import java.lang.reflect.*;

@Action("openChat")
public class OpenChatAction extends ActionWithTrigger{
    public String text="";

    @Override
    public void displayOwn(Table table){
        table.add("@open-chat.message");
        table.field(text, t -> text = t).maxTextLength(Vars.maxTextLength);
    }

    @Override
    public void invoke(){
        ChatFragment chatfrag = Vars.ui.chatfrag;
        if(!chatfrag.shown()){
            chatfrag.toggle();
            try{

                Field chatfield = ChatFragment.class.getField("chatfield");
                chatfield.setAccessible(true);
                TextField field = Reflect.get(chatfrag, chatfield);
                Core.app.post(() -> {
                    String format = TextFormat.format(text);
                    int cursorIndex = format.indexOf("{cursor}");
                    if(cursorIndex != -1){
                        field.setText(format.replace("{cursor}", ""));
                        field.setCursorPosition(cursorIndex);
                    }else{
                        field.setText(format);
                    }
                });
            }catch(Exception e){

            }
        }
//        chatfrag.toggle();
    }
}
