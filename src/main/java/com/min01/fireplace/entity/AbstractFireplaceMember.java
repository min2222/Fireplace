package com.min01.fireplace.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractFireplaceMember extends Monster
{
	public int skillTickCount;
	private AbstractFireplaceMember.ActiveMemberSkills currentSkill = AbstractFireplaceMember.ActiveMemberSkills.NONE;
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractFireplaceMember.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Byte> DATA_SKILL_ID = SynchedEntityData.defineId(AbstractFireplaceMember.class, EntityDataSerializers.BYTE);
	public static final EntityDataAccessor<Boolean> SHOULD_MOVE = SynchedEntityData.defineId(AbstractFireplaceMember.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AbstractFireplaceMember.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> HURT_COUNT = SynchedEntityData.defineId(AbstractFireplaceMember.class, EntityDataSerializers.INT);
	
	public AbstractFireplaceMember(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createFireplaceAttributes()
    {
        return Mob.createMobAttributes()
        		.add(Attributes.FOLLOW_RANGE, 200.0D);
    }
    
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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
			if (this.level instanceof ServerLevel) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
				((ServerLevel)this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, p_21061_), vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
			else
				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, p_21061_), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
		}
	}
	
	public abstract int getMaxHurtCount();
	
	public HitResult rayTraceLooking(Entity entity, double distance)
	{
		Vec3 vecPlayerOrigin = entity.getEyePosition(1.0F);
        Vec3 vecPlayerLook = entity.getViewVector(1.0F);
        Vec3 vecPlayerSee = vecPlayerOrigin.add(vecPlayerLook.x * distance, vecPlayerLook.y * distance, vecPlayerLook.z * distance);
        return entity.level.clip(new ClipContext(vecPlayerOrigin, vecPlayerSee, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
	}
	
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
	public boolean removeWhenFarAway(double p_21542_)
	{
		return false;
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_SKILL_ID, (byte)0);
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(SHOULD_MOVE, true);
    	this.entityData.define(PHASE, 0);
    	this.entityData.define(HURT_COUNT, 0);
	}
	
    public void setAnimationState(int value)
    {
        this.entityData.set(ANIMATION_STATE, value);
    }
    
    public int getAnimationState()
    {
        return this.entityData.get(ANIMATION_STATE);
    }
    
    public void setShouldMove(boolean value)
    {
    	this.entityData.set(SHOULD_MOVE, value);
    }
    
    public boolean shouldMove()
    {
    	return this.entityData.get(SHOULD_MOVE);
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
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
    	this.setHurtCount(this.getHurtCount() + 1);
    	return super.hurt(p_21016_, p_21017_);
    }
    
    @Override
	public void move(MoverType p_19973_, Vec3 p_19974_) 
	{
		if(this.shouldMove())
		{
			super.move(p_19973_, p_19974_);
		}
		else if(!this.shouldMove())
		{
			double yvec = this.onGround ? 0 : this.isNoGravity() ? 0 : this.getDeltaMovement().y;
			super.move(p_19973_, new Vec3(0, yvec, 0));
		}
	}
    
	@Override
	protected void customServerAiStep() 
	{
		super.customServerAiStep();
		if (this.skillTickCount > 0)
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
		if (this.level.isClientSide) 
		{
			return this.entityData.get(DATA_SKILL_ID) > 0;
		} 
		else
		{
			return this.skillTickCount > 0;
		}
	}
	
	protected AbstractFireplaceMember.ActiveMemberSkills getCurrentSpell() 
	{
		return !this.level.isClientSide ? this.currentSkill : AbstractFireplaceMember.ActiveMemberSkills.byId(this.entityData.get(DATA_SKILL_ID));
	}
	
	public void setIsUsingSkill(AbstractFireplaceMember.ActiveMemberSkills p_33728_) 
	{
		this.currentSkill = p_33728_;
		this.entityData.set(DATA_SKILL_ID, (byte)p_33728_.id);
	}
	
	public static enum ActiveMemberSkills 
	{
		NONE(0),
		KARAT_SUMMON_MOB(1),
		KARAT_BUFF_MOBS(2),
		KARAT_WEAR_EQUIPMENTS(3),
		KARAT_SHOOT_PROJECTILE(4),
		KARAT_USE_SHIELD(5),
		KARAT_EAT_GOLDEN_APPLE(6),
		KARAT_SPLASH_POTIONS(7);

		final int id;

		private ActiveMemberSkills(int p_33754_) 
		{
			this.id = p_33754_;
		}
		
		public static AbstractFireplaceMember.ActiveMemberSkills byId(int p_33759_)
		{
			for(AbstractFireplaceMember.ActiveMemberSkills EntityGrandmaster$illagerspell : values()) 
			{
				if (p_33759_ == EntityGrandmaster$illagerspell.id) 
				{
					return EntityGrandmaster$illagerspell;
				}
			}
			return NONE;
		}
	}
}
