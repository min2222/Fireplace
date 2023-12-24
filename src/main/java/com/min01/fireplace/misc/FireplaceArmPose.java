package com.min01.fireplace.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.IArmPoseTransformer;

public class FireplaceArmPose implements IArmPoseTransformer 
{
	@Override
	public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm)
	{
		if(entity.isUsingItem())
		{
			float ageInTicks = entity.tickCount + Minecraft.getInstance().getFrameTime();
			model.rightArm.z = 0.0F;
			model.rightArm.x = -5.0F;
			model.leftArm.z = 0.0F;
			model.leftArm.x = 5.0F;
			model.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			model.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			model.rightArm.zRot = 2.3561945F;
			model.leftArm.zRot = -2.3561945F;
			model.rightArm.yRot = 0.0F;
			model.leftArm.yRot = 0.0F;
		}
	}
}
