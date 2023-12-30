package com.min01.fireplace.entity;

import com.min01.fireplace.init.FireplaceEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityAlienFeng extends AbstractHostileKaratFeng
{
	public EntityAlienFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 50.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
    			.add(Attributes.ATTACK_DAMAGE, 6.0D)
        		.add(Attributes.ARMOR, 12)
        		.add(Attributes.ARMOR_TOUGHNESS, 12)
        		.add(Attributes.FOLLOW_RANGE, 80);
    }
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_) 
	{
		EntityUFO ufo = new EntityUFO(FireplaceEntities.UFO.get(), this.level, this);
		ufo.setPos(this.position());
		this.level.addFreshEntity(ufo);
		this.startRiding(ufo);
		return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		this.setCanMoveToTarget(false);
		if(this.getTarget() != null)
		{
			Vec3 vec3 = this.getTarget().position().add(0, this.getTarget().getEyeHeight() + 5, 0);
			this.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
		}
	}
	
	@Override
	public boolean hurt(DamageSource p_21016_, float p_21017_) 
	{
		if(this.isPassenger())
		{
			if(this.getVehicle() instanceof EntityUFO ufo)
			{
				if(ufo.getOwner() == this)
				{
					return ufo.hurt(p_21016_, p_21017_);
				}
			}
		}
		return super.hurt(p_21016_, p_21017_);
	}
}
