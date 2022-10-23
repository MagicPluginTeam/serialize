package io.github.magicpluginteam.serialize;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public class YamlSection<T> {

    private final YamlSerialize<T> sectionSerializable;
    private final HashMap<String, T> map = new HashMap<>();
    private YamlSection<T> parent;

    public YamlSection(YamlSerialize<T> sectionSerializable) {
        this.sectionSerializable = sectionSerializable;
    }

    public T get(String name) {
        T t = map.getOrDefault(name, null);
        if (t == null && parent != null) return parent.get(name);
        return t;
    }

    public void deserialize(ConfigurationSection conf) {
        for (var key : conf.getKeys(false)) {
            ConfigurationSection section;
            if (conf.isConfigurationSection(key)) {
                section = conf.getConfigurationSection(key);
            } else {
                section = new YamlConfiguration();
                section.set("_", conf.get(key));
            }
            map.put(key, sectionSerializable.deserialize(section));
        }
    }

    public YamlConfiguration serialize() {
        var section = new YamlConfiguration();
        for (var entry : map.entrySet()) {
            var sect = sectionSerializable.serialize(entry.getValue());
            if (sect.isSet("_"))
                section.set(entry.getKey(), sect.get("_"));
            else section.set(entry.getKey(), sect);
        }
        return section;
    }


}
