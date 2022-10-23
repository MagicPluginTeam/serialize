package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

@YamlSymbol(symbol = "list")
public class SerializableList implements YamlSerialize<List<String>> {
    @Override
    public List<String> deserialize(ConfigurationSection conf) {
        var name = "_";
        return conf.getStringList(name);
    }

    @Override
    public ConfigurationSection serialize(List<String> o) {
        var section = new YamlConfiguration();
        section.set("_", o);
        return section;
    }
}
