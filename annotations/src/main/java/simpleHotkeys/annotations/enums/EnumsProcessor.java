package simpleHotkeys.annotations.enums;

import arc.func.*;
import arc.struct.*;
import arc.util.*;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.*;
import mindustry.annotations.util.*;
import mma.annotations.*;
import simpleHotkeys.annotations.SHAnnotations.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

@SupportedAnnotationTypes({"simpleHotkeys.annotations.SHAnnotations.RegisterAction", "simpleHotkeys.annotations.SHAnnotations.RegisterTrigger"})
public class EnumsProcessor extends ModBaseProcessor{

    @Override
    public void process(RoundEnvironment env) throws Exception{
        Seq<Stype> actions = new Seq<>();
        Seq<Stype> triggers = new Seq<>();
        Seq<Stype> actionsToRegister = types(RegisterAction.class);
        Stype rootAction=types(RootAction.class).first();
        for(Stype type : actionsToRegister){
            if(type.superclasses().contains(s -> s.fullName().equals(rootAction.fullName()))){
                actions.add(type);
            } else {
                Log.err("action @ was ignored",type.fullName());
            }
        }
        Seq<Stype> triggerToRegister = types(RegisterTrigger.class);
        Stype rootTrigger=types(RootTrigger.class).first();
        for(Stype type : triggerToRegister){
            if(type.superclasses().contains(s -> s.fullName().equals(rootTrigger.fullName()))){
                triggers.add(type);
            } else {
                Log.err("action @ was ignored",type.fullName());
            }
        }
        if(actions.isEmpty() && triggers.isEmpty()){
            Log.warn("Cannot find any triggers and actions");
        }
        build("ActionList", actions, type->type.annotation(RegisterAction.class).value());
        build("TriggerList", triggers, type->type.annotation(RegisterTrigger.class).value());
    }

    private void build(String name, Seq<Stype> stypes, Func<Stype,String> nameTransformer) throws Exception{
        Builder builder = TypeSpec.enumBuilder(name).addModifiers(Modifier.PUBLIC);

        for(Stype stype : stypes.sort(Structs.comparing(nameTransformer))){
            builder.addEnumConstant(Strings.camelize(nameTransformer.get(stype)), TypeSpec.anonymousClassBuilder("@::new,@.class".replace("@", stype.fullName())).build());
        }
        builder.addField(Prov.class, "reference", Modifier.PUBLIC, Modifier.FINAL);
        builder.addField(Class.class, "referenceType", Modifier.PUBLIC, Modifier.FINAL);
        builder.addMethod(MethodSpec.constructorBuilder()
        .addParameter(Prov.class, "reference")
        .addParameter(Class.class, "referenceType")
        .addStatement("this.reference=reference")
        .addStatement("this.referenceType=referenceType")
        .build());

        builder.addMethod(MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC)
        .addTypeVariable(TypeVariableName.get("T"))
        .returns(ClassName.bestGuess("T"))
        .addStatement("return (T)reference.get()")
        .build());

        write(builder);


    }
}
