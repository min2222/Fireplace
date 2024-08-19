package com.min01.fireplace.item.renderer;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.item.model.ModelKingsStaff;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KingsStaffRenderer extends BlockEntityWithoutLevelRenderer
{
	public EntityModelSet modelSet;
	public KingsStaffRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet modelSet) 
	{
		super(p_172550_, modelSet);
		this.modelSet = modelSet;
	}
	
	@Override
	public void renderByItem(ItemStack p_108830_, TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) 
	{
		p_108832_.pushPose();
		p_108832_.scale(0.5F, 0.5F, 0.5F);
		p_108832_.translate(1, 2.5, 1);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.COMMAND_BLOCK), ItemTransforms.TransformType.NONE, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, p_108832_, p_108833_, 0);
		p_108832_.popPose();
		
		p_108832_.pushPose();
		p_108832_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		p_108832_.mulPose(Vector3f.XP.rotationDegrees(180.0F));
		p_108832_.translate(0, -1.5F, 0);
		ModelKingsStaff model = new ModelKingsStaff(this.modelSet.bakeLayer(ModelKingsStaff.LAYER_LOCATION));
		VertexConsumer consumer = p_108833_.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(Fireplace.MODID, "textures/items/king_staff.png")));
		model.renderToBuffer(p_108832_, consumer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_108832_.popPose();
	}
}
