package simpleHotkeys.annotations;

import java.lang.annotation.*;

public class SHAnnotations{
    /**
     * needs for generate enum with Triggers and Actions
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface RegisterAction{
        /** RegisterAction name in enum list */
        String value();
    }
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface RegisterTrigger{
        /** RegisterTrigger name in enum list */
        String value();
    }
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface RootAction{

    }
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface RootTrigger{
    }
}