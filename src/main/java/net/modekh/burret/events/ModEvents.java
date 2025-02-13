package net.modekh.burret.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.modekh.burret.objects.blocks.entities.BurretBlockEntity;
import net.modekh.burret.registry.AttachmentRegistry;
import net.modekh.burret.registry.KeyRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber()
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
//        if (KeyRegistry.KEY_MENU_OPEN.consumeClick()) {
//            if (event.getEntity() instanceof Player player
//                    && event.getLevel().getBlockEntity(event.getPos()) instanceof BurretBlockEntity blockEntity) {
//                player.openMenu(blockEntity);
//            }
//        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState prevState = level.getBlockState(pos);

        InteractionHand hand = event.getHand();

        if (!(level.getBlockEntity(pos) instanceof BurretBlockEntity burretEntity)
                || hand != InteractionHand.MAIN_HAND || level.isClientSide) {
            return;
        }

        Player player = event.getEntity();

        ItemStack burretStack = burretEntity.getBurretStack();
        ItemStack playerStack = player.getItemInHand(hand);

        boolean isCrouching = player.isShiftKeyDown();

        if (burretStack.isEmpty()) {
            if (playerStack.getItem() instanceof ProjectileItem) {
                burretEntity.setBurretStack(isCrouching
                        ? playerStack : playerStack.copyWithCount(1));
                player.setItemInHand(hand, isCrouching
                        ? ItemStack.EMPTY : playerStack.copyWithCount(playerStack.getCount() - 1));

                player.displayClientMessage(Component.translatable("message.burret.projectile",
                        burretEntity.getBurretStack().getCount())
                        .withStyle(ChatFormatting.YELLOW), true);
            } else if (playerStack.getItem().equals(Items.AMETHYST_SHARD)) {
                burretEntity.setCatalystStack(playerStack.copyWithCount(1));

                player.setItemInHand(hand, playerStack.copyWithCount(playerStack.getCount() - 1));
            } else {
                return;
            }
        } else {

            burretEntity.setBurretStack(isCrouching
                    ? ItemStack.EMPTY : burretStack.copyWithCount(burretStack.getCount() - 1));
            player.addItem(isCrouching ? burretStack : burretStack.copyWithCount(1));

            player.displayClientMessage(Component.translatable("message.burret.projectile",
                            burretEntity.getBurretStack().getCount())
                    .withStyle(ChatFormatting.YELLOW), true);
            player.playSound(SoundEvents.ITEM_PICKUP);
        }

        level.sendBlockUpdated(pos, prevState, level.getBlockState(pos), 3);

        burretEntity.setChanged();
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal().hasData(AttachmentRegistry.STATUS)) {
            event.getEntity().setData(AttachmentRegistry.STATUS,
                    event.getOriginal().getData(AttachmentRegistry.STATUS));
        }
    }
}
