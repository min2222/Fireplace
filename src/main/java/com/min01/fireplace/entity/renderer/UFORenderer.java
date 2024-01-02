package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityUFO;
import com.min01.fireplace.entity.model.ModelUFO;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class UFORenderer extends MobRenderer<EntityUFO, ModelUFO>
{
	public UFORenderer(Context p_174304_)
	{
		super(p_174304_, new ModelUFO(p_174304_.bakeLayer(ModelUFO.LAYER_LOCATION)), 0);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityUFO p_115812_) 
	{
		return new ResourceLocation(Fireplace.MODID, "textures/entity/ufo.png");
	}
}
