package com.min01.fireplace.entity.model;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityUFO;
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

public class ModelUFO extends EntityModel<EntityUFO>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fireplace.MODID, "ufo"), "main");
	private final ModelPart root;

	public ModelUFO(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_part = bone.addOrReplaceChild("lower_part", CubeListBuilder.create().texOffs(0, 0).addBox(-35.0F, -8.0F, -35.0F, 70.0F, 20.0F, 70.0F, new CubeDeformation(0.0F))
		.texOffs(0, 90).addBox(-26.0F, 12.0F, -26.0F, 52.0F, 6.0F, 52.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

		PartDefinition light = lower_part.addOrReplaceChild("light", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		light.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, -39.0F, 18.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-6.0F, -39.0F, -30.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -39.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		light.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-6.0F, -39.0F, 18.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-6.0F, -39.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 16).mirror().addBox(-6.0F, -39.0F, -30.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		light.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-18.0F, -10.0F, -30.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(-18.0F, -10.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 16).mirror().addBox(-18.0F, -10.0F, 18.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-29.0F, -10.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		light.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -12.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(6.0F, -12.0F, -30.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(6.0F, -12.0F, 18.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(27.0F, -10.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cockpit = lower_part.addOrReplaceChild("cockpit", CubeListBuilder.create().texOffs(372, 0).addBox(-18.0F, -8.5F, -18.0F, 35.0F, 0.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(442, 35).addBox(-18.0F, -16.5F, -18.0F, 35.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(442, 35).addBox(-18.0F, -16.5F, 17.0F, 35.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.5F, 0.0F));

		cockpit.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(442, 35).addBox(-17.5F, -4.0F, -17.5F, 35.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(442, 35).addBox(-17.5F, -4.0F, 17.5F, 35.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -12.5F, -0.5F, 0.0F, -1.5708F, 0.0F));

		bone.addOrReplaceChild("upper_part", CubeListBuilder.create().texOffs(0, 148).addBox(-17.5F, -18.5F, -17.5F, 35.0F, 37.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -18.5F, -0.5F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(EntityUFO entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root.xRot = limbSwingAmount * 0.25F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}