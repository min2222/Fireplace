package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class AbstractFireplaceSkillGoal extends Goal
{
	protected int skillWarmupDelay;
	protected int nextAttackTickCount;
	public AbstractFireplaceMember mob;
	
	public AbstractFireplaceSkillGoal(AbstractFireplaceMember mob) 
	{
		this.mob = mob;
	}

    @Override
    public boolean canUse() 
    {
    	LivingEntity livingentity = this.mob.getTarget();
    	if (livingentity != null && livingentity.isAlive()) 
    	{
    		if (this.mob.isUsingSkill())
    		{
    			return false;
    		} 
    		else 
    		{
    			return this.mob.tickCount >= this.nextAttackTickCount;
    		}
    	} 
    	else 
    	{
    		return false;
    	}
    }

    @Override
    public boolean canContinueToUse() 
    {
    	LivingEntity livingentity = this.mob.getTarget();
    	return livingentity != null && livingentity.isAlive() && this.skillWarmupDelay > 0;
    }

    @Override
    public void start()
    {
    	this.mob.setAggressive(true);
    	this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
    	this.mob.skillTickCount = this.getSkillTime();
    	this.nextAttackTickCount = this.mob.tickCount + this.getSkillUsingInterval();
    	
    	this.mob.setIsUsingSkill(this.getSkills());
    }
    
	@Override
	public void stop()
	{
		this.mob.setAggressive(false);
		this.mob.setShouldMove(true);
	}
    
    @Override
    public void tick() 
    {
    	if(this.mob.getTarget() != null)
    	{
        	this.mob.getLookControl().setLookAt(this.mob.getTarget(), 30.0F, 30.0F);
    	}
    	
    	--this.skillWarmupDelay;
    	if (this.skillWarmupDelay == 0) 
    	{
    		this.performSkill();
    	}
    }

    protected abstract void performSkill();

    //wait specific tick before use skill
    protected int getSkillWarmupTime()
    {
    	return 20;
    }

    protected abstract int getSkillTime();

    protected abstract int getSkillUsingInterval();
    
    protected abstract AbstractFireplaceMember.ActiveMemberSkills getSkills();
}
