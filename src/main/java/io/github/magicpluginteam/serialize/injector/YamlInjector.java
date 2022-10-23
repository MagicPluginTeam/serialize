package io.github.magicpluginteam.serialize.injector;

import io.github.magicpluginteam.serialize.YamlSection;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import io.github.magicpluginteam.serialize.tree.YamlTreeUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * You can inject yaml hashmap with serializer
 *
 * example:
 * \@YamlFile(serializable = SomeSerializer.class)
 * public YamlSection<Type> config;
 * \@YamlFile(serializable = ItemSerializer.class, relative = "lang")
 * public HashMap<String, YamlSection<Item>> items;
 *
 */
public class YamlInjector {

    public static void inject(Object obj, File root) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            YamlFile annotation = field.getAnnotation(YamlFile.class);
            if (annotation == null) {
                continue;
            }
            var serializeClass = annotation.serializable();
            YamlSymbol symbolAnnotation = serializeClass.getAnnotation(YamlSymbol.class);
            if (symbolAnnotation == null) {
                throw new AssertionError(serializeClass.getSimpleName() + " does not have YamlSymbol annotation");
            }
            String symbol = symbolAnnotation.symbol();
            try {
                if (field.getType().isAssignableFrom(HashMap.class)) {
                    var map = field.getType().newInstance();
                    field.set(obj, map);
                    var yamlMap = ((Map<String, Object>) map);
                    File base = new File(root, annotation.relative());
                    List<String> paths = YamlTreeUtils.recursionFiles(base, 2);
                    for (var path : paths) {
                        File file = new File(base, path);
                        String fileName = file.getName();
                        int endIndex = fileName.lastIndexOf(".");
                        if (endIndex == -1) continue;
                        String nameWithoutExtension = fileName.substring(0, endIndex);
                        if (nameWithoutExtension.equals(symbol)) {
                            YamlSection<?> section = new YamlSection<>(serializeClass.newInstance());
                            section.deserialize(YamlConfiguration.loadConfiguration(file));
                            yamlMap.put(file.getParentFile().getName(), section);
                        }
                    }
                } else {
                    YamlSection<?> section = new YamlSection<>(serializeClass.newInstance());
                    field.set(obj, section);
                    section.deserialize(YamlConfiguration.loadConfiguration(new File(root, symbol + ".yml")));
                }
            } catch (IllegalAccessException | InstantiationException e) {
                throw new AssertionError("an error occurred while inject field");
            }
        }
    }


}
