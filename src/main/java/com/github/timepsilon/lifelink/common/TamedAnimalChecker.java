package com.github.timepsilon.lifelink.common;

import com.github.timepsilon.lifelink.Lifelink;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= Lifelink.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TamedAnimalChecker {

    private static final int INTERVAL = 100;
    private static final AttributeModifier damageModifier = new AttributeModifier("modified_attack_damage",-0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final AttributeModifier healthModifier = new AttributeModifier("modified_max_health",-0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final AttributeModifier armorModifier = new AttributeModifier("modified_armor",-0.9, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level.isClientSide()) {
            if (event.player.level.getGameTime() % INTERVAL == 0) {
                checkNearbyAnimals(event.player);
            }
        }
    }

    private static void checkNearbyAnimals(Player player) {
        boolean found = false;

        for (TamableAnimal entity : player.level.getEntitiesOfClass(TamableAnimal.class,player.getBoundingBox().inflate(64))) {
            if (entity.isTame() && entity.getOwner() == player) {
                found = true;
                break;
            }
        }

        if (found) {
            if (player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(damageModifier))
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(damageModifier);

            if (player.getAttribute(Attributes.ARMOR).hasModifier(armorModifier))
                player.getAttribute(Attributes.ARMOR).removeModifier(armorModifier);

            if (player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthModifier))
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthModifier);

        } else {

            if (!player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(damageModifier))
                player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(damageModifier);

            if (!player.getAttribute(Attributes.ARMOR).hasModifier(armorModifier))
                player.getAttribute(Attributes.ARMOR).addTransientModifier(armorModifier);

            if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthModifier)) {
                player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthModifier);
                player.setHealth(player.getMaxHealth());
            }

            Component warning = Component.literal("§c (!) No tamed animal found!");
            player.displayClientMessage(warning,true);
            player.playSound(SoundEvents.GOAT_AMBIENT,1,0.5f);
        }
    }

    @SubscribeEvent
    public static void onAnimalDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof TamableAnimal animal) {
            if (animal.isTame() && animal.getOwner() instanceof ServerPlayer player) {
                if (animal.getServer().getPlayerList().getPlayer(player.getUUID()) != null) {
                    player.kill();

                    Component deathMessage = Component.literal("§c " + animal.getDisplayName().getString() + " died!");
                    ClientboundSetTitleTextPacket packet = new ClientboundSetTitleTextPacket(deathMessage);
                    player.connection.send(packet);
                }
            }
        }
    }
}
