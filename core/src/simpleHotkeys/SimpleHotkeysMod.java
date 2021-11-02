package simpleHotkeys;

import arc.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.io.*;
import mma.*;
import mma.annotations.*;
import simpleHotkeys.annotations.SHAnnotations.*;
import simpleHotkeys.gen.*;

import static simpleHotkeys.SHVars.*;

/** If you have no sprites, music and sounds in your mod, remove the annotation after this line */
@ModAnnotations.ModAssetsAnnotation
@EnumConstructing
public class SimpleHotkeysMod extends MMAMod{
    public SimpleHotkeysMod(){
        super();
        ModVars.loaded = true;
        SHVars.load();
        modLog("constructor");
    }


    public void init(){
        modLog("init");
        modInfo = Vars.mods.getMod(this.getClass());
        Seq<Class<?>> classes=Seq.with();
        classes.addAll(Seq.with(ActionEnum.values()).map(a->a.referenceType).as());
        classes.addAll(Seq.with(TriggerEnum.values()).map(t->t.referenceType).as());
        for(Class<?> referenceType : classes){
            JsonIO.json.addClassTag(referenceType.getName(),referenceType);
        }

        SHVars.init();
        //if you do not need ModListener just remove line after this comment
        if(neededInit) listener.init();
    }
}
