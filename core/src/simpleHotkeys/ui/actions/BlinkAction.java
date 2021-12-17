package simpleHotkeys.ui.actions;

import arc.graphics.*;
import arc.scene.*;
import arc.scene.actions.*;

public class BlinkAction extends TemporalAction{
    private final Color end = new Color();
    private boolean hasStartColor = false;
    private float startR, startG, startB, startA;
    private Color color;

    @Override
    protected void begin(){
        if(color == null) color = target.color;
        if(hasStartColor) return;
        startR = color.r;
        startG = color.g;
        startB = color.b;
        startA = color.a;
    }

    @Override
    protected void update(float percent){
        float r = startR + (end.r - startR) * percent;
        float g = startG + (end.g - startG) * percent;
        float b = startB + (end.b - startB) * percent;
        float a = startA + (end.a - startA) * percent;
        color.set(r, g, b, a);
    }

    @Override
    public void reset(){
        super.reset();
        color = null;
    }

    public Color getColor(){
        return color;
    }

    /**
     * Sets the color to modify. If null (the default), the {@link #getActor() actor's} {@link Element#getColor() color} will be
     * used.
     */
    public void setColor(Color color){
        this.color = color;
    }

    public void setStartColor(Color color){
        setStartColor(color.r, color.g, color.b, color.a);
    }
    public void resetStartColor(){
        hasStartColor=false;
    }

    public void setStartColor(float r, float g, float b, float a){
        hasStartColor = false;
        startR = r;
        startG = g;
        startB = b;
        startA = a;
    }

    public Color getEndColor(){
        return end;
    }
    /** Sets the color to transition to. Required. */
    public void setEndColor(Color color){
        end.set(color);
    }
}
