package com.min01.fireplace.entity;

import java.util.List;

import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityFireFeng extends AbstractFlyingHostileKaratFeng
{
	public static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(EntityFireFeng.class, EntityDataSerializers.INT);
	
	public EntityFireFeng(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractFlyingHostileKaratFeng.createFlyingAttributes()
    			.add(Attributes.MAX_HEALTH, 120.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
    			.add(Attributes.ATTACK_DAMAGE, 9.0D)
        		.add(Attributes.FOLLOW_RANGE, 60);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(ATTACK_TYPE, 0);
    }
	
	@Override
	public boolean displayFireAnimation() 
	{
		return false;
	}
    
    @Override
    public void aiStep() 
    {
    	super.aiStep();
    	this.resetFallDistance();
		if(this.getTarget() != null)
		{
			if(this.distanceTo(this.getTarget()) <= 8 && this.distanceTo(this.getTarget()) >= 4)
			{
				this.setAttackType(AttackType.FIREBALL);
				this.setCanMoveToTarget(false);
			}
			else if(this.distanceTo(this.getTarget()) <= 3)
			{
				this.setAttackType(AttackType.FIRE_BREATH);
				this.setCanMoveToTarget(true);
			}
			else if(this.distanceTo(this.getTarget()) > 8)
			{
				this.setAttackType(AttackType.FIRE_DASH);
				this.setCanMoveToTarget(true);
			}
			
			switch(this.getAttackType())
			{
			case FIREBALL:
				if(this.tickCount % 10 == 0)
				{
					SmallFireball fireball = new SmallFireball(this.level, this, 0, 0, 0);
					fireball.setPos(this.position().add(0, this.getEyeHeight(), 0));
					Vec3 motion = FireplaceUtil.getEntityShootVector(fireball, this.getTarget());
					fireball.setDeltaMovement(motion);
					this.level.addFreshEntity(fireball);
					this.setShooting();
				}
				break;
			case FIRE_BREATH:
				LivingEntity breathVictim = FireplaceUtil.createBreathParticle(this.level, ParticleTypes.FLAME, this, this.position(), 10, 1, 0.3, 0.25);
				if(breathVictim != null && this.tickCount % 20 == 0)
				{
					breathVictim.setSecondsOnFire(10);
					breathVictim.hurt(DamageSource.mobAttack(this), 0.1F);
				}
				break;
			case FIRE_DASH:
				if(this.tickCount % 20 == 0)
				{
					Vec3 vec3 = FireplaceUtil.getEntityShootVector(this, this.getTarget());
					List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox());
					for(int i = 0; i < list.size(); i++)
					{
						LivingEntity living = list.get(i);
						boolean flag = living instanceof AbstractKaratFeng karat ? karat.getCurrentRaid() == null : living != this;
						if(flag)
						{
							living.setSecondsOnFire(40);
							living.hurt(DamageSource.mobAttack(this), (float) this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
						}
					}
					this.setDeltaMovement(vec3);
				}
				break;
			case NONE:
				break;
			}
		}
		else
		{
			this.setAttackType(AttackType.NONE);
		}
		
		this.level.broadcastEntityEvent(this, (byte) 1);
    }
    
    @Override
    public void handleEntityEvent(byte p_21375_)
    {
    	super.handleEntityEvent(p_21375_);
    	if(p_21375_ == 1)
    	{
        	for(int i = 0; i < 100; i++)
        	{
        		this.level.addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, this.getX() + this.level.random.nextGaussian() * 0.2D, this.getEyeY() + this.level.random.nextGaussian() * 0.2D, this.getZ() + this.level.random.nextGaussian() * 0.2D, 0, 0, 0);
        	}
    	}
    }
    
    private void setShooting()
    {
    	this.setAttackType(AttackType.FIREBALL);
	}

	public boolean isShooting()
    {
    	return this.getAttackType() == AttackType.FIREBALL;
    }
	
	public boolean isDashing()
    {
    	return this.getAttackType() == AttackType.FIRE_DASH;
    }
	
	public boolean isBreathing()
    {
    	return this.getAttackType() == AttackType.FIRE_BREATH;
    }
    
	public AttackType getAttackType()
	{
		return AttackType.values()[this.entityData.get(ATTACK_TYPE)];
	}
	
	public void setAttackType(AttackType type)
	{
		this.entityData.set(ATTACK_TYPE, type.ordinal());
	}
	
	public static enum AttackType
	{
		NONE,
		FIREBALL,
		FIRE_BREATH,
		FIRE_DASH;
	}
}
