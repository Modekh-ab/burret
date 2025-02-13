//package net.modekh.burret.registry;
//
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.MobCategory;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.modekh.burret.objects.blocks.entities.BurretBlockEntity;
//import net.modekh.burret.objects.items.entities.Ballet;
//import net.modekh.burret.utils.Reference;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//public class EntityTypesRegistry {
//
//    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
//            DeferredRegister.create(Registries.ENTITY_TYPE, Reference.MOD_ID);
//
//    public static final DeferredHolder<EntityType<?>, EntityType<Ballet>> BALLET_TYPE =
//            ENTITY_TYPES.register("ballet", () ->
//                    EntityType.Builder.of(Ballet::new, MobCategory.MISC)
//                            .sized(1.0F, 1.0F)
//                            .build("ballet"));
//
//    public static void register(IEventBus bus) {
//        ENTITY_TYPES.register(bus);
//    }
//}
