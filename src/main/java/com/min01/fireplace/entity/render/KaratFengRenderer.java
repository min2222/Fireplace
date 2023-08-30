package com.min01.fireplace.entity.render;

import com.min01.fireplace.Main;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.model.ModelKaratFeng;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.resources.ResourceLocation;

public class KaratFengRenderer extends MobRenderer<EntityKaratFeng, PlayerModel<EntityKaratFeng>>
{
	public KaratFengRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelKaratFeng(p_174304_.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(p_174304_.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(p_174304_.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new ItemInHandLayer<>(this, p_174304_.getItemInHandRenderer()));
		this.addLayer(new ArrowLayer<>(p_174304_, this));
		this.addLayer(new CustomHeadLayer<>(this, p_174304_.getModelSet(), p_174304_.getItemInHandRenderer()));
		this.addLayer(new ElytraLayer<>(this, p_174304_.getModelSet()));
		this.addLayer(new SpinAttackEffectLayer<>(this, p_174304_.getModelSet()));
		this.addLayer(new BeeStingerLayer<>(this));
	}
	
	@Override
	public void render(EntityKaratFeng p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
	}
	
	@Override
	protected void scale(EntityKaratFeng p_115314_, PoseStack p_115315_, float p_115316_) 
	{
		p_115315_.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityKaratFeng p_115812_) 
	{
		return new ResourceLocation(Main.MODID, "textures/entity/karat_feng.png");
	}
}
