package net.modekh.burret.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.modekh.burret.objects.blocks.BurretBlock;
import net.modekh.burret.objects.blocks.entities.BurretBlockEntity;
import net.modekh.burret.utils.Reference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
    // blocks
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);

    public static final DeferredHolder<Block, BurretBlock> BURRET =
            BLOCKS.register("burret", () -> new BurretBlock(BlockBehaviour.Properties.of()));

    // block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BurretBlockEntity>> BURRET_ENTITY =
            BLOCK_ENTITIES.register("burret", () ->
                    BlockEntityType.Builder.of(BurretBlockEntity::new, BURRET.get()).build(null));

    // registry
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);

        for (DeferredHolder<? extends Block, ? extends Block> block : BLOCKS.getEntries()) {
            ItemRegistry.ITEMS.register(block.getId().getPath(),
                    () -> new BlockItem(block.get(), new Item.Properties()))    ;
        }
    }
}
