package simpleHotkeys;

import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.blocks.*;
import mma.*;
import simpleHotkeys.core.*;
import simpleHotkeys.type.*;

import static mindustry.Vars.*;

public class SHVars extends ModVars{
    private final static Seq<Runnable> onLoad = new Seq<>();
    //    public static ModSettings settings;
//    public static ModNetClient netClient;
//    public static ModNetServer netServer;
    public static SHUI sbUI;
    //    public static ModLogic logic;
    public static SimpleActions simpleActions;
    public static ApplicationCore preListener;

    static{
        new SHVars();
    }

    public static void create(){
        //none
    }

    public static void init(){
        simpleActions = SimpleActions.load();
        ModListener.updaters.add(simpleActions::check);

        preListener = new ApplicationCore(){
            @Override
            public void setup(){

            }

            @Override
            public void update(){
                Action.preUpdate.run();
                super.update();
            }
        };
       Core.app.post(()->{
           Core.app.getListeners().insert(0,preListener);
       });
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
        if(!headless) listener.add(sbUI = new SHUI());
        //listener.add(netClient = new ModNetClient());
        //listener.add(netServer = new ModNetServer());
        //listener.add(logic = new ModLogic());
    }

    public static Unit findUnit(Boolf<Unit> filter){

        Unit unit = Units.closest(player.team(), Core.input.mouseWorld().x, Core.input.mouseWorld().y, 40f, filter);
        if(unit != null && filter.get(unit)){
            unit.hitbox(Tmp.r1);
            Tmp.r1.grow(6f);
            if(Tmp.r1.contains(Core.input.mouseWorld())){
                return unit;
            }
        }

        Building build = world.buildWorld(Core.input.mouseWorld().x, Core.input.mouseWorld().y);
        if(build instanceof ControlBlock cont && cont.canControl() && build.team == player.team() && filter.get(cont.unit())){
            return cont.unit();
        }

        return null;
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
