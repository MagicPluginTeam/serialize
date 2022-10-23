package io.github.magicpluginteam.serialize;

import org.bukkit.configuration.ConfigurationSection;

public interface YamlSerialize<T> {

    T deserialize(ConfigurationSection conf);

    ConfigurationSection serialize(T t);

}

