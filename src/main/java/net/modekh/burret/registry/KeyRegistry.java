package net.modekh.burret.registry;

import net.minecraft.client.KeyMapping;
import net.modekh.burret.utils.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegistry {
    public static final String CATEGORY = "Burret";

    public static final KeyMapping KEY_STATUS_SWITCH =
            new KeyMapping("key.cheaks.status_switch", GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyMapping KEY_MODE_SWITCH =
            new KeyMapping("key.cheaks.mode_switch", GLFW.GLFW_KEY_R, CATEGORY);
    public static final KeyMapping KEY_MENU_OPEN =
            new KeyMapping("key.cheaks.menu_open", GLFW.GLFW_KEY_G, CATEGORY);

    @SubscribeEvent
    public static void onKeybindingRegistry(RegisterKeyMappingsEvent event) {
        event.register(KEY_STATUS_SWITCH);
        event.register(KEY_MODE_SWITCH);
        event.register(KEY_MENU_OPEN);
    }
}
