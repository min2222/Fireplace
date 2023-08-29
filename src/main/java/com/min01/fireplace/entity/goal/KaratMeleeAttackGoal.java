package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class KaratMeleeAttackGoal extends MeleeAttackGoal
{
	public KaratMeleeAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_)
	{
		super(p_25552_, p_25553_, p_25554_);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && ((EntityKaratFeng) this.mob).stopFlying() && ((AbstractFireplaceMember) this.mob).shouldMove() && !this.mob.isNoGravity();
	}
}
