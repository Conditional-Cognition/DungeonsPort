package com.cogworks.dungeonsport.registry;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DPAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "dungeonsport");

    public static final Supplier<AttachmentType<Float>> SOULS = ATTACHMENT_TYPES.register(
        "souls",
        () -> AttachmentType.builder(() -> 0.0f)
            .serialize(Codec.FLOAT)
            .sync((holder, to) -> true, ByteBufCodecs.FLOAT)
            .build()
    );

    public static final Supplier<AttachmentType<Integer>> MAX_SOULS = ATTACHMENT_TYPES.register(
        "max_souls",
        () -> AttachmentType.builder(() -> 300)
            .serialize(Codec.INT)
            .sync((holder, to) -> true, ByteBufCodecs.VAR_INT)
            .build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

    public static float getSouls(Player player) {
        return player.getData(SOULS);
    }

    public static int getMaxSouls(Player player) {
        return player.getData(MAX_SOULS);
    }

    public static void setSouls(Player player, float amount) {
        float safeAmount = Float.isFinite(amount) ? amount : 0.0f;
        float clamped = Math.max(0.0f, Math.min(safeAmount, getMaxSouls(player)));
        player.setData(SOULS, clamped);
    }

    public static void setMaxSouls(Player player, int amount) {
        int clamped = Math.max(0, amount);
        player.setData(MAX_SOULS, clamped);
        if (getSouls(player) > clamped) {
            setSouls(player, clamped);
        }
    }

    public static void addSouls(Player player, float amount) {
        setSouls(player, getSouls(player) + amount);
    }
}