package com.min01.fireplace.entity;

import java.util.ArrayList;

import com.min01.fireplace.entity.goal.NecroFengSummonUndeadGoal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityNecroFeng extends AbstractHostileKaratFeng
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public ArrayList<LivingEntity> aiList = new ArrayList<>();
	public EntityNecroFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 80.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.45D)
    			.add(Attributes.ATTACK_DAMAGE, 3.0D)
        		.add(Attributes.ARMOR, 2)
        		.add(Attributes.ARMOR_TOUGHNESS, 2)
        		.add(Attributes.FOLLOW_RANGE, 70);
    }
    
    @Override
    protected void registerGoals()
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new NecroFengSummonUndeadGoal(this));
    }
    
    @Override
    public void remove(RemovalReason p_146834_) 
    {
    	super.remove(p_146834_);
    	for(int i = 0; i < this.entityList.size(); i++)
    	{
    		LivingEntity living = this.entityList.get(i);
    		if(living.isAlive())
    		{
    			living.discard();
    		}
    	}
    	this.entityList.clear();
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_) 
    {
		this.entityList.clear();
    	return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	
    	for(int i = 0; i < this.entityList.size(); i++)
    	{
    		LivingEntity living = this.entityList.get(i);
    		if(living.tickCount > 20 && living instanceof Mob mob)
    		{
    			if(!this.aiList.contains(mob))
    			{
        			mob.setNoAi(false);
        			mob.setInvulnerable(false);
        			this.aiList.add(mob);
    			}
    		}
    	}
    	
    	if(this.getTarget() != null)
    	{
    		if(this.distanceTo(this.getTarget()) <= 6)
    		{
    			this.setCanMoveToTarget(false);
    		}
    		else
    		{
    			this.setCanMoveToTarget(true);
    		}
    	}
		else
		{
			this.setCanMoveToTarget(true);
		}
    	
    	if(this.level.isClientSide && this.isPreparingSkill())
    	{
            float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, true, this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, 0, 0, 0);
            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, true, this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, 0, 0, 0);
    	}
    }
}
