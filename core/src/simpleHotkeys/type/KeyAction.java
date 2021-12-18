package simpleHotkeys.type;


import arc.func.*;
import arc.input.*;

import static arc.Core.input;

public enum KeyAction{
    down(k -> input.keyDown(k)), tap(k -> input.keyTap(k)), release(k -> input.keyRelease(k));
    final Boolf<KeyCode> action;

    KeyAction(Boolf<KeyCode> action){
        this.action = action;
    }

    public boolean trigger(KeyCode key){
        return action.get(key);
    }

    public boolean down(){
        return this==down;
    }
    public boolean release(){
        return this==release;
    }
    public boolean tap(){
        return this==tap;
    }
}
