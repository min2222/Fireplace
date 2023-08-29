package com.min01.fireplace.entity.goal;

import java.util.ArrayList;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

public class KaratSummonMobGoal extends AbstractFireplaceSkillGoal
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public KaratSummonMobGoal(AbstractFireplaceMember mob)
	{
		super(mob);
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
					mob.setPos(karat.getTarget().position());
					mob.getPersistentData().putUUID("karatUUID", karat.getUUID());
					mob.finalizeSpawn((ServerLevelAccessor) this.mob.getLevel(), this.mob.getLevel().getCurrentDifficultyAt(this.mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
					karat.level.addFreshEntity(mob);
				}
			}
		}
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
