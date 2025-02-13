//package net.modekh.burret.client.screen;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.*;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.modekh.burret.objects.blocks.entities.BurretBlockEntity;
//import net.modekh.burret.registry.BlockRegistry;
//import net.modekh.burret.registry.MenuTypesRegistry;
//import net.neoforged.neoforge.items.SlotItemHandler;
//import org.jetbrains.annotations.NotNull;
//
//public class BurretMenu extends AbstractContainerMenu {
//    public final BurretBlockEntity blockEntity;
//    private final Level level;
//    private final ContainerData data;
//
//    public BurretMenu(int containerId, Inventory playerInventory, BlockEntity blockEntity) {
//        this(containerId, playerInventory, blockEntity, new SimpleContainerData(2));
//    }
//
//    public BurretMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
//        this(containerId, playerInventory,
//                playerInventory.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(2));
//    }
//
//    public BurretMenu(int containerId, Inventory playerInventory, BlockEntity entity, ContainerData data) {
//        super(MenuTypesRegistry.BURRET_MENU.get(), containerId);
//
//        checkContainerSize(playerInventory, 2);
//
//        this.blockEntity = ((BurretBlockEntity) entity);
//        this.level = playerInventory.player.level();
//        this.data = data;
//
//        addPlayerInventory(playerInventory);
//
////        this.blockEntity.capCache.getCapability()
//
//        this.addSlot(new SlotItemHandler(this.blockEntity.lazyStackHandler.get(),
//                0, 77, 16));
//        this.addSlot(new SlotItemHandler(this.blockEntity.lazyStackHandler.get(),
//                0, 77, 52));
//
//        addDataSlots(data);
//    }
//
//    private static final int HOTBAR_SLOT_COUNT = 9;
//    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
//    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
//    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
//    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
//    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
//    private static final int PROJECTILE_SLOT = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
//
//    private static final int SLOTS_NUM = 2;
//
//    @Override
//    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
//        Slot sourceSlot = slots.get(index);
//
//        if (!sourceSlot.hasItem()) {
//            return ItemStack.EMPTY;
//        }
//
//        ItemStack sourceStack = sourceSlot.getItem();
//        ItemStack copyOfSourceStack = sourceStack.copy();
//
//        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
//            if (!moveItemStackTo(sourceStack, PROJECTILE_SLOT, PROJECTILE_SLOT
//                    + SLOTS_NUM, false)) {
//                return ItemStack.EMPTY;
//            }
//        } else if (index < PROJECTILE_SLOT + SLOTS_NUM) {
//            // This is a TE slot so merge the stack into the players inventory
//            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
//                return ItemStack.EMPTY;
//            }
//        } else {
//            return ItemStack.EMPTY;
//        }
//
//        if (sourceStack.getCount() == 0) {
//            sourceSlot.set(ItemStack.EMPTY);
//        } else {
//            sourceSlot.setChanged();
//        }
//
//        sourceSlot.onTake(player, sourceStack);
//
//        return copyOfSourceStack;
//    }
//
//    @Override
//    public boolean stillValid(@NotNull Player player) {
//        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
//                player, BlockRegistry.BURRET.get());
//    }
//
//    private void addPlayerInventory(Inventory playerInventory) {
//        for (int i = 0; i < 3; ++i) {
//            for (int l = 0; l < 9; ++l) {
//                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
//            }
//        }
//
//        // hotbar
//        for (int i = 0; i < 9; ++i) {
//            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
//        }
//    }
//}
