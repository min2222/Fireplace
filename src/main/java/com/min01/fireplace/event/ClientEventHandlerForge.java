package com.min01.fireplace.event;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fireplace.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge 
{
	private static final ResourceLocation CHAIN_LOCATION = new ResourceLocation(Fireplace.MODID + ":textures/entity/fire_chain.png");
	private static final RenderType CHAIN_RENDER_TYPE = RenderType.eyes(CHAIN_LOCATION);
	
	@SubscribeEvent
	public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event)
	{
		if(event.getEntity() instanceof EntityKaratFeng karat)
		{
			for(LivingEntity living : karat.entityList)
			{
				if(living.isAlive() && living.isAddedToWorld() && living.tickCount >= 2)
				{
					drawChain(karat, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), 15728880, living);
				}
			}
		}

		LivingEntity living = event.getEntity();
		PoseStack stack = event.getPoseStack();
		
		if(FireplaceUtil.GRAVITY_MAP.containsKey(living))
		{
			if(living.isAlive())
			{
				if(FireplaceUtil.GRAVITY_MAP.get(living) < 180 && living.isCurrentlyGlowing())
				{
					FireplaceUtil.GRAVITY_MAP.replace(living, FireplaceUtil.GRAVITY_MAP.get(living) + 1);
				}
				else if(FireplaceUtil.GRAVITY_MAP.get(living) > 0 && !living.isCurrentlyGlowing())
				{
					FireplaceUtil.GRAVITY_MAP.replace(living, FireplaceUtil.GRAVITY_MAP.get(living) - 1);
					
					if(FireplaceUtil.GRAVITY_MAP.get(living) <= 0)
					{
						FireplaceUtil.GRAVITY_MAP.remove(living);
					}
				}
				
				if(FireplaceUtil.GRAVITY_MAP.get(living) != null)
				{
					stack.translate(0, FireplaceUtil.GRAVITY_MAP.get(living) / 180 + 0.5F, 0);
					stack.mulPose(Vector3f.XP.rotationDegrees(FireplaceUtil.GRAVITY_MAP.get(living)));
				}
			}
			else
			{
				FireplaceUtil.GRAVITY_MAP.remove(living);
			}
		}
		
		if(FireplaceUtil.NECRO_LIST.contains(living))
		{
			if(living.isAlive() && living.isAddedToWorld() && living.tickCount < 20)
			{
				double offset = -living.getEyeHeight() + (living.tickCount * 0.1);
				if(offset <= 0)
				{
					stack.translate(0, offset, 0);
				}
			}
		}
	}
	
    public static <E extends Entity> void drawChain(Entity entityLivingIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int lightIn, Entity chainTarget) 
    {
    	RenderSystem.enableDepthTest();
        float f3 = entityLivingIn.getEyeHeight();
        matrixStackIn.pushPose();	
        matrixStackIn.translate(0, f3, 0);
        Vec3 Vec3 = getPosition(chainTarget, (double) chainTarget.getBbHeight() * 0.5D, partialTicks);
        Vec3 Vec31 = getPosition(entityLivingIn, f3, partialTicks);
        Vec3 Vec32 = Vec3.subtract(Vec31);
        float f4 = (float) (Vec32.length());
        Vec32 = Vec32.normalize();
        float f5 = (float) Math.acos(Vec32.y);
        float f6 = (float) Math.atan2(Vec32.z, Vec32.x);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
        int j = 255;
        int k = 255;
        int l = 255;
        float f19 = 0;
        float f20 = 0.2F;
        float f21 = 0F;
        float f22 = -0.2F;
        
        float f29 = 0;
        float f30 = f4 + 0.2F;
        
        float f32 = 0.75F;
        float f31 = f4 + f32;

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(CHAIN_RENDER_TYPE);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        
        matrixStackIn.pushPose();
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19 - 0.01F, f4, f20 + 0.02F, j, k, l, 0.5F, f30, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19 - 0.01F, 0.0F, f20 + 0.02F, j, k, l, 0.5F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21 - 0.01F, 0.0F, f22 + 0.02F, j, k, l, 0.0F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21 - 0.01F, f4, f22 + 0.02F, j, k, l, 0.0F, f30, lightIn);

        drawVertex(ivertexbuilder, matrix4f, matrix3f, f20 + 0.02F, f4, f19 - 0.01F, j, k, l, 0.5F, f31, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f20 + 0.02F, 0.0F, f19 - 0.01F, j, k, l, 0.5F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f22 + 0.02F, 0.0F, f21 - 0.01F, j, k, l, 0.0F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f22 + 0.02F, f4, f21 - 0.01F, j, k, l, 0.0F, f31, lightIn);
        matrixStackIn.popPose();
        
        matrixStackIn.pushPose();
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19 - 0.01F, 0.0F, f20 + 0.02F, j, k, l, 0.5F, f30, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19 - 0.01F, f4, f20 + 0.02F, j, k, l, 0.5F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21 - 0.01F, f4, f22 + 0.02F, j, k, l, 0.0F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21 - 0.01F, 0.0F, f22 + 0.02F, j, k, l, 0.0F, f30, lightIn);

        drawVertex(ivertexbuilder, matrix4f, matrix3f, f20 + 0.02F, 0.0F, f19 - 0.01F, j, k, l, 0.5F, f31, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f20 + 0.02F, f4, f19 - 0.01F, j, k, l, 0.5F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f22 + 0.02F, f4, f21 - 0.01F, j, k, l, 0.0F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f22 + 0.02F, 0.0F, f21 - 0.01F, j, k, l, 0.0F, f31, lightIn);
        matrixStackIn.popPose();
        
        matrixStackIn.popPose();
    }

    private static void drawVertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_, int packedLight) 
    {
    	p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_)
    {
    	double d0 = Mth.lerp((double)p_114805_, p_114803_.xOld, p_114803_.getX());
    	double d1 = Mth.lerp((double)p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
        double d2 = Mth.lerp((double)p_114805_, p_114803_.zOld, p_114803_.getZ());
        return new Vec3(d0, d1, d2);
    }
}
