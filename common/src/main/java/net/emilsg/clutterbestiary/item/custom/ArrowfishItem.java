package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArrowfishItem extends ArrowItem {

    public ArrowfishItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new ArrowfishProjectileEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        ArrowfishProjectileEntity projectile = new ArrowfishProjectileEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        projectile.pickup = AbstractArrow.Pickup.ALLOWED;
        return projectile;
    }
}
