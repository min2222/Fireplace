package com.min01.fireplace.entity.goal;

import java.util.EnumSet;

import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class KaratMovingGoal extends Goal
{
	private final EntityKaratFeng mob;
	private float attackRadiusSqr = 40;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;
	private boolean direction;

	public KaratMovingGoal(EntityKaratFeng p_25792_)
	{
		this.mob = p_25792_;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}
	
	@Override
	public boolean canUse()
	{
		return !this.mob.stopFlying();
	}

	@Override
	public void start() 
	{
		super.start();
		this.mob.setAggressive(true);
	}

	@Override
	public void stop()
	{
		super.stop();
		this.mob.setAggressive(false);
		this.seeTime = 0;
	}
	
	@Override
	public boolean requiresUpdateEveryTick() 
	{
		return true;
	}
	
	@Override
	public void tick() 
	{
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null) 
		{
			this.mob.getNavigation().moveTo(livingentity.getX(), livingentity.getEyeY(), livingentity.getZ(), 0.55);
			
			if(this.mob.tickCount % 40 == 0)
			{
				if(this.direction)
				{
					this.direction = false;
				}
				else
				{
					this.direction = true;
				}
			}
			
			double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
			boolean flag1 = this.seeTime > 0;
			if (flag != flag1)
			{
				this.seeTime = 0;
			}

			if (flag) 
			{
				++this.seeTime;
			} 
			else
			{
				--this.seeTime;
			}

			if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20)
			{
				this.mob.getNavigation().stop();
				++this.strafingTime;
			} 
			else 
			{
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) 
			{
				if ((double) this.mob.getRandom().nextFloat() < 0.3D) 
				{
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.mob.getRandom().nextFloat() < 0.3D)
				{
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1)
			{
				if (d0 > (double) (this.attackRadiusSqr * 0.75F)) 
				{
					this.strafingBackwards = false;
				} 
				else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) 
				{
					this.strafingBackwards = true;
				}
				
		        this.mob.setZza(this.direction ? -1.5f : 1.5F);
		        this.mob.setXxa(this.direction ? 1.5F : -1.5F);
			}
		}
	}
}
