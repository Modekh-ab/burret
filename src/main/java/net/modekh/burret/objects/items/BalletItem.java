//package net.modekh.burret.objects.items;
//
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.stats.Stats;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.level.Level;
//import net.modekh.burret.objects.items.entities.Ballet;
//import net.modekh.burret.registry.EntityTypesRegistry;
//
//public class BalletItem extends Item {
//    public BalletItem(Properties properties) {
//        super(properties
//                .stacksTo(16)
//                .rarity(Rarity.UNCOMMON));
//    }
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack balletStack = player.getItemInHand(hand);
//
//        level.playSound(null, player.blockPosition(),
//                SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.MASTER, 1.0F, 1.0F);
//
//        if (!level.isClientSide) {
//            var balletEntity = new Ballet(EntityTypesRegistry.BALLET_TYPE.get(), level);
//
//            level.addFreshEntity(balletEntity);
//            balletStack.shrink(1);
//        }
//
//        player.awardStat(Stats.ITEM_USED.get(this));
//
//        return InteractionResultHolder.sidedSuccess(balletStack, level.isClientSide);
//    }
//}
