package com.github.timepsilon.lifelink.common.events;

import com.github.timepsilon.lifelink.Lifelink;
import com.github.timepsilon.lifelink.common.gui.TamedAnimalInventoryMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;

@Mod.EventBusSubscriber(modid= Lifelink.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class animalEquip {

    @SubscribeEvent
    public static void onAnimalRightClick(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (event.getTarget() instanceof TamableAnimal animal) {
            if (animal.isTame() && Objects.equals(animal.getOwner(), player)) {
                if (player.isCrouching()) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);

                    if (player instanceof ServerPlayer serverPlayer) {
                        NetworkHooks.openScreen(serverPlayer, TamedAnimalInventoryMenu.getServerMenuProvider(animal));
                    }

                } else {
                    ItemStack item = event.getItemStack();
                    if (item.getItem() instanceof ArmorItem armorItem) {
                        EquipmentSlot slot = armorItem.getSlot();

                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);

                        ItemStack oldArmor = animal.getItemBySlot(slot);

                        animal.setItemSlot(slot,item);
                        animal.setDropChance(slot,1);
                        if (oldArmor.getItem() instanceof ArmorItem) {
                            player.setItemInHand(event.getHand(),oldArmor);
                        } else {
                            player.setItemInHand(event.getHand(),ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
}
