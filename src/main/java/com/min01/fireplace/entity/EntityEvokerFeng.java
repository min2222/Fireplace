package com.min01.fireplace.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class EntityEvokerFeng extends AbstractHostileKaratFeng
{
	public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(EntityEvokerFeng.class, EntityDataSerializers.BOOLEAN);
	
	public EntityEvokerFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}	
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 25.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
    			.add(Attributes.ATTACK_DAMAGE, 7.0D)
        		.add(Attributes.ARMOR, 2)
        		.add(Attributes.ARMOR_TOUGHNESS, 2);
    }
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ATTACKING, false);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), false));
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if(this.getTarget() != null)
		{
			if(this.distanceTo(this.getTarget()) <= 8 && this.tickCount % 20 == 0)
			{
				this.knockback(1.2F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
				this.setDeltaMovement(this.getDeltaMovement().add(0, 0.055F, 0));
				if(!this.isAttacking())
				{
					this.setAttacking(true);
				}
			}
		}
		
		if(this.isOnGround() && this.isAttacking() && this.getDeltaMovement().y <= 0)
		{
			this.setAttacking(false);
		}
	}
	
	@Override
	public void doPush(Entity p_21294_) 
	{
		super.doPush(p_21294_);
		if(this.isAttacking() && this.getTarget() != null)
		{
			this.getTarget().hurt(DamageSource.mobAttack(this), (float) this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));	
		}
	}

	public void setAttacking(boolean value)
	{
		this.entityData.set(ATTACKING, value);
	}
	
	public boolean isAttacking()
	{
		return this.entityData.get(ATTACKING);
	}
}
