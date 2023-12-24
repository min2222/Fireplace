package com.min01.fireplace.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.min01.fireplace.entity.goal.DodgeArrowsGoal;
import com.min01.fireplace.entity.goal.KaratBuffMobsGoal;
import com.min01.fireplace.entity.goal.KaratEatingGoldenAppleGoal;
import com.min01.fireplace.entity.goal.KaratMeleeAttackGoal;
import com.min01.fireplace.entity.goal.KaratMovingGoal;
import com.min01.fireplace.entity.goal.KaratRangedAttackGoal;
import com.min01.fireplace.entity.goal.KaratShootProjectileGoal;
import com.min01.fireplace.entity.goal.KaratSplashPotionsGoal;
import com.min01.fireplace.entity.goal.KaratSummonMobGoal;
import com.min01.fireplace.entity.goal.KaratUsingShieldGoal;
import com.min01.fireplace.entity.goal.KaratWearEquipmentsGoal;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityKaratFeng extends AbstractKaratFeng
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public static final EntityDataAccessor<Float> MAX_SUMMONING_HP = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Boolean> STOP_FLYING = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> CHANGE_EQUIP = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_SHIELDING = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public FlyingMoveControl flyingControl =  new FlyingMoveControl(this, 20, false);
	
	public EntityKaratFeng(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
		this.moveControl = this.flyingControl;
		this.setNoGravity(true);
	}
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new KaratSummonMobGoal(this));
    	this.goalSelector.addGoal(0, new KaratWearEquipmentsGoal(this));
    	this.goalSelector.addGoal(4, new KaratMeleeAttackGoal(this, 0.65D, false));
    	this.goalSelector.addGoal(4, new KaratBuffMobsGoal(this));
    	this.goalSelector.addGoal(4, new KaratShootProjectileGoal(this));
    	this.goalSelector.addGoal(4, new KaratUsingShieldGoal(this));
    	this.goalSelector.addGoal(4, new KaratEatingGoldenAppleGoal(this));
    	this.goalSelector.addGoal(4, new KaratSplashPotionsGoal(this));
    	this.goalSelector.addGoal(4, new KaratRangedAttackGoal(this));
    	this.goalSelector.addGoal(4, new KaratMovingGoal(this));
    	this.goalSelector.addGoal(4, new DodgeArrowsGoal(this, 100.0F));
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(MAX_SUMMONING_HP, 300.0F);
    	this.entityData.define(STOP_FLYING, false);
    	this.entityData.define(CHANGE_EQUIP, false);
    	this.entityData.define(IS_SHIELDING, false);
    }
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 200.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.35D)
        		.add(Attributes.ARMOR, 2)
        		.add(Attributes.ARMOR_TOUGHNESS, 2)
        		.add(Attributes.ATTACK_DAMAGE, 4)
        		.add(Attributes.FLYING_SPEED, 0.45D)
        		.add(Attributes.FOLLOW_RANGE, 200.0D);
    }
    
    @Override
    protected PathNavigation createNavigation(Level p_186262_) 
    {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return this.stopFlying() ? super.createNavigation(p_186262_) : flyingpathnavigation;
    }
    
	@Override
	public void travel(Vec3 p_218382_) 
	{
		if(!this.stopFlying())
		{
			if(this.isEffectiveAi() || this.isControlledByLocalInstance())
			{
				this.moveRelative(this.getSpeed(), p_218382_);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale((double)0.5));
			}
			this.calculateEntityAnimation(this, false);
		}
		else
		{
			super.travel(p_218382_);
		}
	}
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_) 
    {
    	this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
		this.entityList.clear();
    	return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();

        this.navigation = this.createNavigation(this.level);
    	
    	if(this.level.isClientSide && this.isPreparingSkill())
    	{
            float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            this.level.addParticle(ParticleTypes.FLAME, true, this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, 0, 0, 0);
            this.level.addParticle(ParticleTypes.FLAME, true, this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, 0, 0, 0);
    	}
    	
    	if(this.getTarget() != null && this.stopFlying())
    	{
			this.getNavigation().moveTo(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 0.65);
    	}
        
    	if(this.getHealth() <= this.getMaxHealth() / 2 || this.getRemainSummoningHP() <= 0 && this.entityList.isEmpty())
    	{
        	for(int i = 0; i < this.entityList.size(); i++)
        	{
        		LivingEntity living = this.entityList.get(i);
        		if(living.isAlive())
        		{
        			living.discard();
        		}
        	}
        	
        	this.entityList.clear();
        	
        	if(!this.level.isClientSide)
        	{
        		ServerLevel serverlevel = (ServerLevel) this.level;
        		List<Entity> list = Lists.newArrayList(serverlevel.getAllEntities().iterator());
        		for(int i = 0; i < list.size(); i++)
        		{
        			Entity entity = list.get(i);
        			if(entity != null)
        			{
            			if(entity.getPersistentData() != null)
            			{
                			if(entity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
                			{
                				UUID uuid = entity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
                				if(uuid.equals(this.getUUID()))
                				{
                					entity.discard();
                				}
                			}
            			}
        			}
        		}
        	}
        	
    		this.setStopFlying(true);
    		this.moveControl = new MoveControl(this);
    		this.setNoGravity(false);
    		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65D);
    	}
    	
    	if(!this.stopFlying())
    	{
        	this.resetFallDistance();
    	}
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
    public void die(DamageSource p_21014_) 
    {
    	if(this.getPhase() == 2 || p_21014_ == DamageSource.OUT_OF_WORLD)
    	{
        	super.die(p_21014_);
        	
        	if(p_21014_.getEntity() != null && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
        	{
        		this.spawnAtLocation(FireplaceItems.KING_STAFF.get());
        	}
    	}
    	else 
    	{
    		this.setupPhase();
    		
			if(this.getPhase() == 0)
			{
    			this.setRemainSummoningHP(600.0F);
	    		this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.55D);
			}
			else if(this.getPhase() == 1)
			{
    			this.setRemainSummoningHP(900.0F);
	    		this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.65D);
			}
    	}
    }
    
    public void setupPhase()
    {
		this.setNoGravity(true);
		this.entityList.clear();
		this.setHealth(200.0F);
		if(!this.level.isClientSide)
		{
			this.setPhase(this.getPhase() + 1);
		}
		this.setStopFlying(false);
		this.moveControl = this.flyingControl;
		this.setShouldChangeEquip(true);
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
        if (this.canBlockDamageSource(p_21016_))
        {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.8f + this.level.random.nextFloat() * 0.4f);
            return false;
        }
    	return super.hurt(p_21016_, p_21017_);
    }
    
    private boolean canBlockDamageSource(DamageSource damageSourceIn)
    {
        Entity entity = damageSourceIn.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow) 
        {
        	AbstractArrow abstractarrowentity = (AbstractArrow) entity;
            if (abstractarrowentity.getPierceLevel() > 0) 
            {
                flag = true;
            }
        }
        if (!damageSourceIn.isBypassArmor() && this.isBlocking() && !flag) 
        {
        	Vec3 vec3d2 = damageSourceIn.getSourcePosition();
            if (vec3d2 != null)
            {
                Vec3 vec3d3 = this.getViewVector(1.0f);
                Vec3 vec3d4 = vec3d2.vectorTo(this.position()).normalize();
                vec3d4 = new Vec3(vec3d4.x, 0.0, vec3d4.z);
                if (vec3d4.dot(vec3d3) < 0.0) 
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setStopFlying(boolean value)
    {
    	this.entityData.set(STOP_FLYING, value);
    }
    
    public boolean stopFlying()
    {
    	return this.entityData.get(STOP_FLYING);
    }
    
    public void setRemainSummoningHP(float value)
    {
    	this.entityData.set(MAX_SUMMONING_HP, value);
    }
    
    public float getRemainSummoningHP()
    {
    	return this.entityData.get(MAX_SUMMONING_HP);
    }
    
    public void setShouldChangeEquip(boolean value)
    {
    	this.entityData.set(CHANGE_EQUIP, value);
    }
    
    public boolean shouldChangeEquip()
    {
    	return this.entityData.get(CHANGE_EQUIP);
    }
    
    public void setShielding(boolean value)
    {
    	this.entityData.set(IS_SHIELDING, value);
    }
    
    public boolean isShielding()
    {
    	return this.entityData.get(IS_SHIELDING);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setStopFlying(p_21450_.getBoolean("stopFlying"));
    	this.setPhase(p_21450_.getInt("Phase"));
    	this.setShouldChangeEquip(p_21450_.getBoolean("changeEquip"));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("stopFlying", this.stopFlying());
    	p_21484_.putInt("Phase", this.getPhase());
    	p_21484_.putBoolean("changeEquip", this.shouldChangeEquip());
    }
    
    @Override
    protected float getEquipmentDropChance(EquipmentSlot p_21520_) 
    {
    	return 0;
    }
    
    @Override
    public boolean shouldShowName() 
    {
    	return true;
    }
	
    @Override
    public Component getCustomName() 
    {
    	return Component.literal("KaratFeng");
    }

	@Override
	public int getMaxHurtCount() 
	{
		return 6;
	}
}
