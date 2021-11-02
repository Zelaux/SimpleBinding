package simpleHotkeys.annotations;

import java.lang.annotation.*;

public class SHAnnotations{
    /**
     * needs for generate enum with Triggers and Actions*/
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface EnumConstructing{

    }
}