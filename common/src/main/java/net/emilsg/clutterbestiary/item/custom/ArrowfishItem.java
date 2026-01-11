package net.emilsg.clutterbestiary.item.custom;

import net.emilsg.clutterbestiary.entity.custom.ArrowfishProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ArrowfishItem extends ArrowItem {

    public ArrowfishItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new ArrowfishProjectileEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ArrowfishProjectileEntity projectile = new ArrowfishProjectileEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return projectile;
    }
}
