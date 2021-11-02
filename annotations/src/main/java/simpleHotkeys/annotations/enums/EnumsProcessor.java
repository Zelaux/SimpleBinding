package simpleHotkeys.annotations.enums;

import arc.func.*;
import arc.struct.*;
import arc.util.*;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.*;
import jdk.javadoc.internal.doclets.toolkit.builders.*;
import mindustry.annotations.util.*;
import mma.annotations.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

@SupportedAnnotationTypes("simpleHotkeys.annotations.SHAnnotations.EnumConstructing")
public class EnumsProcessor extends ModBaseProcessor{

    @Override
    public void process(RoundEnvironment env) throws Exception{
        Seq<Stype> actions = new Seq<>();
        Seq<Stype> triggers = new Seq<>();
        for(Element rootElement : env.getRootElements()){
            if(rootElement instanceof TypeElement){
                Stype stype = new Stype((TypeElement)rootElement);
                if(stype.superclasses().contains(s -> s.fullName().equals("simpleHotkeys.type.ActionTrigger"))){
                    triggers.add(stype);
                }
                if(stype.superclasses().contains(s -> s.fullName().equals("simpleHotkeys.type.ActionWithTrigger"))){
                    actions.add(stype);
                }
            }
        }
        if (actions.isEmpty() && triggers.isEmpty()){
            Log.warn("Cannot find any triggers and actions");
        }
        build("ActionEnum",actions);
        build("TriggerEnum",triggers);
    }

    private void build(String name, Seq<Stype> stypes)throws Exception{
        Builder builder = TypeSpec.enumBuilder(name).addModifiers(Modifier.PUBLIC);

        for(Stype stype : stypes){

            builder.addEnumConstant(Strings.camelize(stype.name()), TypeSpec.anonymousClassBuilder("@::new,@.class".replace("@",stype.fullName())).build());
        }
        builder.addField(Prov.class,"reference",Modifier.PUBLIC,Modifier.FINAL);
        builder.addField(Class.class,"referenceType",Modifier.PUBLIC,Modifier.FINAL);
        builder.addMethod(MethodSpec.constructorBuilder()
        .addParameter(Prov.class,"reference")
        .addParameter(Class.class,"referenceType")
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
