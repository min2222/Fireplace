package com.min01.fireplace.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.Mob;

public abstract class AbstractKaratFengRenderer<T extends Mob, M extends PlayerModel<T>> extends MobRenderer<T, M>
{
	public AbstractKaratFengRenderer(Context p_174304_, M p_174305_, float p_174306_)
	{
		super(p_174304_, p_174305_, p_174306_);
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(p_174304_.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(p_174304_.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
		this.addLayer(new ArrowLayer<>(p_174304_, this));
		this.addLayer(new CustomHeadLayer<>(this, p_174304_.getModelSet(), p_174304_.getItemInHandRenderer()));
		this.addLayer(new BeeStingerLayer<>(this));
	}

	@Override
	protected void scale(T p_115314_, PoseStack p_115315_, float p_115316_) 
	{
		p_115315_.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
