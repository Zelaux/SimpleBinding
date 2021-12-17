package simpleHotkeys.type;

import arc.*;
import arc.graphics.g2d.*;
import arc.scene.*;
import arc.scene.ui.*;
import mindustry.*;

public class ButtonObject{
    public float x = 10, y = 10;
    public float width, height;

    public String text;
    public String regionName;
    public String id;
    transient Button previous;
    private transient int clickedTick = 0;

    public boolean clicked(){
        return clickedTick > 0;
    }

    public void remove(){
        if(previous != null){
            previous.remove();
            previous = null;
        }
    }

    public void add(){
        Button button = toButton();
        if(previous != null){
            Group parent = previous.parent;
            int index = parent.getChildren().indexOf(previous);
            previous.remove();

            parent.addChildAt(index, button);

        }else{
            Core.scene.add(button);
        }
        previous = button;
    }

    public Button toButton(){
        boolean hasText = text != null;
        TextureRegion region = regionName == null ? null : Core.atlas.find(regionName);
        boolean hasRegion = region != null;

        Button button;
        if(hasText && hasRegion){
            button = new ImageButton(region){{
                add(text);
            }};
        }else if(hasText){
            button = new TextButton(text);
        }else if(hasRegion){
            button = new ImageButton(region);
        }else{
            button = new TextButton("@none");

        }
        button.clicked(() -> {
            clickedTick = 2;
        });
        button.update(() -> {
//            if (Vars.state.isGame()){
                clickedTick = Math.max(0, clickedTick - 1);
//            }
        });


        button.setDisabled(()->!Vars.state.isGame());
        button.setBounds(x, y, width, height);
        return button;
    }
}
