package simpleHotkeys.type;

import arc.*;
import arc.func.*;
import arc.input.*;
import arc.struct.*;
import mindustry.core.*;
import mindustry.gen.*;
import simpleHotkeys.*;

public enum TextFormat{
    player(()->{
        Unit unit = SHVars.findUnit(Unit::isPlayer);
        if (unit==null)return "null";
        return unit.getPlayer();
    }),
    unit(()->{

        Unit unit = SHVars.findUnit(u->true);
        if (unit==null)return "null";
        return unit.type.name;
    }),
    tileX(()-> World.toTile(Core.input.mouseWorldX())),
    tileY(()-> World.toTile(Core.input.mouseWorldY())),
    mouseX(()->Core.input.mouseWorldX()),
    mouseY(()->Core.input.mouseWorldY()),

    numTap(()->{
        for(int i = 0; i < 10; i++){
            if(Core.input.keyTap(KeyCode.valueOf("num"+i)) || Core.input.keyTap(KeyCode.valueOf("numpad"+i))){
                return i;
            }
        }
        return -1;
    }),
    numDown(()->{
        for(int i = 0; i < 10; i++){
            if(Core.input.keyDown(KeyCode.valueOf("num"+i)) || Core.input.keyDown(KeyCode.valueOf("numpad"+i))){
                return i;
            }
        }
        return -1;
    }),
    numUp(()->{
        for(int i = 0; i < 10; i++){
            if(Core.input.keyRelease(KeyCode.valueOf("num"+i)) || Core.input.keyRelease(KeyCode.valueOf("numpad"+i))){
                return i;
            }
        }
        return -1;
    }),
    num(numDown)

    ;
    public static final TextFormat[] all = values();
    public final Prov<Object> replacement;
    public final String replaceTarget;

    TextFormat(Prov<Object> replacement){
        this.replacement = replacement;
        replaceTarget="{"+name()+"}";
    }

    TextFormat(TextFormat other){
        this(other.replacement);
    }

    public static String format(String text){
        IntMap<TextFormat> map=new IntMap<>();
        for(TextFormat format : all){
            int index = text.indexOf(format.replaceTarget);
            while(index!=-1){
                map.put(index,format);
                index=text.indexOf(format.replaceTarget,index+1);
            }
        }
        IntSeq keys = map.keys().toArray();
        keys.sort();
        StringBuilder builder=new StringBuilder();
        int[] lastIndex={0};
        keys.each(key->{
            builder.append(text,lastIndex[0],key);
            TextFormat format = map.get(key);
            builder.append(format.replacement());
            lastIndex[0]=key+format.replaceTarget.length();
        });
        builder.append(text,lastIndex[0],text.length());
        return builder.toString();
    }

    private String replacement(){
        return replacement.get()+"";
    }
}
