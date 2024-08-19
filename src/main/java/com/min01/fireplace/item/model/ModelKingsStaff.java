package com.min01.fireplace.item.model;

import com.min01.fireplace.Fireplace;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelKingsStaff extends Model 
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Fireplace.MODID, "kings_staff"), "main");
	private final ModelPart The_Kings_Staff;

	public ModelKingsStaff(ModelPart root) 
	{
		super(RenderType::entitySolid);
		this.The_Kings_Staff = root.getChild("The_Kings_Staff");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("The_Kings_Staff", CubeListBuilder.create().texOffs(0, 30).addBox(-9.0F, -15.0F, 7.0F, 2.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 57).mirror().addBox(-9.0F, -16.0F, 3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 55).addBox(-9.0F, -24.0F, 3.0F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 59).addBox(-9.0F, -25.0F, 3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(42, 59).addBox(-9.0F, -27.0F, 7.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		The_Kings_Staff.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}