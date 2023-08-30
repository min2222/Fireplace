package com.min01.fireplace.entity.goal;

import java.util.ArrayList;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.registries.ForgeRegistries;

public class KaratShootProjectileGoal extends AbstractFireplaceSkillGoal
{
	public EntityKaratFeng mob;
	public ArrayList<Projectile> projectileList = new ArrayList<>();
	public KaratShootProjectileGoal(AbstractFireplaceMember mob) 
	{
		super(mob);
		this.mob = (EntityKaratFeng) mob;
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && !((EntityKaratFeng) this.mob).stopFlying();
	}
	
	@Override
	public void start() 
	{
		super.start();
		for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
		{
			Entity entity = type.create(this.mob.level);
			if(entity instanceof Projectile && !(entity instanceof FishingHook))
			{
				ResourceLocation resourcelocation = EntityType.getKey(type);
				if(FireplaceConfig.useBlackListForProjectile.get())
				{
					if(!FireplaceConfig.karatProjectileBlackList.get().contains(entity.getEncodeId()) && !FireplaceConfig.karatProjectileBlackList.get().contains(resourcelocation.toString().split(":")[0]))
					{
						Projectile projectile = (Projectile) entity;
						this.projectileList.add(projectile);
					}
				}
				else
				{
					if(FireplaceConfig.karatProjectileWhiteList.get().contains(resourcelocation.toString().split(":")[0]))
					{
						Projectile projectile = (Projectile) entity;
						this.projectileList.add(projectile);
					}
				}
			}
		}
	}

	@Override
	protected void performSkill() 
	{
		if(!this.projectileList.isEmpty())
		{
			int rand = (int)Math.floor(Math.random()*this.projectileList.size());
			Projectile projectile = this.projectileList.get(rand);
			projectile.level.getScoreboard().addPlayerToTeam(projectile.getStringUUID(), this.mob.team);
			projectile.setOwner(this.mob);
			projectile.setPos(this.mob.getX(), this.mob.getEyeY() - 0.1D, this.mob.getZ());
			double d0 = this.mob.getTarget().getX() - this.mob.getX();
			double d1 = this.mob.getTarget().getY() - projectile.getY();
			double d2 = this.mob.getTarget().getZ() - this.mob.getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			projectile.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 1);
			this.mob.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 0.4F / (this.mob.getRandom().nextFloat() * 0.4F + 0.8F));
			this.mob.level.addFreshEntity(projectile);
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.projectileList.clear();
	}

	@Override
	protected int getSkillTime() 
	{
		return 20;
	}

	@Override
	protected int getSkillUsingInterval()
	{
		if(this.mob.getPhase() == 0)
		{
			return 80;
		}
		else if(this.mob.getPhase() == 1)
		{
			return 60;
		}
		else 
		{
			return 20;
		}
	}

	@Override
	protected ActiveMemberSkills getSkills() 
	{
		return ActiveMemberSkills.KARAT_SHOOT_PROJECTILE;
	}
}
