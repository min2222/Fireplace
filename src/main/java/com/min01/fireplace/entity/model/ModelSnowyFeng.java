package com.min01.fireplace.entity.model;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModelSnowyFeng extends EntityModel<EntitySnowyFeng>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fireplace.MODID, "snowy_feng"), "main");
	private final ModelPart root;
	private final ModelPart upperBody;
	private final ModelPart head;
	private final ModelPart leftArm;
	private final ModelPart rightArm;

	public ModelSnowyFeng(ModelPart root) 
	{
		this.root = root;
		this.head = root.getChild("head");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
		this.upperBody = root.getChild("upper_body");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		CubeDeformation cubedeformation = new CubeDeformation(-0.5F);
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubedeformation), PartPose.offset(0.0F, 4.0F, 0.0F));
		CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 12.0F, 2.0F, 2.0F, cubedeformation);
		partdefinition.addOrReplaceChild("left_arm", cubelistbuilder, PartPose.offsetAndRotation(5.0F, 6.0F, 1.0F, 0.0F, 0.0F, 1.0F));
		partdefinition.addOrReplaceChild("right_arm", cubelistbuilder, PartPose.offsetAndRotation(-5.0F, 6.0F, -1.0F, 0.0F, (float)Math.PI, -1.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, cubedeformation), PartPose.offset(0.0F, 13.0F, 0.0F));
		body.addOrReplaceChild("karat_head", CubeListBuilder.create().texOffs(32, 9).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)).texOffs(38, 30).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, -5.0F, -2.0F));
		partdefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, cubedeformation), PartPose.offset(0.0F, 24.0F, 0.0F));
	
		return LayerDefinition.create(meshdefinition, 70, 64);
	}

	@Override
	public void setupAnim(EntitySnowyFeng p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_) 
	{
		this.head.yRot = p_103399_ * ((float)Math.PI / 180F);
		this.head.xRot = p_103400_ * ((float)Math.PI / 180F);
		this.upperBody.yRot = p_103399_ * ((float)Math.PI / 180F) * 0.25F;
		float f = Mth.sin(this.upperBody.yRot);
		float f1 = Mth.cos(this.upperBody.yRot);
		this.leftArm.yRot = this.upperBody.yRot;
		this.rightArm.yRot = this.upperBody.yRot + (float)Math.PI;
		this.leftArm.x = f1 * 5.0F;
		this.leftArm.z = -f * 5.0F;
		this.rightArm.x = -f1 * 5.0F;
		this.rightArm.z = f * 5.0F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}