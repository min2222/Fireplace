package com.min01.fireplace.init;

import com.min01.fireplace.Main;
import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityType
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MODID);
	public static final RegistryObject<EntityType<EntityKaratFeng>> KARAT_FENG = ENTITY_TYPES.register("karat_feng",
            () -> EntityType.Builder.of(EntityKaratFeng::new, MobCategory.MISC).build(new ResourceLocation(Main.MODID, "karat_feng").toString()));
}
