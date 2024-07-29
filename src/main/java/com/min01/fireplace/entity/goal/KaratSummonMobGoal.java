package com.min01.fireplace.entity.goal;

import java.util.ArrayList;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.AbstractKaratFeng.KaratSkills;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.network.KaratDataSyncPacket;
import com.min01.fireplace.network.KaratDataSyncPacket.DataType;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;

public class KaratSummonMobGoal extends AbstractFireplaceSkillGoal<EntityKaratFeng>
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public KaratSummonMobGoal(EntityKaratFeng mob)
	{
		super(mob);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.isFlying() && !this.mob.isChangeEquip() && this.mob.getRemainSummoningHP() > 0;
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setPreparingSkill(true);
		for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
		{
			if(type != EntityType.ENDER_DRAGON)
			{
				Entity entity = type.create(this.mob.level);
				if(entity instanceof LivingEntity)
				{
					if(!(entity instanceof AbstractKaratFeng))
					{
						ResourceLocation resourcelocation = EntityType.getKey(type);
						if(FireplaceConfig.useBlackListForSummoning.get())
						{
							if(!FireplaceConfig.karatSummoningBlackList.get().contains(entity.getEncodeId()) && !FireplaceConfig.karatSummoningBlackList.get().contains(resourcelocation.toString().split(":")[0]))
							{
								LivingEntity living = (LivingEntity) entity;
								if(living.getMaxHealth() <= this.mob.getRemainSummoningHP())
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
								if(living.getMaxHealth() <= this.mob.getRemainSummoningHP())
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
		}
		
		if(!this.entityList.isEmpty())
		{
			int rand = (int)Math.floor(Math.random() * this.entityList.size());
			LivingEntity living2 = this.entityList.get(rand);
			if(!this.mob.entityList.contains(living2))
			{
				this.mob.entityList.add(living2);
				FireplaceNetwork.sendToAll(new KaratDataSyncPacket(DataType.ENTITY_LIST, this.mob.getId()));
			}
		}
	}

	@Override
	protected void performSkill() 
	{
		if(!this.mob.entityList.isEmpty())
		{
			LivingEntity living = this.mob.entityList.get(this.mob.entityList.size() - 1);
			if(living instanceof Mob)
			{
				Mob mob = (Mob) living;
				this.mob.setRemainSummoningHP(this.mob.getRemainSummoningHP() - mob.getMaxHealth());
				mob.setTarget(this.mob.getTarget());
				if(!this.mob.level.isClientSide)
				{
					FireplaceUtil.shootFromRotation(mob, this.mob, this.mob.getXRot(), this.mob.getYRot(), 0.0F, 1.5F, 1.0F);
					mob.setPos(this.mob.position());
					mob.getPersistentData().putUUID(FireplaceUtil.KARAT_UUID, this.mob.getUUID());
					ForgeEventFactory.onFinalizeSpawn(this.mob, (ServerLevelAccessor) this.mob.level, this.mob.level.getCurrentDifficultyAt(this.mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
					if(this.mob.getTeam() != null)
					{
						PlayerTeam karatTeam = this.mob.getServer().getScoreboard().getPlayerTeam(this.mob.getTeam().getName());
						this.mob.getServer().getScoreboard().addPlayerToTeam(mob.getStringUUID(), karatTeam);
					}
					this.mob.level.addFreshEntity(mob);
				}
			}
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.entityList.clear();
		this.mob.setPreparingSkill(false);
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
			return 80;
		}
	}

	@Override
	protected KaratSkills getSkills() 
	{
		return KaratSkills.KARAT_SUMMON_MOB;
	} 
}
