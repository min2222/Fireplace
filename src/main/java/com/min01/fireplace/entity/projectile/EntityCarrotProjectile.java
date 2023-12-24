package com.min01.fireplace.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCarrotProjectile extends ThrowableItemProjectile
{
	public EntityCarrotProjectile(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_)
	{
		super(p_37442_, p_37443_);
	}

	public EntityCarrotProjectile(EntityType<? extends ThrowableItemProjectile> p_37432_, double p_37433_, double p_37434_, double p_37435_, Level p_37436_) 
	{
		super(p_37432_, p_37433_, p_37434_, p_37435_, p_37436_);
	}

	public EntityCarrotProjectile(EntityType<? extends ThrowableItemProjectile> p_37438_, LivingEntity p_37439_, Level p_37440_) 
	{
		super(p_37438_, p_37439_, p_37440_);
	}
	
	@Override
	protected void onHit(HitResult p_37260_) 
	{
		super.onHit(p_37260_);
		Vec3 vec = p_37260_.getLocation();
		for(int i = 0; i < 8; ++i) 
		{
			this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem())), vec.x, vec.y, vec.z, 0, 0, 0);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_37259_)
	{
		super.onHitEntity(p_37259_);
		if(p_37259_.getEntity() != null && p_37259_.getEntity() instanceof LivingEntity living)
		{
			living.knockback(0.8F, this.getX() - p_37259_.getEntity().getX(), this.getZ() - p_37259_.getEntity().getZ());
		}
	}

	@Override
	protected Item getDefaultItem() 
	{
		return Items.CARROT;
	}
}
