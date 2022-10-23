package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@YamlSymbol(symbol = "boolean")
public class SerializableBoolean implements YamlSerialize<Boolean> {
    @Override
    public Boolean deserialize(ConfigurationSection conf) {
        var name = "_";
        return conf.getBoolean(name);
    }

    @Override
    public ConfigurationSection serialize(Boolean o) {
        var section = new YamlConfiguration();
        section.set("_", o);
        return section;
    }
}
