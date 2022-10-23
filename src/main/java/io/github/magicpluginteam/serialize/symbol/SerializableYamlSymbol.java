package io.github.magicpluginteam.serialize.symbol;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.utils.ClassUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This is hashmap Symbol Serializer
 *
 * others-yaml-looks like:
 *   key:
 *     contents...
 *   key2:
 *     conetnts...
 *
 *
 * this-example:
 *   key.symbol-A:
 *     contentsA...
 *   key2.symbol-B:
 *     contentsB...
 *
 *  You can use multiple serializer in one yaml file with this
 */
public class SerializableYamlSymbol<T> implements YamlSerialize<T> {

    private final HashMap<String, YamlSerialize<T>> serializableMap = new HashMap<>();

    @SafeVarargs
    public SerializableYamlSymbol(Class<? extends YamlSerialize<? extends T>>...classes) {
        Arrays.stream(classes).forEach(this::bind);
    }

    public void bind(Class<? extends YamlSerialize<? extends T>> clazz) {
        YamlSymbol annotation = clazz.getAnnotation(YamlSymbol.class);
        if (annotation == null) {
            throw new AssertionError(clazz.getSimpleName() + " does not have YamlSymbol annotation");
        }
        try {
            String symbol = annotation.symbol();
            if (serializableMap.getOrDefault(symbol, null) != null) {
                throw new AssertionError(symbol + " symbol is already exists");
            }
            serializableMap.put(symbol, (YamlSerialize<T>) clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(ConfigurationSection conf) {
        String symbol = conf.getKeys(false).stream().findFirst().orElse(null);
        if (symbol == null)
            throw new AssertionError("symbol is not exists");
        var serializable = serializableMap.getOrDefault(symbol, null);
        if (serializable == null)
            throw new AssertionError(symbol + " symbol is not valid");
        ConfigurationSection section;
        if (conf.isConfigurationSection(symbol)) {
            section = conf.getConfigurationSection(symbol);
        } else {
            section = new YamlConfiguration();
            section.set("_", conf.get(symbol));
        }
        return serializable.deserialize(section);
    }

    @Override
    public ConfigurationSection serialize(T t) {
        Class<?> type = t.getClass();
        for (var entry : serializableMap.entrySet()) {
            Class<?> genericType = ClassUtils.getGenericType(entry.getValue().getClass());
            if (genericType.equals(type)) {
                YamlConfiguration section = new YamlConfiguration();
                section.set(entry.getKey(), entry.getValue().serialize(t));
                return section;
            }
        }
        throw new AssertionError(type.getSimpleName() + " does not match any serializer");
    }
}
