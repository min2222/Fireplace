package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember;
import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.mojang.datafixers.util.Pair;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KaratEatingGoldenAppleGoal extends AbstractFireplaceSkillGoal
{
	public ItemStack prevItem;
	public ItemStack goldenApple;
	public KaratEatingGoldenAppleGoal(AbstractFireplaceMember mob) 
	{
		super(mob);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.getHealth() <= this.mob.getMaxHealth() * 50.0F / 100.0F && this.mob.getPhase() > 0;
	}
	
	@Override
	public void start() 
	{
		super.start();
		if(this.mob.getPhase() == 1) 
		{
			this.goldenApple = new ItemStack(Items.GOLDEN_APPLE);
		}
		else if(this.mob.getPhase() == 2)
		{
			this.goldenApple = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
		}
		this.prevItem = this.mob.getMainHandItem().copy();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.goldenApple);
		this.mob.startUsingItem(InteractionHand.MAIN_HAND);
	}

	@Override
	protected void performSkill() 
	{
		
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if (this.shouldTriggerItemUseEffects())
		{
	        this.mob.spawnItemParticles(this.goldenApple, 16);
	        this.mob.playSound(SoundEvents.GENERIC_EAT, 0.5F + 0.5F * (float)this.mob.getRandom().nextInt(2), (this.mob.getRandom().nextFloat() - this.mob.getRandom().nextFloat()) * 0.2F + 1.0F);
		}
	}
	
	private boolean shouldTriggerItemUseEffects()
	{
		int i = this.mob.getUseItemRemainingTicks();
		FoodProperties foodproperties = this.mob.getUseItem().getFoodProperties(this.mob);
		boolean flag = foodproperties != null && foodproperties.isFastFood();
		flag |= i <= this.mob.getUseItem().getUseDuration() - 7;
		return flag && i % 4 == 0;
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.level.playSound((Player)null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, this.mob.getRandom().nextFloat() * 0.1F + 0.9F);
		for(Pair<MobEffectInstance, Float> pair : this.goldenApple.getFoodProperties(this.mob).getEffects()) 
		{
			if (!this.mob.level.isClientSide && pair.getFirst() != null && this.mob.getRandom().nextFloat() < pair.getSecond()) 
			{
				this.mob.addEffect(new MobEffectInstance(pair.getFirst()));
			}
		}
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.prevItem);
	}

	@Override
	protected int getSkillTime() 
	{
		return 32;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 100;
	}

	@Override
	protected ActiveMemberSkills getSkills() 
	{
		return ActiveMemberSkills.KARAT_EAT_GOLDEN_APPLE;
	}
}
