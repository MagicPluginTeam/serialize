package io.github.magicpluginteam.serialize.tree;

import io.github.magicpluginteam.serialize.YamlSection;
import io.github.magicpluginteam.serialize.YamlSerialize;
import io.github.magicpluginteam.serialize.symbol.YamlSymbol;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YamlTreeUtils {

    public static void recursionFiles(List<File> list, File base, int start, int limit) {
        if (limit != -1 && start >= limit) return;
        File[] files = base.listFiles();
        if (files == null) return;
        for (var file : files) {
            if (file.isFile()) {
                list.add(file);
            } else if (file.isDirectory()) {
                recursionFiles(list, file, start+1, limit);
            }
        }
    }

    public static List<String> recursionFiles(File file, int limit) {
        var list = new ArrayList<File>();
        recursionFiles(list, file, 0, limit);
        return list.stream().map(f -> file.toPath().relativize(f.toPath()).toString()).toList();
    }

    private static HashMap<String, YamlSerialize<?>> getSymbolicTable(Class<? extends YamlSerialize<?>>[] serializable) {
        var symbolicTable = new HashMap<String, YamlSerialize<?>>();
        for (var clazz : serializable) {
            YamlSymbol annotation = clazz.getAnnotation(YamlSymbol.class);
            if (annotation == null) {
                throw new AssertionError(clazz.getSimpleName() + " does not have YamlSerializable annotation");
            }
            try {
                String symbol = annotation.symbol();
                if (symbolicTable.getOrDefault(symbol, null) != null) {
                    throw new AssertionError(symbol + " symbol is already exists");
                }
                symbolicTable.put(symbol, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new AssertionError("an error occurred while instancing " + clazz.getSimpleName() + " serializer");
            }

        }
        return symbolicTable;
    }

    @SafeVarargs
    public static HashMap<String, YamlSection<?>> loadYamlTree(File base, Class<? extends YamlSerialize<?>>...fileSymbolTable) {
        var symbolicTable = getSymbolicTable(fileSymbolTable);
        var sectionMap = new HashMap<String, YamlSection<?>>();
        for (String f : recursionFiles(base, -1)) {
            File file = new File(base, f);
            String fileName = file.getName();
            String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
            YamlSerialize<?> serializable = symbolicTable.getOrDefault(nameWithoutExtension, null);
            YamlSection<?> section = new YamlSection<>(serializable);
            section.deserialize(YamlConfiguration.loadConfiguration(file));
            sectionMap.put(f, section);
        }
        return sectionMap;
    }

}
