package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

@YamlSymbol(symbol = "inventories")
public class SerializableInventory implements YamlSerialize<Inventory> {
    @Override
    public Inventory deserialize(ConfigurationSection conf) {
        throw new NotImplementedException();
    }

    @Override
    public ConfigurationSection serialize(Inventory inventory) {
        throw new NotImplementedException();
    }
}
