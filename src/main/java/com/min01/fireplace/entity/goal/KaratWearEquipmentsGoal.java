package com.min01.fireplace.entity.goal;

import com.min01.fireplace.entity.AbstractFireplaceMember.ActiveMemberSkills;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.init.FireplaceItems;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class KaratWearEquipmentsGoal extends AbstractFireplaceSkillGoal
{
	public Item[] diamondSet = {Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.SHIELD, Items.DIAMOND_SWORD};
	public Item[] netheriteSet = {Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS};
	public EquipmentSlot[] netheriteSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	public EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND};
	public int equipCount;
	public int slotCount;
	
	public KaratWearEquipmentsGoal(EntityKaratFeng mob) 
	{
		super(mob);
	}

    @Override
    public boolean canUse() 
    {
    	LivingEntity livingentity = this.mob.getTarget();
    	boolean flag = ((EntityKaratFeng) this.mob).stopFlying() || ((EntityKaratFeng) this.mob).shouldChangeEquip();
    	if (livingentity != null && livingentity.isAlive()) 
    	{
    		return this.mob.tickCount >= this.nextAttackTickCount && flag;
    	} 
    	else 
    	{
    		return false;
    	}
    }

    @Override
    public void start()
    {
    	super.start();
    	this.mob.setInvulnerable(true);
    	if(this.mob.getPhase() == 0 || ((EntityKaratFeng) this.mob).stopFlying())
    	{
        	this.mob.setShouldMove(false);
    	}
		if(((EntityKaratFeng) this.mob).shouldChangeEquip())
    	{
    		this.equipCount = 0;
    		this.slotCount = 0;
    	}
    }
    
    @Override
    public void stop()
    {
    	super.stop();
    	if(((EntityKaratFeng) this.mob).shouldChangeEquip())
    	{
    		this.mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
    		((EntityKaratFeng) this.mob).setShouldChangeEquip(false);
    	}
    	this.mob.setInvulnerable(false);
    }

	@Override
    protected void performSkill()
    {
		if(this.mob.getPhase() == 0)
		{
			this.changeEquipment(6, this.mob.getMainHandItem().getItem() == FireplaceItems.KING_STAFF.get(), this.diamondSet, this.slots, SoundEvents.ARMOR_EQUIP_DIAMOND);
		}
		else if(this.mob.getPhase() == 1)
		{
			if(((EntityKaratFeng) this.mob).shouldChangeEquip())
			{
				this.changeEquipment(4, this.mob.getMainHandItem().getItem() == Items.DIAMOND_SWORD, this.netheriteSet, this.netheriteSlots, SoundEvents.ARMOR_EQUIP_NETHERITE);
			}
			else
			{
        		this.mob.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
			}
		}
		else if(this.mob.getPhase() == 2)
		{
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
			handStack.enchant(Enchantments.SHARPNESS, 5);
			helmetStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			chestStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			legsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			feetsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			Item[] enchantedNetheriteSet = {helmetStack.getItem(), chestStack.getItem(), legsStack.getItem(), feetsStack.getItem(), handStack.getItem()};
			
			if(((EntityKaratFeng) this.mob).shouldChangeEquip())
			{
				this.changeEquipment(4, this.mob.getMainHandItem().getItem() == Items.NETHERITE_AXE, enchantedNetheriteSet, this.netheriteSlots, SoundEvents.ARMOR_EQUIP_NETHERITE);
			}
			else
			{
        		this.mob.setItemSlot(EquipmentSlot.MAINHAND, handStack);
			}
		}
    }
	
	private void changeEquipment(int maxCount, boolean firstFlag, Item[] item, EquipmentSlot[] slots, SoundEvent sound)
	{
		if(this.mob.getMainHandItem().isEmpty() || firstFlag || this.mob.getMainHandItem().getItem() == FireplaceItems.KING_STAFF.get())
		{
			if(this.equipCount < maxCount) 
			{
        		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
        		ItemStack itemstack = new ItemStack(item[this.equipCount]);
        		this.mob.setItemSlot(EquipmentSlot.MAINHAND, itemstack);	
        		this.equipCount++;
			}
		}
		else if(this.mob.getMainHandItem().getItem() == item[this.equipCount - 1])
		{
    		this.mob.level.playLocalSound(this.mob.getX(), this.mob.getY(), this.mob.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F, false);
    		
    		this.mob.setItemSlot(slots[this.slotCount], this.mob.getMainHandItem());
    		this.mob.swing(InteractionHand.MAIN_HAND);
    		
        	if(this.equipCount < maxCount && this.slotCount < maxCount)
        	{
        		this.skillWarmupDelay = this.adjustedTickDelay(this.getSkillWarmupTime());
        		this.slotCount++;
        	}
        	
        	if(this.mob.getMainHandItem().getItem() != item[item.length - 1])
        	{
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
