package com.min01.fireplace.entity.render;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.model.ModelKaratFeng;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class KaratFengRenderer extends AbstractKaratFengRenderer<EntityKaratFeng, PlayerModel<EntityKaratFeng>>
{
	public KaratFengRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelKaratFeng<>(p_174304_.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityKaratFeng p_115812_) 
	{
		return new ResourceLocation(Fireplace.MODID, "textures/entity/karat_feng.png");
	}
}
