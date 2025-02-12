package net.modekh.burret.registry;

import net.modekh.burret.utils.Reference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
