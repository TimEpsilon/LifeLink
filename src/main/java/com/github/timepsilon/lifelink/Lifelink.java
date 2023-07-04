package com.github.timepsilon.lifelink;

import com.github.timepsilon.lifelink.client.ClientProxy;
import com.github.timepsilon.lifelink.common.gui.TamedAnimalInventoryMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Lifelink.MODID)
public class Lifelink {

    private static Lifelink instance;
    public static Lifelink getInstance() {return instance; }

    public static final String MODID = "lifelink";
    public static final Logger LOGGER = LogUtils.getLogger();

    //public final RegistryObject<MenuType<TamedAnimalInventoryMenu>> tamedAnimalMenu;


    public Lifelink() {
        if (instance != null)
            throw new IllegalStateException("Lifelink initialized twice!");
        instance = this;


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        /*DeferredRegister<MenuType<?>> menus = DeferredRegister.create(ForgeRegistries.MENU_TYPES,MODID);
        menus.register(modEventBus);
        tamedAnimalMenu = menus.register("tamed_animal_menu",() -> new MenuType<>(TamedAnimalInventoryMenu::getClientMenu));

        if (FMLEnvironment.dist == Dist.CLIENT) ClientProxy.addClientListeners(modEventBus);*/
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Initializing Lifelink...");
    }

}
