package com.min01.fireplace.entity.goal;

import java.util.ArrayList;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class KaratSummonMobGoal extends AbstractFireplaceSkillGoal
{
	public EntityKaratFeng mob;
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public KaratSummonMobGoal(AbstractFireplaceMember mob)
	{
		super(mob);
		this.mob = (EntityKaratFeng) mob;
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && ((EntityKaratFeng)this.mob).getRemainSummoningHP() > 0;
	}
	
	@Override
	public void start()
	{
		super.start();
		for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
		{
			if(type != EntityType.ENDER_DRAGON)
			{
				Entity entity = type.create(this.mob.level);
				if(entity instanceof LivingEntity)
				{
					ResourceLocation resourcelocation = EntityType.getKey(type);
					if(FireplaceConfig.useBlackListForSummoning.get())
					{
						if(!FireplaceConfig.karatSummoningBlackList.get().contains(entity.getEncodeId()) && !FireplaceConfig.karatSummoningBlackList.get().contains(resourcelocation.toString().split(":")[0]))
						{
							LivingEntity living = (LivingEntity) entity;
							if(living.getMaxHealth() <= ((EntityKaratFeng) this.mob).getRemainSummoningHP())
							{
								if(living instanceof Mob && living.getAttribute(Attributes.ATTACK_DAMAGE) != null && !((Mob)living).isNoAi())
								{
									this.entityList.add(living);
								}
							}
						}
					}
					else
					{
						if(FireplaceConfig.karatSummoningWhiteList.get().contains(resourcelocation.toString().split(":")[0]))
						{
							LivingEntity living = (LivingEntity) entity;
							if(living.getMaxHealth() <= ((EntityKaratFeng) this.mob).getRemainSummoningHP())
							{
								if(living instanceof Mob && living.getAttribute(Attributes.ATTACK_DAMAGE) != null && !((Mob)living).isNoAi())
								{
									this.entityList.add(living);
								}
							}
						}
					}
				}
			}
		}
		
		if(!this.entityList.isEmpty())
		{
			int rand = (int)Math.floor(Math.random()*this.entityList.size());
			LivingEntity living2 = this.entityList.get(rand);
			if(!((EntityKaratFeng) this.mob).entityList.contains(living2))
			{
				((EntityKaratFeng) this.mob).entityList.add(living2);
			}
		}
	}

	@Override
	protected void performSkill() 
	{
		EntityKaratFeng karat = (EntityKaratFeng) this.mob;
		if(!karat.entityList.isEmpty())
		{
			LivingEntity living = karat.entityList.get(karat.entityList.size() - 1);
			if(living instanceof Mob)
			{
				Mob mob = (Mob) living;
				karat.setRemainSummoningHP(karat.getRemainSummoningHP() - mob.getMaxHealth());
				mob.setTarget(karat.getTarget());
				if(!karat.level.isClientSide)
				{
					double d0 = this.mob.getTarget().getX() - this.mob.getX();
					double d1 = this.mob.getTarget().getY() - mob.getY();
					double d2 = this.mob.getTarget().getZ() - this.mob.getZ();
					double d3 = Math.sqrt(d0 * d0 + d2 * d2);
					mob.setPos(karat.position());
					mob.getPersistentData().putUUID(FireplaceUtil.KARAT_UUID, karat.getUUID());
					mob.finalizeSpawn((ServerLevelAccessor) this.mob.getLevel(), this.mob.getLevel().getCurrentDifficultyAt(this.mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
					this.shoot(mob, d0, d1 + d3 * 0.2D, d2, 1.6F, 1);
					karat.level.addFreshEntity(mob);
				}
			}
		}
	}
	
	public void shoot(Entity entity, double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_)
	{
		Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_)).scale((double)p_37269_);
		entity.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		entity.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
		entity.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.entityList.clear();
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
			return 200;
		}
		else if(this.mob.getPhase() == 1)
		{
			return 100;
		}
		else 
		{
			return 50;
		}
	}

	@Override
	protected ActiveMemberSkills getSkills() 
	{
		return ActiveMemberSkills.KARAT_SUMMON_MOB;
	} 
}
