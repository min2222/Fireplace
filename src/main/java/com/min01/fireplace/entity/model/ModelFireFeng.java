package com.min01.fireplace.entity.model;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityFireFeng;
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

public class ModelFireFeng extends PlayerModel<EntityFireFeng> 
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fireplace.MODID, "fire_feng"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public ModelFireFeng(ModelPart root) 
	{
		super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
		this.root = root.getChild("root");
		this.head = this.root.getChild("bone").getChild("body_part").getChild("head_part");
		this.left_arm = this.root.getChild("bone").getChild("arm_part").getChild("left_arm");
		this.right_arm = this.root.getChild("bone").getChild("arm_part").getChild("right_arm");
		this.left_leg = this.root.getChild("bone").getChild("left_leg");
		this.right_leg = this.root.getChild("bone").getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_part = bone.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition head_part = body_part.addOrReplaceChild("head_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		head_part.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		head_part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		body_part.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		body_part.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition arm_part = bone.addOrReplaceChild("arm_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition right_arm = arm_part.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		right_arm.addOrReplaceChild("right_arm_fire", CubeListBuilder.create().texOffs(48, 54).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 13.0F, 0.0F));

		PartDefinition left_arm = arm_part.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		left_arm.addOrReplaceChild("left_arm_fire", CubeListBuilder.create().texOffs(0, 54).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 13.0F, 0.0F));

		PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

		right_leg.addOrReplaceChild("right_leg_fire", CubeListBuilder.create().texOffs(48, 54).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		left_leg.addOrReplaceChild("left_leg_fire", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EntityFireFeng entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.right_arm.xRot = limbSwingAmount;
		this.right_leg.xRot = limbSwingAmount;
        this.left_leg.xRot = limbSwingAmount;
        this.right_arm.zRot = limbSwingAmount;
		this.right_leg.zRot = limbSwingAmount * 0.25F;
        this.left_leg.zRot = -limbSwingAmount * 0.25F;
		if(entity.isShooting())
		{
			float rot = -90F * ((float)Math.PI / 180F);
	        this.left_arm.xRot = rot + Mth.sin(ageInTicks * 0.8F + 10) * 0.2F;
			this.left_arm.zRot = 0;
			this.left_arm.y = Mth.sin(ageInTicks * 0.8F + 10F);
			this.left_arm.z = Mth.sin(ageInTicks * 0.8F + 10F);
		}
		else
		{
			this.left_arm.y = 2F;
			this.left_arm.z = 0;
			this.left_arm.xRot = limbSwingAmount;
			this.left_arm.zRot = -limbSwingAmount;
		}
		this.root.getChild("bone").y = Mth.sin(ageInTicks * 0.09F + 10F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}