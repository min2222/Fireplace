package com.min01.fireplace.entity.model;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityAlienFeng;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModelAlienFeng extends PlayerModel<EntityAlienFeng> 
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fireplace.MODID, "alien_feng"), "main");
	private final ModelPart root;

	public ModelAlienFeng(ModelPart root) 
	{
		super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(96, 0).addBox(-4.0F, -17.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -22.0F, 0.0F));

		left_arm.addOrReplaceChild("left_sleve", CubeListBuilder.create().texOffs(48, 48).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -22.0F, 0.0F));

		right_arm.addOrReplaceChild("right_sleve", CubeListBuilder.create().texOffs(40, 32).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		left_leg.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

		right_leg.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(EntityAlienFeng entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root.getChild("head").yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.root.getChild("head").xRot = headPitch * ((float)Math.PI / 180F);
		this.root.getChild("left_arm").xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.root.getChild("right_arm").xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.root.getChild("right_leg").xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.root.getChild("left_leg").xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		if(this.riding)
		{
			this.root.getChild("right_arm").xRot += (-(float)Math.PI / 5F);
			this.root.getChild("left_arm").xRot += (-(float)Math.PI / 5F);
			this.root.getChild("right_leg").xRot = -1.4137167F;
			this.root.getChild("right_leg").yRot = ((float)Math.PI / 10F);
			this.root.getChild("right_leg").zRot = 0.07853982F;
			this.root.getChild("left_leg").xRot = -1.4137167F;
			this.root.getChild("left_leg").yRot = (-(float)Math.PI / 10F);
			this.root.getChild("left_leg").zRot = -0.07853982F;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}