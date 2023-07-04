package com.github.timepsilon.lifelink.common.gui;

import com.github.timepsilon.lifelink.Lifelink;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class TamedAnimalInventoryMenu extends AbstractContainerMenu {

    private final LivingEntity animalEntity;

    public TamedAnimalInventoryMenu(int pContainerId, Inventory playerInventory, Container container, LivingEntity animalEntity) {
        //super(Lifelink.getInstance().tamedAnimalMenu.get(), pContainerId);
        super(null,pContainerId);
        this.animalEntity = animalEntity;
        container.startOpen(playerInventory.player);

        // Head Slot
        this.addSlot(new Slot(container,0,1,1) {
            /**
             * Checks if the item is allowed to be placed
             */
            public boolean mayPlace(ItemStack item) {
                return item.getItem() instanceof ArmorItem && !this.hasItem() && ((ArmorItem) item.getItem()).getSlot().equals(EquipmentSlot.HEAD);
            }
        });

        // Chestplate Slot
        this.addSlot(new Slot(container,0,2,2) {
            /**
             * Checks if the item is allowed to be placed
             */
            public boolean mayPlace(ItemStack item) {
                return item.getItem() instanceof ArmorItem && !this.hasItem() && ((ArmorItem) item.getItem()).getSlot().equals(EquipmentSlot.CHEST);
            }
        });

        // Leggings Slot
        this.addSlot(new Slot(container,0,3,3) {
            /**
             * Checks if the item is allowed to be placed
             */
            public boolean mayPlace(ItemStack item) {
                return item.getItem() instanceof ArmorItem && !this.hasItem() && ((ArmorItem) item.getItem()).getSlot().equals(EquipmentSlot.LEGS);
            }
        });

        // Boots Slot
        this.addSlot(new Slot(container,0,4,4) {
            /**
             * Checks if the item is allowed to be placed
             */
            public boolean mayPlace(ItemStack item) {
                return item.getItem() instanceof ArmorItem && !this.hasItem() && ((ArmorItem) item.getItem()).getSlot().equals(EquipmentSlot.FEET);
            }
        });

        // Player Inventory
        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        // Player Hotbar
        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        //return animalEntity.isAlive() && animalEntity.isTame() && animalEntity.getOwner().equals(player) && animalEntity.distanceTo(player) < 8.0F;
        return animalEntity.isAlive() && animalEntity.distanceTo(player) < 8.0F;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public static TamedAnimalInventoryMenu getClientMenu(int id, Inventory inventory) {
        return new TamedAnimalInventoryMenu(id, inventory, new SimpleContainer(4), inventory.player);
    }

    public static MenuProvider getServerMenuProvider(TamableAnimal animal) {
        return new SimpleMenuProvider((id,inv,player) -> new TamedAnimalInventoryMenu(id,inv, new SimpleContainer(4),animal), animal.getDisplayName());
    }

}
