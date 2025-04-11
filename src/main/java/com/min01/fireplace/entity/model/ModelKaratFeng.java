package com.min01.fireplace.entity.model;

import com.min01.fireplace.entity.AbstractKaratFeng;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ModelKaratFeng<T extends AbstractKaratFeng> extends PlayerModel<T>
{
	public ModelKaratFeng(ModelPart p_170821_, boolean p_170822_) 
	{
		super(p_170821_, p_170822_);
	}

	@Override
	public void setupAnim(T entity, float p_103396_, float p_103397_, float ageInTicks, float p_103399_, float p_103400_) 
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
			this.leftSleeve.copyFrom(this.leftArm);
			this.rightSleeve.copyFrom(this.rightArm);
		}
	}
}
