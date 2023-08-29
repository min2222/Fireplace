package com.min01.fireplace.entity.render;

import com.min01.fireplace.entity.EntityKaratFeng;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class LayerOrangeGlass<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M>
{
	private static final RenderType TEXTURE = RenderType.entityTranslucent(new ResourceLocation("textures/block/orange_stained_glass.png"));
	
	public LayerOrangeGlass(RenderLayerParent<T, M> parent)
	{
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		if(!(entity instanceof EntityKaratFeng) && entity.getPersistentData().contains("karatUUID"))
		{
			this.getParentModel().renderToBuffer(stack, new SheetedDecalTextureGenerator(buffer.getBuffer(TEXTURE), stack.last().pose(), stack.last().normal()), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
