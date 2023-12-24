package com.min01.fireplace.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FireplaceUtil 
{
	public static final String KARAT_UUID = "karatUUID";
	
	public static void shootFromRotation(Entity entity, Entity owner, float p_37253_, float p_37254_, float p_37255_, float p_37256_, float p_37257_)
	{
		shootFromRotation(entity, owner, p_37253_, p_37254_, p_37255_, p_37256_, p_37257_, true);
	}
	
	public static void shootFromRotation(Entity entity, Entity owner, float p_37253_, float p_37254_, float p_37255_, float p_37256_, float p_37257_, boolean random)
	{
		float f = -Mth.sin(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((p_37253_ + p_37255_) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
		if(random)
		{
			shoot(entity, (double)f, (double)f1, (double)f2, p_37256_, p_37257_);
		}
		else
		{
			shootWithoutRandom(entity, (double)f, (double)f1, (double)f2, p_37256_, p_37257_);
		}
		Vec3 vec3 = owner.getDeltaMovement();
		entity.setDeltaMovement(entity.getDeltaMovement().add(vec3.x, owner.isOnGround() ? 0.0D : vec3.y, vec3.z));
	}
	
	public static void shootWithoutRandom(Entity entity, double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) 
	{
		Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize();
		entity.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		entity.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
		entity.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}
	
	public static void shoot(Entity entity, double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) 
	{
		Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_)).scale((double)p_37269_);
		entity.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		entity.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
		entity.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}
}
