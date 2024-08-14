package com.min01.fireplace.entity;

import java.util.List;

import com.min01.fireplace.misc.FireplaceTags;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

public class EntityUFO extends AbstractOwnableMonster<EntityAlienFeng>
{
	public EntityUFO(EntityType<? extends Monster> p_19870_, Level p_19871_)
	{
		super(p_19870_, p_19871_);
		this.setNoGravity(true);
		this.moveControl = new FlyingMoveControl(this, 20, false);
	}
	
	public EntityUFO(EntityType<? extends Monster> p_19870_, Level p_19871_, EntityAlienFeng owner)
	{
		this(p_19870_, p_19871_);
		this.setOwner(owner);
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
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
        		.add(Attributes.MAX_HEALTH, 200)
        		.add(Attributes.ARMOR, 20)
        		.add(Attributes.ARMOR_TOUGHNESS, 20)
        		.add(Attributes.FLYING_SPEED, 0.65D);
    }
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, this.getAttributeBaseValue(Attributes.FLYING_SPEED))
		{
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && EntityUFO.this.getOwner() != null && EntityUFO.this.getOwner().getTarget() == null;
        	} 
		});
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }
	
	@Override
	public void travel(Vec3 travelVector) 
	{
		LivingEntity livingentity = (LivingEntity) this.getFirstPassenger();
		if(this.isVehicle() && livingentity != null)
		{
			if(this.getOwner() != null && livingentity == this.getOwner())
			{
	            this.setYRot(livingentity.getYRot());
	            this.yRotO = this.getYRot();
	            this.setXRot(livingentity.getXRot() * 0.5F);
	            this.setRot(this.getYRot(), this.getXRot());
	            this.yBodyRot = this.getYRot();
	            this.yHeadRot = this.yBodyRot;
			}
		}
		
		if(this.isEffectiveAi() || this.isControlledByLocalInstance())
		{
			this.moveRelative(this.getSpeed(), travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale((double)0.1));
		}
		
		this.calculateEntityAnimation(this, false);
	}
	
	@Override
	public double getPassengersRidingOffset() 
	{
		return -0.4F;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		this.resetFallDistance();
		
		if(this.getOwner() != null)
		{
			if(this.getOwner().getTarget() != null)
			{
				this.lookAt(Anchor.EYES, this.getOwner().getTarget().position().add(0, this.getOwner().getTarget().getEyeHeight(), 0));
				
				List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6));
				for(int i = 0; i < list.size(); i++)
				{
					LivingEntity living = list.get(i);
					boolean flag = living.getType().is(FireplaceTags.THE_FENGS) && living instanceof AbstractKaratFeng feng ? feng.getCurrentRaid() == null : true;
					if(list.size() >= 9 && !FireplaceUtil.GRAVITY_MAP.containsKey(living) && living != this && living != this.getOwner() && flag)
					{
						living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1, false, false));
						living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 1, false, false));
						FireplaceUtil.GRAVITY_MAP.put(living, 0);
					}
				}
			}
		}
	}
}
