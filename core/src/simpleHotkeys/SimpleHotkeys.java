package simpleHotkeys;

import mindustry.*;
import mindustry.ctype.*;
import mma.*;
import mma.annotations.*;

import static simpleHotkeys.SHVars.*;

/** If you have no sprites, music and sounds in your mod, remove the annotation after this line */
@ModAnnotations.ModAssetsAnnotation
public class SimpleHotkeys extends MMAMod{
    public SimpleHotkeys(){
        super();
        ModVars.loaded = true;
        SHVars.load();
        modLog("constructor");
    }


    public void init(){
        modInfo = Vars.mods.getMod(this.getClass());
        modLog("init");
        //if you do not need ModListener just remove line after this comment
        if(neededInit) listener.init();
    }
}
