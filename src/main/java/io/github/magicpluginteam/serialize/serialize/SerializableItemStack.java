package io.github.magicpluginteam.serialize.serialize;

import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import io.github.magicpluginteam.serialize.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

@YamlSymbol(symbol = "items")
public class SerializableItemStack implements YamlSerialize<ItemStack> {
    @Override
    public ItemStack deserialize(ConfigurationSection conf) {
        ItemStack itemStack;
        if (conf.isSet("item")) {
            itemStack = conf.getItemStack("item");
            if (itemStack == null) {
                throw new AssertionError("item configuration is not valid");
            }
        } else {
            itemStack = new ItemStack(Material.AIR);
        }
        if (conf.isSet("type")) {
            String type = string(conf, "type");
            try {
                itemStack.setType(Material.valueOf(type));
            } catch (Exception e) {
                throw new AssertionError(type + " is not a material");
            }
        }
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        if (conf.isSet("amount")) {
            itemStack.setAmount(conf.getInt("amount"));
        }
        if (conf.isSet("name")) {
            itemMeta.setDisplayName(string(conf, "name"));
        }
        if (conf.isSet("lore")) {
            itemMeta.setLore(stringList(conf, "lore"));
        }
        if (conf.isSet("hide")) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        }
        if (conf.isSet("unbreak")) {
            itemMeta.setUnbreakable(true);
        }
        if (conf.isSet("data")) {
            MaterialData data = itemStack.getData();
            if (data == null)
                throw new AssertionError("a unknown error occurred while deserializing yaml item configuration");
            data.setData((byte)conf.getInt("data"));
        }
        if (conf.isSet("color")) {
            Color color;
            if (conf.isString("color")) {
                color = ColorUtils.hex2RGB(string(conf, "color"));
            } else {
                color = Color.fromRGB(
                        conf.getInt("color.R"),
                        conf.getInt("color.G"),
                        conf.getInt("color.B")
                );
            }
            ((LeatherArmorMeta) itemMeta).setColor(color);
        }
        if (conf.isSet("potion")) {
            PotionMeta potion = (PotionMeta) itemMeta;
            try {
                potion.setBasePotionData(new PotionData(PotionType.valueOf(string(conf, "potion"))));
            } catch (Exception e) {
                throw new AssertionError("an error occurred while read item potion configuration");
            }
        }
        if (conf.isSet("enchants")) {
            ConfigurationSection section = conf.getConfigurationSection("enchants");
            section.getKeys(false).forEach(k -> {
                try {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(k), section.getInt(k));
                } catch (Exception e) {
                    throw new AssertionError("an error occurred while read enchants item configuration");
                }
            });
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ConfigurationSection serialize(ItemStack itemStack) {
        throw new AssertionError("Not implemented Yet");
    }

    public static String string(ConfigurationSection section, String key) {
        String string = section.getString(key);
        if (string == null) return null;
        return ColorUtils.color(string);
    }

    public static List<String> stringList(ConfigurationSection section, String key) {
        List<String> stringList = section.getStringList(key);
        return stringList.stream().map(ColorUtils::color).toList();
    }

}
