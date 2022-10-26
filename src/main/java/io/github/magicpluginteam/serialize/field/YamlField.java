package io.github.magicpluginteam.serialize.field;

import io.github.magicpluginteam.serialize.YamlSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface YamlField {

    Class<? extends YamlSerialize<?>> value();

}
