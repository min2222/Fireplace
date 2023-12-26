package com.min01.fireplace.entity;

import javax.annotation.Nullable;

import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityVampireFeng extends AbstractHostileKaratFeng
{
	public static final EntityDataAccessor<Boolean> IS_BAT = SynchedEntityData.defineId(EntityVampireFeng.class, EntityDataSerializers.BOOLEAN);
	@Nullable
	private BlockPos targetPosition;
	
	public EntityVampireFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 100.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
    			.add(Attributes.ATTACK_DAMAGE, 6.0D)
        		.add(Attributes.ARMOR, 4)
        		.add(Attributes.ARMOR_TOUGHNESS, 4)
        		.add(Attributes.FOLLOW_RANGE, 100);
    }
    
    @Override
    public EntityDimensions getDimensions(Pose p_21047_)
    {
    	return this.isBat() ? EntityDimensions.fixed(0.5F, 0.9F) : super.getDimensions(p_21047_);
    }
    
    @Override
    protected void registerGoals()
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new MeleeAttackGoal(this, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), false)
    	{
    		@Override
    		public boolean canUse() 
    		{
    			return !EntityVampireFeng.this.isBat();
    		}
    	});
    }
    
    @Override
    protected void defineSynchedData()
    {
    	super.defineSynchedData();
    	this.entityData.define(IS_BAT, false);
    }
    
    @Override
    public void aiStep() 
    {
    	super.aiStep();
    	
    	this.refreshDimensions();
    	
    	if(this.getHealth() <= this.getMaxHealth() / 2)
    	{
    		if(!this.isBat())
    		{
    			this.transformToBat();
    		}
    		
    		if(this.isBat())
    		{
    			if(this.tickCount % 5 == 0)
    			{
        			this.heal(0.5F);
    			}
    			if(this.getTarget() != null && this.distanceTo(this.getTarget()) <= 12)
    			{
    				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    				
    				if(this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(this.position(), 2.0D)) 
    				{
    					this.targetPosition = new BlockPos(this.getTarget().getX() - this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0D, this.getTarget().getZ() - this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
    				}

    				if(this.targetPosition != null)
    				{
        				double d2 = (double)this.targetPosition.getX() + 0.5D - this.getX();
        				double d0 = (double)this.targetPosition.getY() + 0.1D - this.getY();
        				double d1 = (double)this.targetPosition.getZ() + 0.5D - this.getZ();
        				Vec3 vec3 = this.getDeltaMovement();
        				Vec3 vec31 = vec3.add((Math.signum(d2) * 0.5D - vec3.x) * (double)0.1F, (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F, (Math.signum(d1) * 0.5D - vec3.z) * (double)0.1F);
        				this.setDeltaMovement(vec31);
        				float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
        				float f1 = Mth.wrapDegrees(f - this.getYRot());
        				this.zza = 0.5F;
        				this.setYRot(this.getYRot() + f1);
    				}
    			}
    		}
    	}
    	else if(this.isBat() && this.getHealth() == this.getMaxHealth())
    	{
    		this.untransform();
    	}
    	
    	this.resetFallDistance();
    }
    
    @Override
    public void handleEntityEvent(byte p_21375_)
    {
    	super.handleEntityEvent(p_21375_);
    	if(p_21375_ == 98)
    	{
    		FireplaceUtil.addSpreadingParticle(150, this.level, ParticleTypes.LARGE_SMOKE, this.position().add(0, 1.5F, 0));
    	}
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_27451_)
    {
        return this.isBat() ? SoundEvents.BAT_HURT : super.getHurtSound(p_27451_);
    }

    @Override
    protected SoundEvent getDeathSound()
    {
    	return this.isBat() ? SoundEvents.BAT_DEATH : super.getDeathSound();
    }

    @Override
    public boolean isPushable() 
    {
    	return !this.isBat();
    }
    
    @Override
    protected void doPush(Entity p_27415_)
    {
    	if(!this.isBat())
    	{
    		super.doPush(p_27415_);
    	}
    }

    @Override
    protected void pushEntities()
    {
    	if(!this.isBat())
    	{
    		super.pushEntities();
    	}
    }
    
    @Override
    public boolean doHurtTarget(Entity p_21372_) 
    {
    	boolean doHurt = super.doHurtTarget(p_21372_);
    	if(doHurt)
    	{
    		this.heal(1 + Mth.nextInt(this.random, 1, 2));
    	}
    	return doHurt;
    }
    
    public void untransform()
    {
    	this.setIsBat(false);
    	this.setCanMoveToTarget(true);
    	this.level.broadcastEntityEvent(this, (byte) 98);
		this.level.playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1, 1);
    }
    
    public void transformToBat()
    {
    	this.setIsBat(true);
    	this.setCanMoveToTarget(false);
    	this.level.broadcastEntityEvent(this, (byte) 98);
		this.level.playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1, 1);
    }
    
    public void setIsBat(boolean value)
    {
    	this.entityData.set(IS_BAT, value);
    }
    
    public boolean isBat()
    {
    	return this.entityData.get(IS_BAT);
    }
}
