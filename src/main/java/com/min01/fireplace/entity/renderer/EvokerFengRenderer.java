package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.entity.EntityEvokerFeng;
import com.min01.fireplace.entity.model.ModelEvokerFeng;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class EvokerFengRenderer extends AbstractKaratFengRenderer<EntityEvokerFeng, ModelEvokerFeng>
{
	public EvokerFengRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelEvokerFeng(p_174304_.bakeLayer(ModelEvokerFeng.LAYER_LOCATION)), 0.5F);
	}
	
	@Override
	protected void setupRotations(EntityEvokerFeng p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) 
	{
		super.setupRotations(p_115317_, p_115318_, p_115319_, p_115320_, p_115321_);
		if(p_115317_.isAttacking())
		{
			p_115318_.translate(0, 1, 0);
			p_115318_.mulPose(Axis.XP.rotationDegrees(p_115317_.getXRot() - 90));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityEvokerFeng p_115812_)
	{
		return FireplaceUtil.getKaratTexture("evoker_feng");
	}
}
