package com.github.timepsilon.lifelink.common.events;

import com.github.timepsilon.lifelink.Lifelink;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= Lifelink.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LifelinkListener {
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && !event.getSource().equals(DamageSource.OUT_OF_WORLD)) {
            killEveryone(player);
        }
    }

    public static void killEveryone(Player player) {
        for (ServerPlayer p : player.getServer().getPlayerList().getPlayers()) {
            p.kill();

            Component deathMessage = Component.literal("§4 " + player.getDisplayName().getString() + " died!");
            ClientboundSetTitleTextPacket packet = new ClientboundSetTitleTextPacket(deathMessage);
            p.connection.send(packet);
            p.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT,2,0.5f);
        }
    }
}
