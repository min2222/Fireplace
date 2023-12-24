package com.min01.fireplace.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractNonBossKarat extends AbstractKaratFeng
{
	public static final EntityDataAccessor<Boolean> CAN_MOVE_TO_TARGET = SynchedEntityData.defineId(AbstractNonBossKarat.class, EntityDataSerializers.BOOLEAN);
	
	public AbstractNonBossKarat(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(CAN_MOVE_TO_TARGET, true);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED))
		{
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && AbstractNonBossKarat.this.getTarget() == null;
        	} 
		});
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.getTarget() != null)
		{
			this.getLookControl().setLookAt(this.getTarget());
			if(this.canMoveToTarget())
			{
				this.getNavigation().moveTo(this.getTarget(), this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
			}
		}
	}
	
    public void setCanMoveToTarget(boolean value)
    {
    	this.entityData.set(CAN_MOVE_TO_TARGET, value);
    }
    
    public boolean canMoveToTarget()
    {
    	return this.entityData.get(CAN_MOVE_TO_TARGET);
    }

	@Override
	public int getMaxHurtCount() 
	{
		return 0;
	}
}
