package com.min01.fireplace.entity.goal;

import java.util.ArrayList;

import com.min01.fireplace.entity.AbstractKaratFeng.KaratSkills;
import com.min01.fireplace.entity.EntityNecroFeng;
import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.network.KaratDataSyncPacket;
import com.min01.fireplace.network.KaratDataSyncPacket.DataType;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.registries.ForgeRegistries;

public class NecroFengSummonUndeadGoal extends AbstractFireplaceSkillGoal<EntityNecroFeng>
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public NecroFengSummonUndeadGoal(EntityNecroFeng mob)
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setPreparingSkill(true);
		for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
		{
			if(type != EntityType.WITHER)
			{
				Entity entity = type.create(this.mob.level);
				if(entity instanceof LivingEntity living)
				{
					if(living.getMobType().equals(MobType.UNDEAD) || living.isInvertedHealAndHarm())
					{
						if(living.getAttribute(Attributes.ATTACK_DAMAGE) != null && living.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) > 0)
						{
							this.entityList.add(living);
						}
					}
				}
			}
		}
		
		if(!this.entityList.isEmpty())
		{
			int rand = (int)Math.floor(Math.random()*this.entityList.size());
			LivingEntity living2 = this.entityList.get(rand);
			if(!this.mob.entityList.contains(living2))
			{
				this.mob.entityList.add(living2);
				FireplaceNetwork.sendToAll(new KaratDataSyncPacket(DataType.NECRO_FENG_SUMMONING, this.mob.getId()));
			}
		}
	}

	@Override
	protected void performSkill() 
	{
		EntityNecroFeng karat = this.mob;
		karat.level.playSound(null, karat.blockPosition(), SoundEvents.EVOKER_PREPARE_WOLOLO, karat.getSoundSource(), 1, 1);
		if(!karat.entityList.isEmpty())
		{
			LivingEntity living = karat.entityList.get(karat.entityList.size() - 1);
			if(living instanceof Mob)
			{
				Mob mob = (Mob) living;
				mob.setTarget(karat.getTarget());
	    		double spawnRange = 5;
	            double x = (double) karat.getX() + (karat.level.random.nextDouble() - karat.level.random.nextDouble()) * (double)spawnRange + 0.5D;
	            double z = (double) karat.getZ() + (karat.level.random.nextDouble() - karat.level.random.nextDouble()) * (double)spawnRange + 0.5D;
	            
				mob.setPos(x, karat.getY(), z);
				mob.setNoAi(true);
				mob.setInvulnerable(true);
				mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10000000, 1));
				mob.getPersistentData().putUUID(FireplaceUtil.NECRO_UUID, karat.getUUID());
				FireplaceUtil.NECRO_LIST.add(mob);
				mob.finalizeSpawn((ServerLevelAccessor) this.mob.getLevel(), this.mob.getLevel().getCurrentDifficultyAt(this.mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				
				if(karat.getTeam() != null)
				{
					PlayerTeam karatTeam = karat.getServer().getScoreboard().getPlayerTeam(karat.getTeam().getName());
					karat.getServer().getScoreboard().addPlayerToTeam(mob.getStringUUID(), karatTeam);
				}
				karat.level.addFreshEntity(mob);
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
		return 40;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}

	@Override
	protected KaratSkills getSkills() 
	{
		return KaratSkills.NECRO_FENG_SUMMONING;
	} 
}
