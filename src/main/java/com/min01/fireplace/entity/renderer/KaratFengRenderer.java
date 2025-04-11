package com.min01.fireplace.entity.renderer;

import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.model.ModelKaratFeng;
import com.min01.fireplace.util.FireplaceUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class KaratFengRenderer extends AbstractKaratFengRenderer<EntityKaratFeng, ModelKaratFeng<EntityKaratFeng>>
{
	public KaratFengRenderer(Context p_174304_)
	{
		super(p_174304_, FireplaceUtil.getKaratModel(p_174304_), 0.5F);
	}
	
	@Override
	public void render(EntityKaratFeng p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) 
	{
		this.setModelProperties(p_115455_);
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
	}
	
	private void setModelProperties(EntityKaratFeng p_117819_)
	{
		ModelKaratFeng<EntityKaratFeng> model = this.getModel();
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(p_117819_, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(p_117819_, InteractionHand.OFF_HAND);
        if(humanoidmodel$armpose.isTwoHanded())
        {
        	humanoidmodel$armpose1 = p_117819_.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }
        if(p_117819_.getMainArm() == HumanoidArm.RIGHT) 
        {
        	model.rightArmPose = humanoidmodel$armpose;
        	model.leftArmPose = humanoidmodel$armpose1;
        } 
        else 
        {
        	model.rightArmPose = humanoidmodel$armpose1;
        	model.leftArmPose = humanoidmodel$armpose;
        }
	}
	
	private static HumanoidModel.ArmPose getArmPose(EntityKaratFeng p_117795_, InteractionHand p_117796_)
	{
		ItemStack itemstack = p_117795_.getItemInHand(p_117796_);
		if(itemstack.isEmpty())
		{
			return HumanoidModel.ArmPose.EMPTY;
		} 
		else 
		{
			if(p_117795_.getUsedItemHand() == p_117796_ && p_117795_.getUseItemRemainingTicks() > 0) 
			{
				UseAnim useanim = itemstack.getUseAnimation();
				if(useanim == UseAnim.BLOCK)
				{
					return HumanoidModel.ArmPose.BLOCK;
				}
				if(useanim == UseAnim.BOW) 
				{
					return HumanoidModel.ArmPose.BOW_AND_ARROW;
				}
			}
			return HumanoidModel.ArmPose.ITEM;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityKaratFeng p_115812_) 
	{
		return FireplaceUtil.getKaratTexture("karat_feng");
	}
}
