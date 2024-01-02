package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.model.ModelPresent;
import com.min01.fireplace.entity.projectile.EntityPresentProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PresentRenderer extends EntityRenderer<EntityPresentProjectile>
{
	private final ModelPresent model;
	
	public PresentRenderer(Context p_174008_) 
	{
		super(p_174008_);
		this.model = new ModelPresent(p_174008_.bakeLayer(ModelPresent.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityPresentProjectile p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		p_114488_.pushPose();
		p_114488_.scale(-1, -1, 1);
		p_114488_.translate(0, -1.5F, 0);
		float yRot = Mth.rotLerp(p_114487_, p_114485_.yRotO, p_114485_.getYRot());
		p_114488_.mulPose(Vector3f.YP.rotationDegrees(180.0F - yRot));
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(RenderType.entityCutout(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		p_114488_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityPresentProjectile p_114482_) 
	{
		switch(p_114482_.getPresentType())
		{
		case BLUE:
			return new ResourceLocation(Fireplace.MODID, "textures/entity/blue_present.png");
		case GREEN:
			return new ResourceLocation(Fireplace.MODID, "textures/entity/green_present.png");
		case RED:
			return new ResourceLocation(Fireplace.MODID, "textures/entity/red_present.png");
		}
		return new ResourceLocation(Fireplace.MODID, "textures/entity/green_present.png");
	}
}
