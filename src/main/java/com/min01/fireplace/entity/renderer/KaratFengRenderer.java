package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class KaratFengRenderer extends AbstractKaratFengRenderer<EntityKaratFeng, PlayerModel<EntityKaratFeng>>
{
	public KaratFengRenderer(Context p_174304_)
	{
		super(p_174304_, FireplaceUtil.getKaratModel(p_174304_), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityKaratFeng p_115812_) 
	{
		return FireplaceUtil.getKaratTexture("karat_feng");
	}
}
