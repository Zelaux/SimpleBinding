package simpleHotkeys.type.trigger;

import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.type.*;

@Trigger("connectedToServer")
public class ConnectedToServerTrigger extends ActionTrigger{
    private transient boolean
    client = false, connecting = false;

    @Override
    public boolean trigger(){
        boolean returns = false;
        if(Vars.net.client() && !client ){
            boolean isConnecting = Vars.netClient.isConnecting();
            if(isConnecting && !connecting){
                connecting = true;
            }else if(!isConnecting && connecting){
                returns = true;
                connecting=false;
                client = true;
            }
        }else if(!Vars.net.client()){
            client=connecting = false;
        }
        return returns;
    }

    @Override
    public void rebuild(Table table){
        table.label(() -> getClass().getSimpleName().replace("Trigger", ""));
        removeButton(table);
    }
}
