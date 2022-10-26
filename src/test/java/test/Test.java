package test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.serialize.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static final String TEST_FILE_PATH = "src/test/resources/test.yml";

    public static final List<Class<? extends YamlSerialize<?>>> serializers = Arrays.asList(
            SerializableItemStack.class,
            SerializableInventory.class,
            SerializableInteger.class,
            SerializablePrimitive.class,
            SerializableBoolean.class,
            SerializableList.class,
            SerializableString.class
    );
    @org.junit.Test
    public void testAllDeserialize() {
        for (Class<? extends YamlSerialize<?>> serializer : serializers) {
            try {
                deserialize(serializer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deserialize(Class<? extends YamlSerialize<?>> clazz) {
        try {
            YamlSerialize<?> yamlSerialize = clazz.newInstance();
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(TEST_FILE_PATH));
            ConfigurationSection section = conf.getConfigurationSection(clazz.getSimpleName());
            if (section == null) throw new AssertionError("section is null");
            section.getKeys(false).forEach(key -> {
                Object deserialize = yamlSerialize.deserialize(section.getConfigurationSection(key));
                System.out.println(deserialize);
            });
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void serialize(Class<? extends YamlSerialize<T>> clazz, T obj) {
        try {
            YamlSerialize<T> yamlSerialize = clazz.newInstance();
            Object serialize = yamlSerialize.serialize(obj);
            System.out.println(serialize);

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerMock server;
    private MyPlugin plugin;

    @Before
    public void setUp()
    {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(MyPlugin.class);
    }

    @After
    public void tearDown()
    {
        MockBukkit.unmock();
    }


}
