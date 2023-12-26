package com.min01.fireplace.entity.render;

import com.min01.fireplace.entity.EntityFireFeng;
import com.min01.fireplace.entity.model.ModelFireFeng;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class FireFengRenderer extends AbstractKaratFengRenderer<EntityFireFeng, ModelFireFeng>
{
	public FireFengRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelFireFeng(p_174304_.bakeLayer(ModelFireFeng.LAYER_LOCATION)), 0);
	}
	
	@Override
	protected void setupRotations(EntityFireFeng p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) 
	{
		super.setupRotations(p_115317_, p_115318_, p_115319_, p_115320_, p_115321_);
		if(p_115317_.isDashing())
		{
			p_115318_.translate(0, 1, 0);
			p_115318_.mulPose(Vector3f.XP.rotationDegrees(p_115317_.getXRot() - 90));
			p_115318_.mulPose(Vector3f.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityFireFeng p_115812_) 
	{
		return FireplaceUtil.getKaratTexture("fire_feng");
	}
}
