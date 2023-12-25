package com.min01.fireplace.entity.render;

import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.entity.model.ModelSnowyFeng;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnowyFengRenderer extends MobRenderer<EntitySnowyFeng, ModelSnowyFeng>
{
	public SnowyFengRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelSnowyFeng(p_174304_.bakeLayer(ModelSnowyFeng.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntitySnowyFeng p_115812_)
	{
		return FireplaceUtil.getKaratTexture("snowy_feng");
	}
}
