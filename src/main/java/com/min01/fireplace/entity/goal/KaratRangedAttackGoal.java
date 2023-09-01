package com.min01.fireplace.entity.goal;

import java.util.EnumSet;

import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KaratRangedAttackGoal extends Goal 
{
	public ItemStack prevItem;
	public ItemStack bowItem = new ItemStack(Items.BOW);
	private final EntityKaratFeng mob;
	private int attackIntervalMin;
	private int attackTime = -1;
	private int seeTime;

	public KaratRangedAttackGoal(EntityKaratFeng p_25792_, int p_25794_) 
	{
		this.mob = p_25792_;
		this.attackIntervalMin = p_25794_;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() 
	{
		return this.mob.getTarget() != null && this.mob.stopFlying() && this.mob.distanceTo(this.mob.getTarget()) >= 6;
	}

	@Override
	public void start()
	{
		this.mob.setAggressive(true);
		this.prevItem = this.mob.getMainHandItem().copy();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.bowItem);
	}

	@Override
	public void stop()
	{
		this.mob.setAggressive(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.mob.stopUsingItem();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.prevItem);
	}

	@Override
	public void tick() 
	{
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null) 
		{
			boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
			boolean flag1 = this.seeTime > 0;
			
			if (flag != flag1) 
			{
				this.seeTime = 0;
			}

			if (flag)
			{
				++this.seeTime;
			} else 
			{
				--this.seeTime;
			}
			
			if (this.mob.isUsingItem()) 
			{
				if (!flag && this.seeTime < -60)
				{
					this.mob.stopUsingItem();
				}
				else if (flag) 
				{
					int i = this.mob.getTicksUsingItem();
					if (i >= 20) 
					{
						this.mob.stopUsingItem();
						this.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
						this.attackTime = this.attackIntervalMin;
					}
				}
			}
			else if (--this.attackTime <= 0 && this.seeTime >= -60) 
			{
				this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof BowItem));
			}
		}
	}
	
	public void performRangedAttack(LivingEntity p_32141_, float p_32142_)
	{
		ItemStack itemstack = this.mob.getProjectile(this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof net.minecraft.world.item.BowItem)));
		AbstractArrow abstractarrow = this.getArrow(itemstack, p_32142_);
		double d0 = p_32141_.getX() - this.mob.getX();
		double d1 = p_32141_.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = p_32141_.getZ() - this.mob.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.mob.level.getDifficulty().getId() * 4));
		this.mob.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.mob.getRandom().nextFloat() * 0.4F + 0.8F));
		this.mob.level.addFreshEntity(abstractarrow);
	}
	
	protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) 
	{
		return ProjectileUtil.getMobArrow(this.mob, p_32156_, p_32157_);
	}
}
