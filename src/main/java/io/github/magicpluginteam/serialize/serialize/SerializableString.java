package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import io.github.magicpluginteam.serialize.utils.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

@YamlSymbol(symbol = "string")
public class SerializableString implements YamlSerialize<String> {
    @Override
    public String deserialize(ConfigurationSection conf) {
        var name = "_";
        return conf.getString(name);
    }

    @Override
    public ConfigurationSection serialize(String o) {
        var section = new YamlConfiguration();
        section.set("_", o);
        return section;
    }
}
