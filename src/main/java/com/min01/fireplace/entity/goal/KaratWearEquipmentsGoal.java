package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.init.FireplaceItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class KaratWearEquipmentsGoal extends AbstractFireplaceSkillGoal
{
	private boolean canUse = true;
	
	public KaratWearEquipmentsGoal(EntityKaratFeng mob) 
	{
		super(mob);
	}

    @Override
    public boolean canUse() 
    {
    	boolean flag = ((EntityKaratFeng) this.mob).stopFlying() || ((EntityKaratFeng) this.mob).shouldChangeEquip();
    	return super.canUse() && flag;
    }
    
    @Override
    public boolean canContinueToUse() 
    {
    	return super.canContinueToUse() && this.canUse;
    }

    @Override
    public void start()
    {
    	this.mob.setInvulnerable(true);
		this.canUse = true;
		if(((EntityKaratFeng) this.mob).getPhase() == 0 || ((EntityKaratFeng) this.mob).stopFlying())
		{
	    	this.mob.setShouldMove(false);
		}
    	super.start();
    }
    
    @Override
    public void stop()
    {
    	super.stop();
    	((EntityKaratFeng) this.mob).setShouldChangeEquip(false);
    	if(!this.mob.shouldMove())
    	{
    		this.mob.setShouldMove(true);
    	}
    	this.mob.setInvulnerable(false);
    }

	@Override
    protected void performSkill()
    {
		switch(this.mob.getPhase())
		{
		case 0:
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_BOOTS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FireplaceItems.KING_STAFF.get());
			this.changeItemWhen(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DIAMOND_BOOTS);
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_LEGGINGS), this.mob.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIAMOND_BOOTS);
			this.changeItemWhen(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DIAMOND_LEGGINGS);
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_CHESTPLATE), this.mob.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.DIAMOND_LEGGINGS);
			this.changeItemWhen(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DIAMOND_CHESTPLATE);
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HELMET), this.mob.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.DIAMOND_CHESTPLATE);
			this.changeItemWhen(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DIAMOND_HELMET);
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD), this.mob.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.DIAMOND_HELMET, true);
			break;
		case 1:
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_BOOTS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DIAMOND_SWORD && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.NETHERITE_BOOTS && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_LEGGINGS), this.mob.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.NETHERITE_BOOTS && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.NETHERITE_LEGGINGS && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_CHESTPLATE), this.mob.getItemBySlot(EquipmentSlot.LEGS).getItem() == Items.NETHERITE_LEGGINGS && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.NETHERITE_CHESTPLATE && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_HELMET), this.mob.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.NETHERITE_CHESTPLATE && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.NETHERITE_HELMET && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(FireplaceItems.KING_STAFF.get()), this.mob.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.NETHERITE_HELMET && !((EntityKaratFeng) this.mob).stopFlying(), true);
			
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.SHIELD), this.mob.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.NETHERITE_HELMET && ((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.SHIELD && ((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE), this.mob.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.SHIELD && ((EntityKaratFeng) this.mob).stopFlying(), true);
			break;
		case 2:
			Item enchantedNetheriteHelmet = Items.NETHERITE_HELMET;
			Item enchantedNetheriteChestplate = Items.NETHERITE_CHESTPLATE;
			Item enchantedNetheriteLeggings = Items.NETHERITE_LEGGINGS;
			Item enchantedNetheriteBoots = Items.NETHERITE_BOOTS;
			Item enchantedNetheriteAxe = Items.NETHERITE_AXE;
			ItemStack helmetStack = new ItemStack(enchantedNetheriteHelmet);
			ItemStack chestStack = new ItemStack(enchantedNetheriteChestplate);
			ItemStack legsStack = new ItemStack(enchantedNetheriteLeggings);
			ItemStack feetsStack = new ItemStack(enchantedNetheriteBoots);
			ItemStack handStack = new ItemStack(enchantedNetheriteAxe);
			helmetStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			chestStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			legsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			feetsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			handStack.enchant(Enchantments.SHARPNESS, 5);
			this.changeItemWhen(EquipmentSlot.MAINHAND, feetsStack, this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.NETHERITE_AXE && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.FEET, feetsStack, this.mob.getItemBySlot(EquipmentSlot.MAINHAND) == feetsStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, legsStack, this.mob.getItemBySlot(EquipmentSlot.FEET) == feetsStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.LEGS, legsStack, this.mob.getItemBySlot(EquipmentSlot.MAINHAND) == legsStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, chestStack, this.mob.getItemBySlot(EquipmentSlot.LEGS) == legsStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.CHEST, chestStack, this.mob.getItemBySlot(EquipmentSlot.MAINHAND) == chestStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, helmetStack, this.mob.getItemBySlot(EquipmentSlot.CHEST)== chestStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.HEAD, helmetStack, this.mob.getItemBySlot(EquipmentSlot.MAINHAND) == helmetStack && !((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(FireplaceItems.KING_STAFF.get()), this.mob.getItemBySlot(EquipmentSlot.HEAD) == helmetStack && !((EntityKaratFeng) this.mob).stopFlying(), true);
			
			this.changeItemWhen(EquipmentSlot.MAINHAND, new ItemStack(Items.SHIELD), this.mob.getItemBySlot(EquipmentSlot.HEAD) == helmetStack && ((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD), this.mob.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.SHIELD && ((EntityKaratFeng) this.mob).stopFlying());
			this.changeItemWhen(EquipmentSlot.MAINHAND, handStack, this.mob.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.SHIELD && ((EntityKaratFeng) this.mob).stopFlying(), true);
			break;
		}
    }
	
	private void changeItemWhen(EquipmentSlot slot, ItemStack stack, boolean flag)
	{
		this.changeItemWhen(slot, stack, flag, false);
	}
	
	private void changeItemWhen(EquipmentSlot slot, ItemStack stack, boolean flag, boolean finish)
	{
		if(flag)
		{
			if(slot != EquipmentSlot.MAINHAND)
			{
				this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			}
			
			if(!finish) 
			{
	    		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
			}
			else
			{
				this.canUse = false;
			}
				
			this.mob.setItemSlot(slot, stack);
		}
	}

    @Override
    protected int getSkillWarmupTime()
    {
    	return 5;
    }

    @Override
    protected int getSkillTime()
    {
    	return 100;
    }

    @Override
    protected int getSkillUsingInterval()
    {
    	return 20;
    }
    
    @Override
    protected ActiveMemberSkills getSkills()
    {
    	return ActiveMemberSkills.KARAT_WEAR_EQUIPMENTS;
    }
}
