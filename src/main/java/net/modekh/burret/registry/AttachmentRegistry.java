package net.modekh.burret.registry;

import com.mojang.serialization.Codec;
import net.modekh.burret.utils.Reference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reference.MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> STATUS =
            ATTACHMENT_TYPES.register("status", () ->
                    AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
