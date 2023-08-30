package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.init.FireplaceItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class KaratWearEquipmentsGoal extends AbstractFireplaceSkillGoal
{
	public Item[] equipItems1 = {Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.SHIELD};
	public Item[] equipItems2 = {Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS};
	public EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND};
	public int equipCount;
	public int slotCount;
	
	public KaratWearEquipmentsGoal(EntityKaratFeng mob) 
	{
		super(mob);
	}

    @Override
    public boolean canUse() 
    {
    	boolean flag = this.mob.getPhase() == 0 && this.mob.getMainHandItem().getItem() == FireplaceItems.KING_STAFF.get() && ((EntityKaratFeng) this.mob).stopFlying();
    	return super.canUse() && flag || ((EntityKaratFeng) this.mob).shouldChangeEquip();
    }

    @Override
    public void start()
    {
    	this.mob.setInvulnerable(true);
    	if(!((EntityKaratFeng) this.mob).shouldChangeEquip())
    	{
        	this.mob.setShouldMove(false);
    	}
    	else if(((EntityKaratFeng) this.mob).shouldChangeEquip())
    	{
    		this.equipCount = 0;
    		this.slotCount = 0;
    	}
    	super.start();
    }
    
    @Override
    public void stop()
    {
    	super.stop();
    	if(this.mob.getPhase() == 0)
    	{
    		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
    	}
    	else if(this.mob.getPhase() == 1 && this.mob.getMainHandItem().getItem() == Items.AIR)
    	{
    		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
    	}
    	else if(this.mob.getPhase() == 2 && this.mob.getMainHandItem().getItem() == Items.AIR)
    	{
    		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
    	}
    	if(((EntityKaratFeng) this.mob).shouldChangeEquip())
    	{
    		((EntityKaratFeng) this.mob).setShouldChangeEquip(false);
    	}
    	this.mob.setInvulnerable(false);
    }

	@Override
    protected void performSkill()
    {
		if(this.mob.getPhase() == 0)
		{
    		if(this.mob.getMainHandItem().getItem() == Items.AIR || this.mob.getMainHandItem().getItem() == FireplaceItems.KING_STAFF.get())
    		{
            	if(this.equipCount < 5 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
            		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
            		ItemStack itemstack = new ItemStack(this.equipItems1[this.equipCount]);
            		this.mob.setItemInHand(InteractionHand.MAIN_HAND, itemstack);	
            		this.equipCount++;
            	}
    		}
    		else
    		{
        		this.mob.level.playLocalSound(this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.ARMOR_EQUIP_DIAMOND, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        		this.mob.setItemSlot(this.slots[this.slotCount], this.mob.getMainHandItem());
        		this.mob.swing(InteractionHand.MAIN_HAND);
            	if(this.equipCount < 5 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
            		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
            		this.slotCount++;
            	}
        		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.AIR));
    		}
		}
		else if(this.mob.getPhase() == 1)
		{
    		if(this.mob.getMainHandItem().getItem() == Items.AIR || this.mob.getMainHandItem().getItem() == Items.DIAMOND_SWORD)
    		{
            	if(this.equipCount < 4 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
            		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
            		ItemStack itemstack = new ItemStack(this.equipItems2[this.equipCount]);
            		this.mob.setItemInHand(InteractionHand.MAIN_HAND, itemstack);
            		this.equipCount++;
            	}
    		}
    		else
    		{
        		this.mob.level.playLocalSound(this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        		this.mob.setItemSlot(this.slots[this.slotCount], this.mob.getMainHandItem());
        		this.mob.swing(InteractionHand.MAIN_HAND);
            	if(this.equipCount < 4 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
            		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
            		this.slotCount++;
            	}
        		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.AIR));
    		}
		}
		else if(this.mob.getPhase() == 2)
		{
    		if(this.mob.getMainHandItem().getItem() == Items.AIR || this.mob.getMainHandItem().getItem() == Items.NETHERITE_AXE)
    		{
            	if(this.equipCount < 4 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
	        		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
	    			Item enchantedNetheriteHelmet = Items.NETHERITE_HELMET;
	    			Item enchantedNetheriteChestplate = Items.NETHERITE_CHESTPLATE;
	    			Item enchantedNetheriteLeggings = Items.NETHERITE_LEGGINGS;
	    			Item enchantedNetheriteBoots = Items.NETHERITE_BOOTS;
	    			ItemStack helmetStack = new ItemStack(enchantedNetheriteHelmet);
	    			ItemStack chestStack = new ItemStack(enchantedNetheriteChestplate);
	    			ItemStack legsStack = new ItemStack(enchantedNetheriteLeggings);
	    			ItemStack feetsStack = new ItemStack(enchantedNetheriteBoots);
	    			helmetStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
	    			chestStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
	    			legsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
	    			feetsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
	    			ItemStack[] equipItems3 = {helmetStack, chestStack, legsStack, feetsStack};
	        		ItemStack itemstack = equipItems3[this.equipCount];
	        		this.mob.setItemInHand(InteractionHand.MAIN_HAND, itemstack);
	        		this.equipCount++;
            	}
    		}   		
    		else
    		{
        		this.mob.level.playLocalSound(this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        		this.mob.setItemSlot(this.slots[this.slotCount], this.mob.getMainHandItem());
        		this.mob.swing(InteractionHand.MAIN_HAND);
            	if(this.equipCount < 4 || this.mob.getMainHandItem().getItem() == Items.SHIELD)
            	{
            		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
            		this.slotCount++;
            	}
        		this.mob.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.AIR));
    		}
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
