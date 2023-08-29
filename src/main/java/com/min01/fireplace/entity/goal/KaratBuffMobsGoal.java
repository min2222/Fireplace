package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class KaratBuffMobsGoal extends AbstractFireplaceSkillGoal
{
	public MobEffect[] effects = {MobEffects.ABSORPTION, MobEffects.REGENERATION, MobEffects.DAMAGE_RESISTANCE, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_BOOST, MobEffects.HEALTH_BOOST, MobEffects.INVISIBILITY, MobEffects.SLOW_FALLING};
	public KaratBuffMobsGoal(AbstractFireplaceMember mob)
	{
		super(mob);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && ((EntityKaratFeng)this.mob).getRemainSummoningHP() > 0;
	}

	@Override
	protected void performSkill()
	{
		EntityKaratFeng karat = (EntityKaratFeng) this.mob;
		if(karat.entityList.size() > 0)
		{
			int rand = (int)Math.floor(Math.random()*karat.entityList.size());
			LivingEntity living = karat.entityList.get(rand);
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
	protected ActiveMemberSkills getSkills() 
	{
		return ActiveMemberSkills.KARAT_BUFF_MOBS;
	}
}
