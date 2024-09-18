package com.min01.fireplace.entity;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.min01.fireplace.misc.IKaratRaid;
import com.min01.fireplace.raid.KaratRaid;
import com.min01.fireplace.raid.KaratRaidSaveData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractKaratFeng extends Monster
{
	public int skillTickCount;
	private AbstractKaratFeng.KaratSkills currentSkill = AbstractKaratFeng.KaratSkills.NONE;
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Byte> DATA_SKILL_ID = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.BYTE);
	public static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> HURT_COUNT = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_PREPARING_SKILL = SynchedEntityData.defineId(AbstractKaratFeng.class, EntityDataSerializers.BOOLEAN);
	
	@Nullable
	protected KaratRaid raid;
	private int wave;
	private boolean canJoinRaid;
	private int ticksOutsideRaid;
	
	public AbstractKaratFeng(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createFireplaceAttributes()
    {
        return Mob.createMobAttributes()
        		.add(Attributes.FOLLOW_RANGE, 40.0D);
    }
    
	public boolean canJoinRaid() 
	{
		return this.canJoinRaid;
	}

	public void setCanJoinRaid(boolean p_37898_) 
	{
		this.canJoinRaid = p_37898_;
	}
	
	@Override
	public void aiStep()
	{
		if(this.level instanceof ServerLevel && this.isAlive()) 
		{
			KaratRaid raid = this.getCurrentRaid();
			if(this.canJoinRaid()) 
			{
				if(raid == null) 
				{
					if(this.level.getGameTime() % 20L == 0L)
					{
						KaratRaid raid1 = ((IKaratRaid)(ServerLevel) this.level).getRaidAt(this.blockPosition());
						if(raid1 != null && KaratRaidSaveData.canJoinRaid(this, raid1)) 
						{
							raid1.joinRaid(raid1.getGroupsSpawned(), this, (BlockPos) null, true);
						}
					}
				} 
				else 
				{
					LivingEntity livingentity = this.getTarget();
					if(livingentity != null && (livingentity.getType() == EntityType.PLAYER || livingentity.getType() == EntityType.IRON_GOLEM)) 
					{
						this.noActionTime = 0;
					}
				}
			}
		}

		super.aiStep();
	}
	
	@Override
	protected void updateNoActionTime() 
	{
		this.noActionTime += 2;
	}
	
	@Override
	public void die(DamageSource p_37847_)
	{
		if(this.level instanceof ServerLevel)
		{
			Entity entity = p_37847_.getEntity();
			KaratRaid raid = this.getCurrentRaid();
			if(raid != null) 
			{
				if(entity != null && entity.getType() == EntityType.PLAYER) 
				{
					raid.addHeroOfTheVillage(entity);
				}

				raid.removeFromRaid(this, false);
			}
		}
	}
	
	public void setCurrentRaid(@Nullable KaratRaid p_37852_) 
	{
		this.raid = p_37852_;
	}

	@Nullable
	public KaratRaid getCurrentRaid() 
	{
		return this.raid;
	}

	public boolean hasActiveRaid() 
	{
		return this.getCurrentRaid() != null && this.getCurrentRaid().isActive();
	}

	public void setWave(int p_37843_)
	{
		this.wave = p_37843_;
	}

	public int getWave() 
	{
		return this.wave;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_37870_)
	{
		super.addAdditionalSaveData(p_37870_);
		p_37870_.putInt("Wave", this.wave);
		p_37870_.putBoolean("CanJoinRaid", this.canJoinRaid);
		if(this.raid != null)
		{
			p_37870_.putInt("RaidId", this.raid.getId());
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_37862_)
	{
		super.readAdditionalSaveData(p_37862_);
		this.wave = p_37862_.getInt("Wave");
		this.canJoinRaid = p_37862_.getBoolean("CanJoinRaid");
		if(p_37862_.contains("RaidId", 3)) 
		{
			if(this.level instanceof ServerLevel) 
			{
				this.raid = ((IKaratRaid)(ServerLevel) this.level).getRaids().get(p_37862_.getInt("RaidId"));
			}

			if(this.raid != null)
			{
				this.raid.addWaveMob(this.wave, this, false);
			}
		}
	}
	
	@Override
	public boolean removeWhenFarAway(double p_37894_) 
	{
		return this.getCurrentRaid() == null ? super.removeWhenFarAway(p_37894_) : false;
	}
	
	@Override
	public boolean requiresCustomPersistence() 
	{
		return super.requiresCustomPersistence() || this.getCurrentRaid() != null;
	}

	public int getTicksOutsideRaid()
	{
		return this.ticksOutsideRaid;
	}

	public void setTicksOutsideRaid(int p_37864_) 
	{
		this.ticksOutsideRaid = p_37864_;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_)
	{
		this.setCanJoinRaid(true);
		return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
	}
	
	static class RaiderMoveThroughVillageGoal extends Goal
	{
		private final AbstractKaratFeng raider;
		private final double speedModifier;
		private BlockPos poiPos;
		private final List<BlockPos> visited = Lists.newArrayList();
		private final int distanceToPoi;
		private boolean stuck;

		public RaiderMoveThroughVillageGoal(AbstractKaratFeng p_37936_, double p_37937_, int p_37938_) 
		{
			this.raider = p_37936_;
			this.speedModifier = p_37937_;
			this.distanceToPoi = p_37938_;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse()
		{
			this.updateVisited();
			boolean flag = this.raider.getTarget() == null || !((ServerLevel) this.raider.level).isVillage(this.raider.blockPosition());
			return this.isValidRaid() && this.hasSuitablePoi() && flag;
		}

		private boolean isValidRaid() 
		{
			return this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver();
		}

		private boolean hasSuitablePoi() 
		{
			ServerLevel serverlevel = (ServerLevel) this.raider.level;
			BlockPos blockpos = this.raider.blockPosition();
			Optional<BlockPos> optional = serverlevel.getPoiManager().getRandom((p_219843_) ->
			{
				return p_219843_.is(PoiTypes.HOME);
			}, this::hasNotVisited, PoiManager.Occupancy.ANY, blockpos, 48, this.raider.random);
			if(!optional.isPresent()) 
			{
				return false;
			} 
			else 
			{
				this.poiPos = optional.get().immutable();
				return true;
			}
		}

		public boolean canContinueToUse() 
		{
			if(this.raider.getNavigation().isDone()) 
			{
				return false;
			} 
			else 
			{
				return this.raider.getTarget() == null && !this.poiPos.closerToCenterThan(this.raider.position(), (double) (this.raider.getBbWidth() + (float) this.distanceToPoi)) && !this.stuck;
			}
		}

		public void stop() 
		{
			if(this.poiPos.closerToCenterThan(this.raider.position(), (double) this.distanceToPoi)) 
			{
				this.visited.add(this.poiPos);
			}

		}

		public void start() 
		{
			super.start();
			this.raider.setNoActionTime(0);
			this.raider.getNavigation().moveTo((double) this.poiPos.getX(), (double) this.poiPos.getY(), (double) this.poiPos.getZ(), this.speedModifier);
			this.stuck = false;
		}

		public void tick()
		{
			if(this.raider.getNavigation().isDone()) 
			{
				Vec3 vec3 = Vec3.atBottomCenterOf(this.poiPos);
				Vec3 vec31 = DefaultRandomPos.getPosTowards(this.raider, 16, 7, vec3, (double) ((float) Math.PI / 10F));
				if(vec31 == null) 
				{
					vec31 = DefaultRandomPos.getPosTowards(this.raider, 8, 7, vec3, (double) ((float) Math.PI / 2F));
				}

				if(vec31 == null)
				{
					this.stuck = true;
					return;
				}

				this.raider.getNavigation().moveTo(vec31.x, vec31.y, vec31.z, this.speedModifier);
			}
		}

		private boolean hasNotVisited(BlockPos p_37943_) 
		{
			for(BlockPos blockpos : this.visited) 
			{
				if(Objects.equals(p_37943_, blockpos)) 
				{
					return false;
				}
			}

			return true;
		}

		private void updateVisited() 
		{
			if(this.visited.size() > 2) 
			{
				this.visited.remove(0);
			}
		}
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this)
        {
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && AbstractKaratFeng.this.getTarget() == null;
        	} 
        });
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(4, new AbstractKaratFeng.RaiderMoveThroughVillageGoal(this, this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), 1));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(this.getClass()));
	}

	public void spawnItemParticles(ItemStack p_21061_, int p_21062_)
	{
		for(int i = 0; i < p_21062_; ++i) 
		{
			Vec3 vec3 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3 = vec3.xRot(-this.getXRot() * ((float)Math.PI / 180F));
			vec3 = vec3.yRot(-this.getYRot() * ((float)Math.PI / 180F));
			double d0 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
			Vec3 vec31 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
			vec31 = vec31.xRot(-this.getXRot() * ((float)Math.PI / 180F));
			vec31 = vec31.yRot(-this.getYRot() * ((float)Math.PI / 180F));
			vec31 = vec31.add(this.getX(), this.getEyeY(), this.getZ());
			if(this.level instanceof ServerLevel) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
				((ServerLevel)this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, p_21061_), vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
			else
				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, p_21061_), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
		}
	}
	
	public abstract int getMaxHurtCount();
	
    public boolean canBlockPosBeSeen(BlockPos pos)
    {
        double x = pos.getX() + 0.5F;
        double y = pos.getY() + 0.5F;
        double z = pos.getZ() + 0.5F;
        HitResult result =  this.level.clip(new ClipContext(new Vec3(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0D || result.getType() == HitResult.Type.MISS;
    }
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_SKILL_ID, (byte)0);
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(CAN_MOVE, true);
    	this.entityData.define(PHASE, 0);
    	this.entityData.define(HURT_COUNT, 0);
    	this.entityData.define(IS_PREPARING_SKILL, false);
	}
	
    public void setAnimationState(int value)
    {
        this.entityData.set(ANIMATION_STATE, value);
    }
    
    public int getAnimationState()
    {
        return this.entityData.get(ANIMATION_STATE);
    }
    
    public void setCanMove(boolean value)
    {
    	this.entityData.set(CAN_MOVE, value);
    }
    
    public boolean canMove()
    {
    	return this.entityData.get(CAN_MOVE);
    }
    
    public void setPhase(int value)
    {
    	this.entityData.set(PHASE, value);
    }
    
    public int getPhase()
    {
    	return this.entityData.get(PHASE);
    }
    
    public void setHurtCount(int value)
    {
    	this.entityData.set(HURT_COUNT, value);
    }
    
    public int getHurtCount()
    {
    	return this.entityData.get(HURT_COUNT);
    }
    
    public void setPreparingSkill(boolean value)
    {
    	this.entityData.set(IS_PREPARING_SKILL, value);
    }
    
    public boolean isPreparingSkill()
    {
    	return this.entityData.get(IS_PREPARING_SKILL);
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
		if(this.hasActiveRaid())
		{
			this.getCurrentRaid().updateBossbar();
		}
		
    	this.setHurtCount(this.getHurtCount() + 1);
    	return super.hurt(p_21016_, p_21017_);
    }
    
	@Override
	protected void customServerAiStep() 
	{
		super.customServerAiStep();
		if(this.skillTickCount > 0)
		{
			--this.skillTickCount;
		}
	}
    
	protected int getSkillTime()
	{
		return this.skillTickCount;
	}
	
	public boolean isUsingSkill() 
	{
		if(this.level.isClientSide) 
		{
			return this.entityData.get(DATA_SKILL_ID) > 0;
		} 
		else
		{
			return this.skillTickCount > 0;
		}
	}
	
	public AbstractKaratFeng.KaratSkills getCurrentSkill() 
	{
		return !this.level.isClientSide ? this.currentSkill : AbstractKaratFeng.KaratSkills.byId(this.entityData.get(DATA_SKILL_ID));
	}
	
	public void setIsUsingSkill(AbstractKaratFeng.KaratSkills p_33728_) 
	{
		this.currentSkill = p_33728_;
		this.entityData.set(DATA_SKILL_ID, (byte)p_33728_.id);
	}
	
	public static enum KaratSkills 
	{
		NONE(0),
		KARAT_SUMMON_MOB(1),
		KARAT_BUFF_MOBS(2),
		KARAT_SHOOT_PROJECTILE(3),
		KARAT_USE_SHIELD(4),
		KARAT_EAT_GOLDEN_APPLE(5),
		KARAT_SPLASH_POTIONS(6),
		KARAT_RANGE(7),
		NECRO_FENG_SUMMONING(8);

		final int id;

		private KaratSkills(int p_33754_) 
		{
			this.id = p_33754_;
		}
		
		public static AbstractKaratFeng.KaratSkills byId(int p_33759_)
		{
			for(AbstractKaratFeng.KaratSkills EntityGrandmaster$illagerspell : values()) 
			{
				if(p_33759_ == EntityGrandmaster$illagerspell.id) 
				{
					return EntityGrandmaster$illagerspell;
				}
			}
			return NONE;
		}
	}
}
