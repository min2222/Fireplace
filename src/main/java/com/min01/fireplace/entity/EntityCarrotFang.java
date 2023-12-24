package com.min01.fireplace.entity;

import com.min01.fireplace.entity.projectile.EntityCarrotProjectile;
import com.min01.fireplace.init.FireplaceEntities;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EntityCarrotFang extends AbstractNonBossKarat
{
	public EntityCarrotFang(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CARROT));
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if(this.getTarget() != null)
		{
			if(this.distanceTo(this.getTarget()) <= 6)
			{
				this.setCanMoveToTarget(false);
				if(this.tickCount % 20 == 0)
				{
					EntityCarrotProjectile carrot = new EntityCarrotProjectile(FireplaceEntities.CARROT.get(), this, this.level);
					FireplaceUtil.shootFromRotation(carrot, this, this.getXRot(), this.getYRot(), 0.0F, 1.5F, 1.0F, false);
					carrot.setPos(this.position().add(0, this.getEyeHeight(), 0));
					this.swing(InteractionHand.MAIN_HAND);
					this.level.addFreshEntity(carrot);
				}
			}
			else
			{
				this.setCanMoveToTarget(true);
			}
		}
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 20.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.65D)
        		.add(Attributes.ARMOR, 2)
        		.add(Attributes.ARMOR_TOUGHNESS, 2);
    }
}
