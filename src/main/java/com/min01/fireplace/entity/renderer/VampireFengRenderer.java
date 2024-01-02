package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.entity.EntityVampireFeng;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;

public class VampireFengRenderer extends AbstractKaratFengRenderer<EntityVampireFeng, PlayerModel<EntityVampireFeng>>
{
	public VampireFengRenderer(Context p_174304_)
	{
		super(p_174304_, FireplaceUtil.getKaratModel(p_174304_), 0.5F);
	}
	
	@Override
	public void render(EntityVampireFeng p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		if(p_115455_.isBat())
		{
			p_115458_.pushPose();
			Bat bat = EntityType.BAT.create(p_115455_.level);
			bat.setNoAi(true);
			bat.setInvulnerable(true);
			bat.setResting(false);
			EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(bat);
			LivingEntityRenderer<? super LivingEntity, ?> livingrenderer = (LivingEntityRenderer<? super LivingEntity, ?>) renderer;
			FireplaceUtil.tick(p_115455_, bat);
			p_115458_.translate(0, p_115455_.getEyeHeight() - 1F, 0);
			livingrenderer.render((LivingEntity) bat, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
			p_115458_.popPose();
		}
		else
		{
			super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityVampireFeng p_115812_)
	{
		return FireplaceUtil.getKaratTexture("vampire_feng");
	}
}
