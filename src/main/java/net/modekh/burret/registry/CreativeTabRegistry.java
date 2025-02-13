package net.modekh.burret.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.modekh.burret.utils.Reference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BURRET_TAB = CREATIVE_MODE_TABS.register("burret_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.burret"))
            .icon(() -> BlockRegistry.BURRET.get().asItem().getDefaultInstance())
            .noScrollBar()
            .build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BURRET_TAB.getKey()) {
            for (DeferredHolder<Item, ? extends Item> item : ItemRegistry.ITEMS.getEntries()) {
                event.accept(item.get());
            }
        }
    }

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
