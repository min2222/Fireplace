package com.min01.fireplace.entity.model;

import java.util.Optional;

import com.min01.fireplace.entity.AbstractKaratFeng;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;

public abstract class HierarchicalPlayerModel<T extends AbstractKaratFeng> extends PlayerModel<T>
{
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
	public HierarchicalPlayerModel()
	{
		super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
	}

	public abstract ModelPart root();
	
	@Override
	public void setupAnim(T p_103395_, float p_103396_, float p_103397_, float p_103398_, float p_103399_, float p_103400_) 
	{
		super.setupAnim(p_103395_, p_103396_, p_103397_, p_103398_, p_103399_, p_103400_);
		this.root().getAllParts().forEach(ModelPart::resetPose);
	}
	
	public Optional<ModelPart> getAnyDescendantWithName(String p_233394_) 
	{
		return this.root().getAllParts().filter((p_233400_) -> 
		{
			return p_233400_.hasChild(p_233394_);
		}).findFirst().map((p_233397_) ->
		{
			return p_233397_.getChild(p_233394_);
		});
	}

	protected void animate(AnimationState p_233382_, AnimationDefinition p_233383_, float p_233384_)
	{
		this.animate(p_233382_, p_233383_, p_233384_, 1.0F);
	}

	protected void animate(AnimationState p_233386_, AnimationDefinition p_233387_, float p_233388_, float p_233389_) 
	{
		p_233386_.updateTime(p_233388_, p_233389_);
		p_233386_.ifStarted((p_233392_) ->
		{
			KeyframePlayerModelAnimations.animate(this, p_233387_, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
}
