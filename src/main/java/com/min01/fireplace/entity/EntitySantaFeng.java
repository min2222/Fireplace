package com.min01.fireplace.entity;

import com.min01.fireplace.entity.projectile.EntityPresentProjectile;
import com.min01.fireplace.entity.projectile.EntityPresentProjectile.PresentType;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySantaFeng extends AbstractHostileKaratFeng
{
	public EntitySantaFeng(EntityType<? extends Monster> p_21368_, Level p_21369_)
	{
		super(p_21368_, p_21369_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return AbstractKaratFeng.createFireplaceAttributes()
    			.add(Attributes.MAX_HEALTH, 100.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.55D)
    			.add(Attributes.ATTACK_DAMAGE, 3.0D)
        		.add(Attributes.ARMOR, 3)
        		.add(Attributes.ARMOR_TOUGHNESS, 3);
    }
    
    @Override
    public void aiStep() 
    {
    	super.aiStep();
		if(this.getTarget() != null)
		{
			if(this.distanceTo(this.getTarget()) <= 12)
			{
				this.setCanMoveToTarget(false);
				float tick = this.getHealth() == this.getHealth() / 2 ? 5 : 35;
				if(this.tickCount % tick == 0)
				{
					EntityPresentProjectile present = new EntityPresentProjectile(FireplaceEntities.PRESENT.get(), this, this.level);
					present.setPos(this.position().add(0, this.getEyeHeight(), 0));
					present.setPresentType(Util.getRandom(PresentType.values(), this.random));
					Vec3 motion = FireplaceUtil.getEntityShootVector(present, this.getTarget());
					present.setDeltaMovement(motion);
					this.swing(InteractionHand.MAIN_HAND);
					this.level.addFreshEntity(present);
				}
			}
			else
			{
				this.setCanMoveToTarget(true);
			}
		}
    }
}
