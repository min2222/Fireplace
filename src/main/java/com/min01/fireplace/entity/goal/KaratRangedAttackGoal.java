package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractKaratFeng.KaratSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class KaratRangedAttackGoal extends AbstractFireplaceSkillGoal<EntityKaratFeng>
{
	public ItemStack prevItem;
	public ItemStack bowItem;
	public KaratRangedAttackGoal(EntityKaratFeng mob) 
	{
		super(mob);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && !this.mob.isFlying() && !this.mob.isChangeEquip() && this.mob.distanceTo(this.mob.getTarget()) >= 7;
	}
	
	@Override
	public void start() 
	{
		super.start();
		if(this.mob.getPhase() == 2)
		{
			ItemStack bow = new ItemStack(Items.BOW);
			bow.enchant(Enchantments.POWER_ARROWS, 5);
			bow.enchant(Enchantments.FLAMING_ARROWS, 1);
			bow.enchant(Enchantments.PUNCH_ARROWS, 2);
			this.bowItem = bow;
		}
		else
		{
			this.bowItem = new ItemStack(Items.BOW);
		}
		this.prevItem = this.mob.getMainHandItem().copy();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.bowItem);
		this.mob.startUsingItem(InteractionHand.MAIN_HAND);
		this.mob.setCanMove(false);
	}

	@Override
	protected void performSkill() 
	{
		
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.mob.getTarget() != null)
		{
	        int i = this.mob.getTicksUsingItem();
	        if (i >= 18) 
	        {
	           this.mob.stopUsingItem();
	           this.performRangedAttack(this.mob.getTarget(), BowItem.getPowerForTime(i));
	        }
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.stopUsingItem();
		this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.prevItem);
	}
	
	public void performRangedAttack(LivingEntity p_32141_, float p_32142_)
	{
		ItemStack itemstack = this.mob.getProjectile(this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof net.minecraft.world.item.BowItem)));
		AbstractArrow abstractarrow = this.getArrow(itemstack, p_32142_);
		double d0 = p_32141_.getX() - this.mob.getX();
		double d1 = p_32141_.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = p_32141_.getZ() - this.mob.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.mob.level.getDifficulty().getId() * 4));
		this.mob.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.mob.getRandom().nextFloat() * 0.4F + 0.8F));
		this.mob.level.addFreshEntity(abstractarrow);
	}

	protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) 
	{
		return ProjectileUtil.getMobArrow(this.mob, p_32156_, p_32157_);
	}

	@Override
	protected int getSkillTime() 
	{
		return 35;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 20;
	}

	@Override
	protected KaratSkills getSkills() 
	{
		return KaratSkills.KARAT_RANGE;
	}
}
