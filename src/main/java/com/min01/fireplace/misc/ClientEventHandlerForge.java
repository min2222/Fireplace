package com.min01.fireplace.misc;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityKaratFeng;
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
	private static final RenderType CHAIN_RENDER_TYPE = FireplaceRenderType.getGlowingEffect(CHAIN_LOCATION);
	
	@SubscribeEvent
	public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event)
	{
		if(event.getEntity() instanceof EntityKaratFeng karat)
		{
			for(LivingEntity living : karat.entityList)
			{
				if(living.isAlive() && living.isAddedToWorld() && living.tickCount >= 2)
				{
					renderLink(karat, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), 15728880, living);
				}
			}
		}
	}
	
    public static <E extends Entity> void renderLink(Entity entityLivingIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int lightIn, Entity chainTarget) 
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
        float f7 = -1.0F;
        int j = 255;
        int k = 255;
        int l = 255;
        float f19 = 0;
        float f20 = 0.2F;
        float f21 = 0F;
        float f22 = -0.2F;
        float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f29 = 0;
        float f30 = f4 + f29;
        float f32 = 0.75F;
        float f31 = f4 + f32;

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(CHAIN_RENDER_TYPE);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        matrixStackIn.pushPose();
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30, lightIn);

        drawVertex(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f31, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f32, lightIn);
        drawVertex(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f31, lightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    private static void drawVertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_, int packedLight) 
    {
    	p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static Vec3 getPosition(Entity LivingEntityIn, double p_177110_2_, float p_177110_4_)
    {
    	double d0 = LivingEntityIn.xOld + (LivingEntityIn.getX() - LivingEntityIn.xOld) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.yOld + (LivingEntityIn.getY() - LivingEntityIn.yOld) * (double) p_177110_4_;
        double d2 = LivingEntityIn.zOld + (LivingEntityIn.getZ() - LivingEntityIn.zOld) * (double) p_177110_4_;
        return new Vec3(d0, d1, d2);
    }
}
