package com.min01.fireplace.entity.renderer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class SimpleKaratFengRenderer<T extends Mob, M extends PlayerModel<T>> extends AbstractKaratFengRenderer<T, M>
{
	private final ResourceLocation texture;
	
	public SimpleKaratFengRenderer(Context p_174304_, M p_174305_, ResourceLocation location)
	{
		super(p_174304_, p_174305_, 0.5F);
		this.texture = location;
	}

	@Override
	public ResourceLocation getTextureLocation(T p_115812_) 
	{
		return texture;
	}
}
