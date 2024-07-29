package com.min01.fireplace.misc;

import com.min01.fireplace.Fireplace;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class FireplaceTags
{
	public static final TagKey<EntityType<?>> THE_FENGS = createEntityTypeTagKey("the_fengs");
	
	private static TagKey<EntityType<?>> createEntityTypeTagKey(String p_203849_) 
	{
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Fireplace.MODID, p_203849_));
	}
}
