package test;

import io.github.magicpluginteam.serialize.field.YamlField;
import io.github.magicpluginteam.serialize.field.YamlFieldInjector;
import io.github.magicpluginteam.serialize.serialize.SerializableItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Yaml {

    @YamlField(SerializableItemStack.class)
    public ItemStack item;

    public Yaml() {
        File file = new File("src/test/resources/test.yml");
        YamlFieldInjector.inject(this, file);
    }
}
