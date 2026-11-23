package com.cogworks.dungeonsport.registry;

import com.cogworks.dungeonsport.DungeonsPort;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = DungeonsPort.MODID)
public class DPCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("dungeonsport")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                    .then(Commands.literal("souls")
                        .then(Commands.literal("get")
                            .executes(ctx -> getSouls(
                                ctx.getSource(),
                                EntityArgument.getPlayers(ctx, "targets")
                            ))
                        )
                        .then(Commands.literal("set")
                            .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
                                .executes(ctx -> setSouls(
                                    ctx.getSource(),
                                    EntityArgument.getPlayers(ctx, "targets"),
                                    FloatArgumentType.getFloat(ctx, "amount")
                                ))
                            )
                        )
                        .then(Commands.literal("add")
                            .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                .executes(ctx -> addSouls(
                                    ctx.getSource(),
                                    EntityArgument.getPlayers(ctx, "targets"),
                                    FloatArgumentType.getFloat(ctx, "amount")
                                ))
                            )
                        )
                    )
                    .then(Commands.literal("max_souls")
                        .then(Commands.literal("get")
                            .executes(ctx -> getMaxSouls(
                                ctx.getSource(),
                                EntityArgument.getPlayers(ctx, "targets")
                            ))
                        )
                        .then(Commands.literal("set")
                            .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(ctx -> setMaxSouls(
                                    ctx.getSource(),
                                    EntityArgument.getPlayers(ctx, "targets"),
                                    IntegerArgumentType.getInteger(ctx, "amount")
                                ))
                            )
                        )
                    )
                    .then(Commands.literal("dynamo_cache")
                        .then(Commands.literal("get")
                            .executes(ctx -> getDynamoCache(
                                ctx.getSource(),
                                EntityArgument.getPlayers(ctx, "targets")
                            ))
                        )
                        .then(Commands.literal("set")
                            .then(Commands.argument("amount", IntegerArgumentType.integer(0, DPAttachments.MAX_DYNAMO_CACHE))
                                .executes(ctx -> setDynamoCache(
                                    ctx.getSource(),
                                    EntityArgument.getPlayers(ctx, "targets"),
                                    IntegerArgumentType.getInteger(ctx, "amount")
                                ))
                            )
                        )
                        .then(Commands.literal("add")
                            .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(ctx -> addDynamoCache(
                                    ctx.getSource(),
                                    EntityArgument.getPlayers(ctx, "targets"),
                                    IntegerArgumentType.getInteger(ctx, "amount")
                                ))
                            )
                        )
                    )
                )
        );
    }

    private static int getSouls(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            float value = DPAttachments.getSouls(player);
            source.sendSuccess(
                () -> Component.literal(player.getScoreboardName() + " has " + value + " souls"),
                false
            );
        }
        return targets.size();
    }

    private static int setSouls(CommandSourceStack source, Collection<ServerPlayer> targets, float amount) {
        for (ServerPlayer player : targets) {
            DPAttachments.setSouls(player, amount);
        }
        source.sendSuccess(
            () -> Component.literal("Set souls to " + amount + " for " + targets.size() + " player(s)"),
            true
        );
        return targets.size();
    }

    private static int addSouls(CommandSourceStack source, Collection<ServerPlayer> targets, float amount) {
        for (ServerPlayer player : targets) {
            DPAttachments.addSouls(player, amount);
        }
        source.sendSuccess(
            () -> Component.literal("Added " + amount + " souls to " + targets.size() + " player(s)"),
            true
        );
        return targets.size();
    }

    private static int getMaxSouls(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            int value = DPAttachments.getMaxSouls(player);
            source.sendSuccess(
                () -> Component.literal(player.getScoreboardName() + " has max_souls = " + value),
                false
            );
        }
        return targets.size();
    }

    private static int setMaxSouls(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) {
        for (ServerPlayer player : targets) {
            DPAttachments.setMaxSouls(player, amount);
        }
        source.sendSuccess(
            () -> Component.literal("Set max_souls to " + amount + " for " + targets.size() + " player(s)"),
            true
        );
        return targets.size();
    }

    private static int getDynamoCache(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            int value = DPAttachments.getDynamoCache(player);
            source.sendSuccess(
                () -> Component.literal(player.getScoreboardName() + " has dynamo_cache = " + value),
                false
            );
        }
        return targets.size();
    }

    private static int setDynamoCache(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) {
        for (ServerPlayer player : targets) {
            DPAttachments.setDynamoCache(player, amount);
        }
        source.sendSuccess(
            () -> Component.literal("Set dynamo_cache to " + amount + " for " + targets.size() + " player(s)"),
            true
        );
        return targets.size();
    }

    private static int addDynamoCache(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) {
        for (ServerPlayer player : targets) {
            DPAttachments.addDynamoCache(player, amount);
        }
        source.sendSuccess(
            () -> Component.literal("Added " + amount + " to dynamo_cache for " + targets.size() + " player(s)"),
            true
        );
        return targets.size();
    }
}