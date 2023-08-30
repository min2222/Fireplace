package com.min01.fireplace.init;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FireplaceEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Fireplace.MODID);
	public static final RegistryObject<EntityType<EntityKaratFeng>> KARAT_FENG = ENTITY_TYPES.register("karat_feng",
            () -> EntityType.Builder.of(EntityKaratFeng::new, MobCategory.MISC).build(new ResourceLocation(Fireplace.MODID, "karat_feng").toString()));
}
