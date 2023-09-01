package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class KaratSplashPotionsGoal extends AbstractFireplaceSkillGoal
{
	public Item prevItem;
	public KaratSplashPotionsGoal(AbstractFireplaceMember mob)
	{
		super(mob);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && ((EntityKaratFeng) this.mob).stopFlying() && this.mob.getPhase() > 0 && this.mob.distanceTo(this.mob.getTarget()) > 4 && !this.mob.hasEffect(MobEffects.DAMAGE_BOOST);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.swing(InteractionHand.MAIN_HAND);
		this.mob.playSound(SoundEvents.SPLASH_POTION_THROW, 1.0F, 1.0F);
		this.prevItem = this.mob.getMainHandItem().copy().getItem();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_STRENGTH));
	}

	@Override
	protected void performSkill() 
	{
		this.mob.level.levelEvent(2007, this.mob.blockPosition(), PotionUtils.getColor(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_STRENGTH)));
		this.mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1));
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this.prevItem));
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 10;
	}

	@Override
	protected int getSkillTime() 
	{
		return 15;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}

	@Override
	protected ActiveMemberSkills getSkills()
	{
		return ActiveMemberSkills.KARAT_SPLASH_POTIONS;
	}
}
