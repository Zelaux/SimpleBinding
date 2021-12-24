package simpleHotkeys.ui;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.scene.ui.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.dialogs.*;
import simpleHotkeys.*;
import simpleHotkeys.type.*;
import simpleHotkeys.ui.actions.*;

import static mindustry.Vars.renderer;

public class ButtonsDialog extends BaseDialog{
    ObjectSet<String> ids = new ObjectSet<>();
    BlinkAction blinkAction = new BlinkAction();
    private Table buttonsTable;
     private final FrameBuffer hudBuffer=new FrameBuffer();

    public ButtonsDialog(){
        super("@dialog.buttons");

        shown(this::rebuildButtons);
        shown(() -> SHVars.simpleActions.buttons.each(ButtonObject::remove));
        hidden(() -> SHVars.simpleActions.buttons.each(ButtonObject::add));
        setup();
    }

    private void setup(){
        cont.pane(this::rebuildButtons).row();
        buttons.defaults().size(210f, 64f);
        String[] id = {null};
        TextField idField = new TextField("");
        idField.changed(() -> {
            if(idField.isValid()){
                String text = idField.getText();
                if(ids.contains(text) || text.isEmpty()){
                    idField.color.set(Color.yellow);
                }else{
                    idField.color.set(Color.white);
                    id[0] = text;
                }
                blinkAction.setEndColor(idField.color);
            }
        });
        buttons.button("@buttons.add", () -> {
            String id_ = id[0];
            if(id_ == null || id_.isEmpty() || ids.contains(id_)){
//                Color color = idField.color.cpy();
//                idField.color.set(Color.red);
//                idField.getActions().each(a -> a instanceof ColorAction, idField::removeAction);
                idField.removeAction(blinkAction);
                blinkAction.setDuration(10f);
//                blinkAction.reset();
                blinkAction.setStartColor(Color.red);
                blinkAction.setEndColor(idField.color);
                idField.addAction(blinkAction);
//                ColorAction action = Actions.color(Color.white, 1f);
//                idField.addAction(action);
            }else{
                ButtonObject object = new ButtonObject();
                object.id = id_;
                object.x = Core.graphics.getWidth() / 2f;
                object.y = Core.graphics.getHeight() / 2f;
                SHVars.simpleActions.buttons.add(object);
                rebuildButtons();
            }
        });
        buttons.add(idField).row();


        buttons.defaults().colspan(2);
        addCloseButton();
        buttons.defaults().colspan(1);
    }

    private void rebuildButtons(){
        rebuildButtons(buttonsTable);
    }

    private void rebuildButtons(Table table){
        this.buttonsTable = table;
        if(SHVars.simpleActions == null){

            return;
        }
        table.clearChildren();
        ids.clear();
        for(ButtonObject object : SHVars.simpleActions.buttons){
            ids.add(object.id);
            table.table(Tex.pane, t -> {
                Label infoLabel = new Label("");
                t.add("@button.id");
                t.field(object.id, id -> {

                    if(id.equals(object.id)) return;
                    if(ids.contains(id)){
                        infoLabel.setText("@buttons.same-id");
                    }else{
                        ids.remove(object.id);
                        ids.add(id);
                        object.id = id;
                    }
                });
                t.add("@button.text");
                t.field(object.text, text -> object.text = text).row();

                t.add("@button.width");
                t.field("" + object.width, TextFieldFilter.floatsOnly, value -> object.width = Strings.parseFloat(value, object.width));
                t.add("@button.height");
                t.field("" + object.height, TextFieldFilter.floatsOnly, value -> object.height = Strings.parseFloat(value, object.height)).row();

                t.button("@button.position", () -> {
                    boolean[] firstDraw = {true};
                    MovableButton.preDraw(() -> {
                        if(firstDraw[0]){
                            firstDraw[0] = false;
                            hudBuffer.resize(Core.graphics.getWidth(), Core.graphics.getWidth());

                            Tmp.m1.set(Draw.proj());
                            Draw.proj(0,0,Core.graphics.getWidth(),Core.graphics.getHeight());
                            hudBuffer.begin(Color.clear);
                            Vars.ui.hudGroup.draw();

                            for(ButtonObject button : SHVars.simpleActions.buttons){
                                if(button == object){
                                    continue;
                                }
                                button.toButton().draw();
                            }
                            hudBuffer.end();
                            Draw.proj(Tmp.m1);
                        }
                        Draw.mixcol(Color.cyan,0.3f);
                        float w = Core.scene.getWidth();
                        float h = Core.scene.getHeight();
                        Draw.rect(Draw.wrap(hudBuffer.getTexture()),w/2f,h/2f, w, -h,0f);
                        Draw.mixcol();
//                        Vars.renderer.effectBuffer.blit(Shaders.screenspace);
                    });
                    MovableButton.show(object.toButton(), (x, y) -> {
                        object.x = x;
                        object.y = y;
                    });
                }).size(210f, 64f).colspan(3);
                t.button("@delete", () -> {
                    SHVars.simpleActions.buttons.remove(object);
                    rebuildButtons();
                }).right().bottom().colspan(1);
            }).row();

        }
    }

}
