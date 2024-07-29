package com.min01.fireplace.item.renderer;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.event.ClientEventHandlerForge;
import com.min01.fireplace.item.model.ModelKingsStaff;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KingsStaffRenderer extends BlockEntityWithoutLevelRenderer
{
	public EntityModelSet set;
	public KingsStaffRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) 
	{
		super(p_172550_, p_172551_);
		this.set = p_172551_;
	}
	
	@Override
	public void renderByItem(ItemStack p_108830_, ItemDisplayContext p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) 
	{
		p_108832_.pushPose();
		p_108832_.scale(0.5F, 0.5F, 0.5F);
		p_108832_.translate(1, 2.5, 1);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.COMMAND_BLOCK), ItemDisplayContext.NONE, 240, OverlayTexture.NO_OVERLAY, p_108832_, p_108833_, ClientEventHandlerForge.MC.level, 0);
		p_108832_.popPose();
		
		p_108832_.pushPose();
		p_108832_.mulPose(Axis.YP.rotationDegrees(180F));
		p_108832_.mulPose(Axis.XP.rotationDegrees(180F));
		p_108832_.translate(0, -1.5, 0);
		ModelKingsStaff model = new ModelKingsStaff(this.set.bakeLayer(ModelKingsStaff.LAYER_LOCATION));
		VertexConsumer consumer = p_108833_.getBuffer(RenderType.entitySolid(new ResourceLocation(Fireplace.MODID, "textures/item/king_staff.png")));
		model.renderToBuffer(p_108832_, consumer, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_108832_.popPose();
	}
}
