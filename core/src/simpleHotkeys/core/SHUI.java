package simpleHotkeys.core;

import arc.KeyBinds.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.*;
import mindustry.ui.*;
import mma.core.*;
import simpleHotkeys.ui.*;

import static mindustry.Vars.ui;

public class SHUI extends ModUI{
    public RebindDialog rebindDialog;
    public CommandBindDialog commandBindDialog;
    public SHUI(){
        super(new KeyBind[0]);
    }

    @Override
    public void init(){
        super.init();
        rebindDialog=new RebindDialog();
        commandBindDialog=new CommandBindDialog();
        Runnable rebuild=()->{
            Table settingUi = (Table)((Group)((Group)(ui.settings.getChildren().get(1))).getChildren().get(0)).getChildren().get(0);
            settingUi.row();
            settingUi.button("test",Styles.cleart, commandBindDialog::show);
        };
        Log.info("LOL");
        ui.settings.resized(rebuild::run);
        ui.settings.shown(rebuild::run);
//        ui.settings.menu.button("test",commandBindDialog::show);
    }
}
