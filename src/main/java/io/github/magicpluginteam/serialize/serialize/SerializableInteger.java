package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@YamlSymbol(symbol = "integer")
public class SerializableInteger implements YamlSerialize<Integer> {
    @Override
    public Integer deserialize(ConfigurationSection conf) {
        var name = "_";
        return conf.getInt(name);
    }

    @Override
    public ConfigurationSection serialize(Integer o) {
        var section = new YamlConfiguration();
        section.set("_", o);
        return section;
    }
}
