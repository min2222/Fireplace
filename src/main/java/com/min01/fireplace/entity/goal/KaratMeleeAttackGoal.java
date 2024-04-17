package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class KaratMeleeAttackGoal extends MeleeAttackGoal
{
	private EntityKaratFeng mob;
	public KaratMeleeAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_)
	{
		super(p_25552_, p_25553_, p_25554_);
		this.mob = (EntityKaratFeng) p_25552_;
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && this.mob.isMelee() && !this.mob.isChangeEquip();
	}
	
	@Override
	public boolean canContinueToUse() 
	{
		return super.canContinueToUse() && this.canUse();
	}
	
	@Override
	protected double getAttackReachSqr(LivingEntity p_25556_)
	{
		return (double)(this.mob.getBbWidth() * 3.0F * this.mob.getBbWidth() * 3.0F + p_25556_.getBbWidth());
	}
}
