package com.min01.fireplace.entity;

import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySnowyFeng extends AbstractHostileKaratFeng
{
	public EntitySnowyFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 10.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
    			.add(Attributes.ATTACK_DAMAGE, 4.0D)
        		.add(Attributes.ARMOR, 6)
        		.add(Attributes.ARMOR_TOUGHNESS, 6);
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
			if(this.distanceTo(this.getTarget()) <= 12)
			{
				this.setCanMoveToTarget(false);
				if(this.tickCount % 20 == 0)
				{
					Snowball snowball = new Snowball(this.level, this);
					snowball.setPos(this.position().add(0, this.getEyeHeight(), 0));
					Vec3 motion = FireplaceUtil.getEntityShootVector(snowball, this.getTarget());
					snowball.setDeltaMovement(motion);
					this.swing(InteractionHand.MAIN_HAND);
					this.level.addFreshEntity(snowball);
				}
			}
			else
			{
				this.setCanMoveToTarget(true);
			}
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() 
	{
		return SoundEvents.SNOW_GOLEM_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource p_33034_) 
	{
		return SoundEvents.SNOW_GOLEM_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() 
	{
		return SoundEvents.SNOW_GOLEM_DEATH;
	}
    
    @Override
    public boolean doHurtTarget(Entity p_21372_)
    {
    	p_21372_.setTicksFrozen(100);
    	return super.doHurtTarget(p_21372_);
    }
}
