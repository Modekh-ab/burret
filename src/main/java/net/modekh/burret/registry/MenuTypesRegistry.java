//package net.modekh.burret.registry;
//
//import net.minecraft.core.registries.Registries;
//import net.minecraft.world.inventory.MenuType;
//import net.modekh.burret.client.screen.BurretMenu;
//import net.modekh.burret.utils.Reference;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//public class MenuTypesRegistry {
//    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
//            DeferredRegister.create(Registries.MENU, Reference.MOD_ID);
//
//    public static final DeferredHolder<MenuType<?>, MenuType<BurretMenu>> BURRET_MENU =
//            MENU_TYPES.register("burret_menu", () -> IMenuTypeExtension.create(BurretMenu::new));
//
//    public static void register(IEventBus bus) {
//        MENU_TYPES.register(bus);
//    }
//}
