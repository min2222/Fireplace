package com.min01.fireplace.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractHostileKaratFeng extends AbstractKaratFeng
{
	public static final EntityDataAccessor<Boolean> CAN_MOVE_TO_TARGET = SynchedEntityData.defineId(AbstractHostileKaratFeng.class, EntityDataSerializers.BOOLEAN);
	
	public AbstractHostileKaratFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
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
        		return super.canUse() && AbstractHostileKaratFeng.this.getTarget() == null;
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
			boolean flag = this instanceof EntityVampireFeng vampire ? !vampire.isBat() : true;
			
			if(flag)
			{
				this.lookAt(Anchor.EYES, this.getTarget().position().add(0, this.getTarget().getEyeHeight(), 0));
			}
			
			if(this.canMoveToTarget())
			{
				boolean targetFlag = this.getCurrentRaid() == null ? true : ((ServerLevel) this.level).isVillage(this.getTarget().blockPosition());
				
				if(targetFlag)
				{
					this.getNavigation().moveTo(this.getTarget(), this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
				}
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
