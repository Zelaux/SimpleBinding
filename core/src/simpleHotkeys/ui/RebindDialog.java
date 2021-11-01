package simpleHotkeys.ui;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.util.*;

import static arc.Core.bundle;

public class RebindDialog extends Dialog{
    String rebindKey;
    boolean rebindAxis = false;
    boolean rebindMin = true;
    KeyCode minKey;
    Cons<KeyCode> listener = code -> {
    };

    void rebind(KeyCode newKey){
        if(rebindKey == null) return;
        hide();
        listener.get(newKey);
        if(rebindAxis && rebindMin && !newKey.axis){
            rebindMin = false;
            minKey = newKey;
            show(listener, true, false);
        }else{
            listener = code -> {
            };
            rebindKey = null;
            rebindAxis = false;
        }
    }

    public void show(Cons<KeyCode> listener){
        show(listener, false);
    }

    public void show(Cons2<KeyCode, KeyCode> listener){
        show(keyCode -> {
            if(minKey != null){
                listener.get(minKey, keyCode);
                minKey = null;
            }
        }, true);
    }

    private void show(Cons<KeyCode> listener, boolean rebindAxis){
        show(listener, rebindAxis, rebindMin);
    }

    private void show(Cons<KeyCode> listener, boolean rebindAxis, boolean rebindMin){
        this.rebindAxis = rebindAxis;
        this.rebindMin = rebindMin;
        this.listener = listener;

        clearListeners();
        cont.clearChildren();

        title.setText(rebindAxis ? bundle.get("keybind.press.axis", "Press an axis or key...") : bundle.get("keybind.press", "Press a key..."));

        rebindKey = name;

        titleTable.getCells().first().pad(4);


        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                if(Core.app.isAndroid()) return false;
                rebind(button);
                return false;
            }

            @Override
            public boolean keyDown(InputEvent event, KeyCode keycode){
                hide();
                if(keycode == KeyCode.escape) return false;
                rebind(keycode);
                return false;
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY){
                if(!rebindAxis) return false;
                hide();
                rebind(KeyCode.scroll);
                return false;
            }
        });

        show();
        Time.runTask(1f, () -> getScene().setScrollFocus(this));
    }

}
