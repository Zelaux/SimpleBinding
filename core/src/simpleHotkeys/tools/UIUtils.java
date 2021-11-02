package simpleHotkeys.tools;

import arc.*;
import arc.func.*;
import arc.math.*;
import arc.scene.*;
import arc.scene.actions.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;

public class UIUtils{
    public static <T extends Enum<T>> void showSelect(Button b, T[] values, T current, Cons<T> getter, int cols, Cons<Cell> sizer){
        showSelectTable(b, (t, hide) -> {
            ButtonGroup<Button> group = new ButtonGroup<>();
            int i = 0;
            t.defaults().size(60f, 38f);

            for(T p : values){
                sizer.get(t.button(p.toString(), Styles.logicTogglet, () -> {
                    getter.get(p);
                    hide.run();
                }).self(c -> tooltip(c, p)).checked(current == p).group(group));

                if(++i % cols == 0) t.row();
            }
        });
    }

    public static void tooltip(Cell<?> cell, Enum<?> key){
        String cl = key.getClass().getSimpleName().toLowerCase() + "." + key.name().toLowerCase();
        if(Core.bundle.has(cl)){
            tooltip(cell, cl);
        }else{

        }
    }
    public static void tooltip(Cell<?> cell, String key){
        String lkey = key.toLowerCase().replace(" ", "");
        if(Core.bundle.has(lkey)){
            var tip = new Tooltip(t -> t.background(Styles.black8).margin(4f).add("[lightgray]" + Core.bundle.get(lkey)).style(Styles.outlineLabel));

            //mobile devices need long-press tooltips
            if(Vars.mobile){
                cell.get().addListener(new ElementGestureListener(20, 0.4f, 0.43f, 0.15f){
                    @Override
                    public boolean longPress(Element element, float x, float y){
                        tip.show(element, x, y);
//                        canvas.tooltips.add(tip);
                        //prevent touch down for other listeners
                        for(var list : cell.get().getListeners()){
                            if(list instanceof ClickListener cl){
                                cl.cancel();
                            }
                        }
                        return true;
                    }
                });
            }else{
                cell.get().addListener(tip);
            }

        }
    }

    public static void showSelectTable(Button b, Cons2<Table, Runnable> hideCons){
        Table t = new Table(Tex.paneSolid){
            @Override
            public float getPrefHeight(){
                return Math.min(super.getPrefHeight(), Core.graphics.getHeight());
            }

            @Override
            public float getPrefWidth(){
                return Math.min(super.getPrefWidth(), Core.graphics.getWidth());
            }
        };
        t.margin(4);

        //triggers events behind the element to simulate deselection
        Element hitter = new Element();

        Runnable hide = () -> {
            Core.app.post(hitter::remove);
            t.actions(Actions.fadeOut(0.3f, Interp.fade), Actions.remove());
        };

        hitter.fillParent = true;
        hitter.tapped(hide);

        Core.scene.add(hitter);
        Core.scene.add(t);

        t.update(() -> {
            if(b.parent == null || !b.isDescendantOf(Core.scene.root)){
                Core.app.post(() -> {
                    hitter.remove();
                    t.remove();
                });
                return;
            }

            b.localToStageCoordinates(Tmp.v1.set(b.getWidth()/2f, b.getHeight()/2f));
            t.setPosition(Tmp.v1.x, Tmp.v1.y, Align.center);
            if(t.getWidth() > Core.scene.getWidth()) t.setWidth(Core.graphics.getWidth());
            if(t.getHeight() > Core.scene.getHeight()) t.setHeight(Core.graphics.getHeight());
            t.keepInStage();
            t.invalidateHierarchy();
            t.pack();
        });
        t.actions(Actions.alpha(0), Actions.fadeIn(0.3f, Interp.fade));

        t.top().pane(inner -> {
            inner.top();
            hideCons.get(inner, hide);
        }).pad(0f).top().scrollX(false);

        t.pack();
    }
}
