package com.min01.fireplace.entity.model;

import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.item.UseAnim;

public class ModelKaratFeng extends PlayerModel<EntityKaratFeng>
{
	public ModelKaratFeng(ModelPart p_170821_, boolean p_170822_) 
	{
		super(p_170821_, p_170822_);
	}

	@Override
	public void setupAnim(EntityKaratFeng entity, float p_103396_, float p_103397_, float ageInTicks, float p_103399_, float p_103400_) 
	{
		super.setupAnim(entity, p_103396_, p_103397_, ageInTicks, p_103399_, p_103400_);
		if(entity.isPreparingSkill())
		{
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 2.3561945F;
			this.leftArm.zRot = -2.3561945F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		}
		if(entity.isUsingItem() && entity.getUseItem().getUseAnimation() == UseAnim.BOW)
		{
            this.rightArm.yRot = -0.1F + this.head.yRot;
            this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
            this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
            this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
		}
	}
}
