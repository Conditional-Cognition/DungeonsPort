package com.cogworks.dungeonsport.math_and_helpers;

import com.cogworks.dungeonsport.DungeonsPort;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

public final class ItemObliteratorCompat {

    private static final List<String> DEFAULT_BLACKLIST = List.of(
        // Swords
        "minecraft:wooden_sword",
        "minecraft:stone_sword",
        "minecraft:iron_sword",
        "minecraft:golden_sword",
        "minecraft:diamond_sword",
        "minecraft:netherite_sword",
        // Axes
        "minecraft:wooden_axe",
        "minecraft:stone_axe",
        "minecraft:iron_axe",
        "minecraft:golden_axe",
        "minecraft:diamond_axe",
        "minecraft:netherite_axe",
        // Pickaxes
        "minecraft:wooden_pickaxe",
        "minecraft:stone_pickaxe",
        "minecraft:iron_pickaxe",
        "minecraft:golden_pickaxe",
        "minecraft:diamond_pickaxe",
        "minecraft:netherite_pickaxe",
        // Shovels
        "minecraft:wooden_shovel",
        "minecraft:stone_shovel",
        "minecraft:iron_shovel",
        "minecraft:golden_shovel",
        "minecraft:diamond_shovel",
        "minecraft:netherite_shovel",
        // Hoes
        "minecraft:wooden_hoe",
        "minecraft:stone_hoe",
        "minecraft:iron_hoe",
        "minecraft:golden_hoe",
        "minecraft:diamond_hoe",
        "minecraft:netherite_hoe",
        // Bows & Crossbows
        "minecraft:bow",
        "minecraft:crossbow"
    );

    private ItemObliteratorCompat() {
    }

    public static boolean injectBlacklistEntries() {
        if (!ModList.get().isLoaded("item_obliterator")) {
            return false;
        }

        Path configPath = FMLPaths.CONFIGDIR.get().resolve("item_obliterator.json5");
        if (!Files.exists(configPath)) {
            return false;
        }

        try {
            String content = Files.readString(configPath, StandardCharsets.UTF_8);
            List<String> missing = new ArrayList<>();
            for (String itemId : DEFAULT_BLACKLIST) {
                if (!content.contains("\"" + itemId + "\"")) {
                    missing.add(itemId);
                }
            }

            if (missing.isEmpty()) {
                return false;
            }

            Pattern pattern = Pattern.compile("(\"blacklisted_items\"\\s*:\\s*\\[)");
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                StringBuilder insertion = new StringBuilder(matcher.group(1)).append("\n");
                for (String id : missing) {
                    insertion.append("    \"").append(id).append("\",\n");
                }
                String updated = matcher.replaceFirst(Matcher.quoteReplacement(insertion.toString()));
                Files.writeString(configPath, updated, StandardCharsets.UTF_8);
                DungeonsPort.LOGGER.info("Added {} DungeonsPort blacklist entries to item_obliterator.json5.", missing.size());
                return true;
            }
        } catch (IOException e) {
            DungeonsPort.LOGGER.error("Failed to update item_obliterator.json5", e);
        }

        return false;
    }
}
