package simpleHotkeys.type.trigger;

import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.ui.*;
import simpleHotkeys.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.type.*;

@RegisterTrigger("buttonPress")
public class ButtonPressTrigger extends ActionTrigger{
    public String buttonId;
    private transient ButtonObject object;
    private transient boolean mustFind = true;

    @Override
    public boolean trigger(){
        if(mustFind && object == null){
            updateButton();
            mustFind = false;
        }
        if(object == null) return false;
        return object.clicked();
    }

    private void updateButton(){
        object = SHVars.simpleActions.buttonsById(buttonId);
    }

    @Override
    protected void rebuild(Table table){
        table.add("@trigger.button-press.id");
        table.add(new TextField(buttonId){{
            NinePatchDrawable underline = ((NinePatchDrawable)Tex.underline).tint(Color.white);
            setStyle(new TextFieldStyle(Styles.defaultField){{
                background = underline;
            }});
            updateButton();
            underline.getPatch().getColor().set(object == null ? Color.red : Color.white);
            changed(() -> {
                String text = getText();
                buttonId = text;
                ButtonObject object = SHVars.simpleActions.buttonsById(text);
                underline.getPatch().getColor().set(object == null ? Color.red : Color.white);
                ButtonPressTrigger.this.object = object;
                mustFind = true;
            });
        }});
        removeButton(table);
    }
}
