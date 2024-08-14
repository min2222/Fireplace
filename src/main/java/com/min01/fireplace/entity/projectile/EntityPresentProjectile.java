package com.min01.fireplace.entity.projectile;

import java.util.List;

import com.google.common.collect.Lists;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.init.FireplaceEntities;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPresentProjectile extends ThrowableProjectile
{
	//red = explosive
	//green = firework
	//blue = snowy feng
	
	public static final EntityDataAccessor<Integer> PRESENT_ID = SynchedEntityData.defineId(EntityPresentProjectile.class, EntityDataSerializers.INT);
	
	public EntityPresentProjectile(EntityType<? extends ThrowableProjectile> p_37442_, Level p_37443_)
	{
		super(p_37442_, p_37443_);
	}

	public EntityPresentProjectile(EntityType<? extends ThrowableProjectile> p_37432_, double p_37433_, double p_37434_, double p_37435_, Level p_37436_) 
	{
		super(p_37432_, p_37433_, p_37434_, p_37435_, p_37436_);
	}

	public EntityPresentProjectile(EntityType<? extends ThrowableProjectile> p_37438_, LivingEntity p_37439_, Level p_37440_) 
	{
		super(p_37438_, p_37439_, p_37440_);
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(PRESENT_ID, 1);
	}
	
	@Override
	protected void onHit(HitResult p_37260_) 
	{
		super.onHit(p_37260_);
		if(this.getOwner() != null)
		{
			switch(this.getPresentType())
			{
			case BLUE:
				this.bluePresentHit();
				break;
			case GREEN:
				this.greenPresentHit();
				break;
			case RED:
				this.redPresentHit();
				break;
			}
		}
		this.discard();
	}
	
	public PresentType getPresentType()
	{
		return PresentType.values()[this.entityData.get(PRESENT_ID)];
	}
	
	public void setPresentType(PresentType type)
	{
		this.entityData.set(PRESENT_ID, type.ordinal());
	}
	
	public void bluePresentHit()
	{
		this.spawnEntityBurst();
	}
	
	public void greenPresentHit()
	{
		this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
		this.level.broadcastEntityEvent(this, (byte) 17);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5));
		for(int i = 0; i < list.size(); i++)
		{
			LivingEntity living = list.get(i);
			boolean flag = living instanceof AbstractKaratFeng karat ? karat.getCurrentRaid() == null : true;
			if(flag && living != this.getOwner())
			{
				living.hurt(DamageSource.explosion((LivingEntity) this.getOwner()), (this.tickCount / 20) % 1);
			}
		}
	}
	
	public void redPresentHit()
	{
		this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), (this.tickCount / 20) % 2, BlockInteraction.NONE);
	}
	
    private void spawnEntityBurst()
    {
    	double d0 = this.random.nextGaussian() * 0.05D;
        double d1 = this.random.nextGaussian() * 0.05D;

        for(int i = 0; i < 3; ++i) 
        {
			Vec3 vec3 = this.getDeltaMovement();
        	double d2 = vec3.x * 0.5D + this.random.nextGaussian() * 0.15D + d0;
        	double d4 = vec3.y * 0.5D + this.random.nextDouble() * 0.5D;
        	double d3 = vec3.z * 0.5D + this.random.nextGaussian() * 0.15D + d1;
        	EntitySnowyFeng snowyFeng = new EntitySnowyFeng(FireplaceEntities.SNOWY_FENG.get(), this.level);
        	snowyFeng.setPos(this.position().add(0, 0.5F, 0));
        	snowyFeng.setDeltaMovement(d2, d4, d3);
        	if(this.getOwner() != null && ((AbstractKaratFeng) this.getOwner()).getCurrentRaid() != null)
        	{
        		snowyFeng.setCurrentRaid(((AbstractKaratFeng) this.getOwner()).getCurrentRaid());
        	}
        	this.level.addFreshEntity(snowyFeng);
        }
    }
	
	@Override
	public void handleEntityEvent(byte p_19882_)
	{
		if(p_19882_ == 17)
		{
			DyeColor dyecolor = Util.getRandom(DyeColor.values(), this.random);
			int i = this.random.nextInt(3);
			ItemStack itemstack = this.getFirework(dyecolor, i);
			Vec3 vec3 = this.getDeltaMovement();
            this.level.createFireworks(this.getX(), this.getY(), this.getZ(), vec3.x, vec3.y, vec3.z, itemstack.getTagElement("Fireworks"));
		}
	}
	
	private ItemStack getFirework(DyeColor p_22697_, int p_22698_)
	{
		ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 1);
		ItemStack itemstack1 = new ItemStack(Items.FIREWORK_STAR);
		CompoundTag compoundtag = itemstack1.getOrCreateTagElement("Explosion");
		List<Integer> list = Lists.newArrayList();
		list.add(p_22697_.getFireworkColor());
		compoundtag.putIntArray("Colors", list);
		compoundtag.putByte("Type", (byte)FireworkRocketItem.Shape.BURST.getId());
		CompoundTag compoundtag1 = itemstack.getOrCreateTagElement("Fireworks");
		ListTag listtag = new ListTag();
		CompoundTag compoundtag2 = itemstack1.getTagElement("Explosion");
		if(compoundtag2 != null)
		{
			listtag.add(compoundtag2);
		}

		compoundtag1.putByte("Flight", (byte)p_22698_);
		if(!listtag.isEmpty())
		{
			compoundtag1.put("Explosions", listtag);
		}

		return itemstack;
	}
	
	public static enum PresentType
	{
		RED,
		GREEN,
		BLUE;
	}
}
