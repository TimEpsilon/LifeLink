package com.github.timepsilon.lifelink.client;

import com.github.timepsilon.lifelink.Lifelink;
import com.github.timepsilon.lifelink.common.gui.TamedAnimalInventoryScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy {

    /*public static void addClientListeners(IEventBus modBus)
    {
        modBus.addListener(ClientProxy::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(
                () -> MenuScreens.register(Lifelink.getInstance().tamedAnimalMenu.get(), TamedAnimalInventoryScreen::new)
        );
    }*/
}
