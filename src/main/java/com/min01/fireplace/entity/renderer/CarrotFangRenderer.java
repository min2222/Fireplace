package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.entity.EntityCarrotFang;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CarrotFangRenderer extends AbstractKaratFengRenderer<EntityCarrotFang, PlayerModel<EntityCarrotFang>>
{
	public CarrotFangRenderer(Context p_174304_)
	{
		super(p_174304_, FireplaceUtil.getKaratModel(p_174304_), 0.5F);
	}
	
	@Override
	public void render(EntityCarrotFang p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		p_115458_.pushPose();
		if(p_115455_.isAlive())
		{
			float headRot = Mth.rotLerp(p_115457_, p_115455_.yHeadRotO, p_115455_.yHeadRot);
			p_115458_.scale(1.05F, 1.05F, 1.05F);
			p_115458_.translate(0, p_115455_.getEyeHeight() + 0.1F, 0);
			p_115458_.mulPose(Vector3f.YP.rotationDegrees(180.0F - headRot));
			p_115458_.mulPose(Vector3f.ZP.rotationDegrees(-45));
			Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.CARROT), TransformType.NONE, 15728880, OverlayTexture.NO_OVERLAY, p_115458_, p_115459_, p_115455_.getId());
		}
		p_115458_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityCarrotFang p_115812_) 
	{
		return FireplaceUtil.getKaratTexture("karat_feng");
	}
}
