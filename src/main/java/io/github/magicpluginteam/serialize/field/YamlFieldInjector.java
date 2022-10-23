package io.github.magicpluginteam.serialize.field;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;

public class YamlFieldInjector {

    public static void inject(Object obj, File file) {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        for (Field field : obj.getClass().getDeclaredFields()) {
            YamlField annotation = field.getAnnotation(YamlField.class);
            if (annotation == null) {
                continue;
            }
            try {
                String name = field.getName();
                ConfigurationSection section;
                if (conf.isConfigurationSection(name)) {
                    section = conf.getConfigurationSection(name);
                } else {
                    section = new YamlConfiguration();
                    section.set("_", conf.get(name));
                }
                var serialize = annotation.serializable().newInstance();
                field.setAccessible(true);
                field.set(obj, serialize.deserialize(section));
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                throw new AssertionError("an error occurred while inject field");
            }
        }
    }


}
