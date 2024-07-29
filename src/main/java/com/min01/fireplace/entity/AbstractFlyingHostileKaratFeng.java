package com.min01.fireplace.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractFlyingHostileKaratFeng extends AbstractKaratFeng
{
	public static final EntityDataAccessor<Boolean> CAN_MOVE_TO_TARGET = SynchedEntityData.defineId(AbstractFlyingHostileKaratFeng.class, EntityDataSerializers.BOOLEAN);
	
	public AbstractFlyingHostileKaratFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
		this.setNoGravity(true);
		this.moveControl = new FlyingMoveControl(this, 20, false);
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
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED))
		{
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && AbstractFlyingHostileKaratFeng.this.getTarget() == null;
        	} 
		});
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}
	
    
    @Override
    protected PathNavigation createNavigation(Level p_186262_) 
    {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }
	
    public static AttributeSupplier.Builder createFlyingAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
        		.add(Attributes.FLYING_SPEED, 0.55D);
    }
    
	@Override
	public void travel(Vec3 p_218382_) 
	{
		if(this.isEffectiveAi() || this.isControlledByLocalInstance())
		{
			this.moveRelative(this.getSpeed(), p_218382_);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale((double)0.1));
		}
		this.calculateEntityAnimation(false);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		this.resetFallDistance();
		if(this.getTarget() != null)
		{
			this.lookAt(Anchor.EYES, this.getTarget().position().add(0, this.getTarget().getEyeHeight(), 0));
			if(this.canMoveToTarget())
			{
				this.getNavigation().moveTo(this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ(), this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
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
