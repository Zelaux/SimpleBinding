package simpleHotkeys;

import arc.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mma.*;
import simpleHotkeys.core.*;
import simpleHotkeys.type.*;

import static mindustry.Vars.headless;

public class SHVars extends ModVars{
    private final static Seq<Runnable> onLoad = new Seq<>();
//    public static ModSettings settings;
//    public static ModNetClient netClient;
//    public static ModNetServer netServer;
    public static SHUI shUI;
//    public static ModLogic logic;
    public static SimpleActions simpleActions;

    static{
        new SHVars();
    }

    public static void create(){
        //none
    }

    public static void init(){
        simpleActions = SimpleActions.load();
        ModListener.updaters.add(simpleActions::check);
    }

    /**
     * Here you can initialize your classes as ModSettings or ModLogic and
     * add listeners to ModListener (listener variable in ModVars)
     */
    public static void load(){
        onLoad.each(Runnable::run);
        onLoad.clear();
        //for example
        //settings = new ModSettings();
        if (!headless) listener.add(shUI = new SHUI());
        //listener.add(netClient = new ModNetClient());
        //listener.add(netServer = new ModNetServer());
        //listener.add(logic = new ModLogic());
    }


    public static void modLog(String text, Object... args){
        Log.info("[@] @", modInfo == null ? "simple-hotkeys" : modInfo.name, Strings.format(text, args));
    }

    @Override
    protected void onLoad(Runnable runnable){
        onLoad.add(runnable);
    }

    @Override
    public String getFullName(String name){
        return null;
    }

    @Override
    public ContentList[] getContentList(){
        return new ContentList[0];
    }

    @Override
    protected void showException(Throwable exception){
        Log.err(exception);
        if(headless) return;
        if(modInfo == null || Vars.ui == null){
            Events.on(EventType.ClientLoadEvent.class, event -> {
                Vars.ui.showException(Strings.format("Error in @", modInfo == null ? null : modInfo.meta.displayName), exception);
            });
        }else{
            Vars.ui.showException(Strings.format("Error in @", modInfo.meta.displayName), exception);
        }
    }
}
