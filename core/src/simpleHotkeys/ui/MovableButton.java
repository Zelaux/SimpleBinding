package simpleHotkeys.ui;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.pooling.*;
import mindustry.gen.*;

public class MovableButton extends Table{
    private final static MovableButton instance = new MovableButton();
    private static Element element;
    private static Floatc2 positionCons;

    public MovableButton(){

        keyDown(key -> {
            if(key == KeyCode.escape || key == KeyCode.back){
                Core.app.post(this::hide);
            }
        });
        setFillParent(true);
        touchable = Touchable.enabled;
        boolean[] isPressed = {false};
        dragged((dx, dy) -> {
            if(isPressed[0]){

                Vec2 min = localToStageCoordinates(Tmp.v1.set(0, 0));
                Vec2 max = localToStageCoordinates(Tmp.v2.set(getWidth(), getHeight())).sub(element.getWidth(), element.getHeight());
                element.x += dx;
                element.y += dy;
                element.setPosition(Mathf.clamp(element.x, min.x, max.x), Mathf.clamp(element.y, min.y, max.y));
            }
        });
        add(new Element()).self(cell -> {
            cell.update(e -> {
                if(element != null){
                    Vec2 vec2 = e.stageToLocalCoordinates(Tmp.v1.set(element.x, element.y));
                    e.setBounds(vec2.x, vec2.y, element.getWidth(), element.getHeight());
                    cell.setBounds(vec2.x, vec2.y, element.getWidth(), element.getHeight());
                }
            });
            cell.get().addListener(new HandCursorListener());
        }).get().toBack();
        addListener(new ClickListener(){

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                isPressed[0] = false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                Rect set = Tmp.r1.set(element.x, element.y, element.getWidth(), element.getHeight());

//                Vec2 point = table.localToStageCoordinates(Tmp.v1.set(x, y));
//                Log.info("pos(@,@) vec2(@) rect(@)", x, y, point, set);
                if(set.contains(x, y)){
                    isPressed[0] = true;
//                    Log.info("touch");
//                    return true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

        });
//        Core.scene.root.addChildAfter();
//        addChild(Elem.im());
        button("@back", Icon.left, this::hide).size(210f, 64f).uniform().bottom().align(Align.bottom).self(cell -> {
            getCells().remove(cell);

//            TextButton button = cell.get();

            cell.update(button->{
               button.setBounds(getWidth()/2f-105f,0,210,64);
            });

            Pools.get(Cell.class, Cell::new).free(cell);
        });
    }

    public static void show(Element element, Floatc2 positionCons){
        MovableButton.element = element;

        MovableButton.positionCons = positionCons;
//        positionCons.get(Mathf.clamp(element.x, min.x, max.x), Mathf.clamp(element.y, min.y, max.y));

        instance.show();
//        table.clearChildren();

    }

    private void hide(){
        Core.scene.root.removeChild(this);
        positionCons.get(element.x, element.y);
        element = null;
    }

    private void show(){
        Core.scene.root.addChild(this);
        Core.scene.setKeyboardFocus(this);
        background(Tex.pane);
    }

    @Override
    public void draw(){
        super.draw();
        if(element != null){
            float prevX = element.x;
            float prevY = element.y;
            Vec2 vec2 = stageToLocalCoordinates(Tmp.v1.set(prevX, prevY));
            element.setPosition(vec2.x, vec2.y);
            element.draw();

            element.setPosition(prevX, prevY);
//                    element.validate();
        }
    }

}
