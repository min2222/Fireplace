package com.min01.fireplace.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractOwnableMonster<T extends LivingEntity> extends Monster
{
	@Nullable
	private UUID ownerUUID;
	@Nullable
	private T cachedOwner;
	
	private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(AbstractOwnableMonster.class, EntityDataSerializers.INT);
	
	public AbstractOwnableMonster(EntityType<? extends Monster> p_19870_, Level p_19871_) 
	{
		super(p_19870_, p_19871_);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(OWNER_ID, 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag p_37265_) 
	{
		if (this.ownerUUID != null)
		{
			p_37265_.putUUID("Owner", this.ownerUUID);
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag p_37262_) 
	{
		if (p_37262_.hasUUID("Owner")) 
		{
			this.ownerUUID = p_37262_.getUUID("Owner");
		}
	}
	
	public void setOwner(@Nullable T p_37263_)
	{
		if (p_37263_ != null)
		{
			this.ownerUUID = p_37263_.getUUID();
			this.cachedOwner = p_37263_;
			this.entityData.set(OWNER_ID, p_37263_.getId());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public T getOwner() 
	{
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) 
		{
			return this.cachedOwner;
		}
		else if (this.ownerUUID != null && this.level instanceof ServerLevel) 
		{
			this.cachedOwner = (T) ((ServerLevel)this.level).getEntity(this.ownerUUID);
			return this.cachedOwner;
		}
		else 
		{
			return (T) this.level.getEntity(this.entityData.get(OWNER_ID));
		}
	}
}