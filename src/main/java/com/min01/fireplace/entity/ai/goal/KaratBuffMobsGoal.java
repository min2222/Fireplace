package com.min01.fireplace.entity.ai.goal;

import com.min01.fireplace.entity.AbstractKaratFeng.KaratSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class KaratBuffMobsGoal extends AbstractFireplaceSkillGoal<EntityKaratFeng>
{
	public MobEffect[] effects = {MobEffects.ABSORPTION, MobEffects.REGENERATION, MobEffects.DAMAGE_RESISTANCE, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_BOOST, MobEffects.HEALTH_BOOST, MobEffects.INVISIBILITY, MobEffects.SLOW_FALLING};
	public KaratBuffMobsGoal(EntityKaratFeng mob)
	{
		super(mob);
	}
	
	@Override
	public void start()
	{
		super.start();
		this.mob.setPreparingSkill(true);
	}
	
	@Override
	public void stop()
	{
		super.stop();
		this.mob.setPreparingSkill(false);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.isFlying() && !this.mob.isChangeEquip() && this.mob.getRemainSummoningHP() > 0;
	}

	@Override
	protected void performSkill()
	{
		if(this.mob.entityList.size() > 0)
		{
			int rand = (int)Math.floor(Math.random() * this.mob.entityList.size());
			LivingEntity living = this.mob.entityList.get(rand);
			if(living.isAlive())
			{
				living.addEffect(new MobEffectInstance(this.effects[getRandomNumber(0, 7)], getRandomNumber(60, 200), getRandomNumber(0, 1)));
			}
		}
	}
	
    public static int getRandomNumber(int min, int max) 
    {
        return (int) ((Math.random() * (max - min)) + min);
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
			return 100;
		}
		else if(this.mob.getPhase() == 1)
		{
			return 80;
		}
		else 
		{
			return 40;
		}
	}

	@Override
	protected KaratSkills getSkills() 
	{
		return KaratSkills.KARAT_BUFF_MOBS;
	}
}
