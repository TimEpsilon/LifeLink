package com.github.timepsilon.lifelink.common.gui;

import com.github.timepsilon.lifelink.Lifelink;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TamedAnimalInventoryScreen extends AbstractContainerMenu {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Lifelink.MODID, "textures/gui/animal_inventory.png");

    private final TamableAnimal animalEntity;
    private final Player player;

    protected TamedAnimalInventoryScreen(@Nullable MenuType<?> pMenuType, int pContainerId, TamableAnimal animalEntity, Player player) {
        super(pMenuType, pContainerId);

        this.animalEntity = animalEntity;
        this.player = player;
    }



    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}
