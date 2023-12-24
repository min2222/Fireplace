package com.min01.fireplace.entity.goal;

import java.util.UUID;

import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.AbstractKaratFeng.KaratSkills;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

public class KaratUsingShieldGoal extends AbstractFireplaceSkillGoal
{
	public int shieldingTimer;
	private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "speed penalty", -0.24, AttributeModifier.Operation.ADDITION);
    
	public KaratUsingShieldGoal(AbstractKaratFeng mob) 
	{
		super(mob);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && ((EntityKaratFeng) this.mob).stopFlying() && this.mob.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.SHIELD && this.mob.getHurtCount() > this.mob.getMaxHurtCount() && this.mob.distanceTo(this.mob.getTarget()) <= 4;
	}

	@Override
	protected void performSkill() 
	{
		
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if (((EntityKaratFeng) this.mob).isShielding()) 
        {
            if (this.shieldingTimer-- <= 0) 
            {
                ((EntityKaratFeng) this.mob).setShielding(false);
                this.mob.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                this.mob.stopUsingItem();
            }
        }
		else
		{
            this.shieldingTimer = 20;
            ((EntityKaratFeng) this.mob).setShielding(true);
            this.mob.startUsingItem(InteractionHand.OFF_HAND);
            AttributeInstance iattributeinstance = this.mob.getAttribute(Attributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(MODIFIER);
            iattributeinstance.addPermanentModifier(MODIFIER);
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.shieldingTimer = 0;
		this.mob.stopUsingItem();
		this.mob.setHurtCount(0);
	}

	@Override
	protected int getSkillTime() 
	{
		return 40;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 10;
	}

	@Override
	protected KaratSkills getSkills()
	{
		return KaratSkills.KARAT_USE_SHIELD;
	}
}
