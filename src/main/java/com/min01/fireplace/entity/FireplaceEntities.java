package com.min01.fireplace.entity;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.projectile.EntityCarrotProjectile;
import com.min01.fireplace.entity.projectile.EntityPresentProjectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FireplaceEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Fireplace.MODID);
	
	public static final RegistryObject<EntityType<EntityKaratFeng>> KARAT_FENG = registerEntity("karat_feng", createBuilder(EntityKaratFeng::new, MobCategory.MISC));
	public static final RegistryObject<EntityType<EntityCarrotFang>> CARROT_FANG = registerEntity("carrot_fang", createBuilder(EntityCarrotFang::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntitySnowyFeng>> SNOWY_FENG = registerEntity("snowy_feng", createBuilder(EntitySnowyFeng::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntityEvokerFeng>> EVOKER_FENG = registerEntity("evoker_feng", createBuilder(EntityEvokerFeng::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntitySantaFeng>> SANTA_FENG = registerEntity("santa_feng", createBuilder(EntitySantaFeng::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntityVampireFeng>> VAMPIRE_FENG = registerEntity("vampire_feng", createBuilder(EntityVampireFeng::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntityFireFeng>> FIRE_FENG = registerEntity("fire_feng", createBuilder(EntityFireFeng::new, MobCategory.MONSTER).fireImmune());
	public static final RegistryObject<EntityType<EntityNecroFeng>> NECRO_FENG = registerEntity("necro_feng", createBuilder(EntityNecroFeng::new, MobCategory.MONSTER));
	public static final RegistryObject<EntityType<EntityAlienFeng>> ALIEN_FENG = registerEntity("alien_feng", createBuilder(EntityAlienFeng::new, MobCategory.MONSTER).sized(0.6F, 2.2F));
	public static final RegistryObject<EntityType<EntityUFO>> UFO = registerEntity("ufo", EntityType.Builder.<EntityUFO>of(EntityUFO::new, MobCategory.MONSTER).sized(4.0F, 2.0F));
	
	public static final RegistryObject<EntityType<EntityCarrotProjectile>> CARROT = registerEntity("carrot", EntityType.Builder.<EntityCarrotProjectile>of(EntityCarrotProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));
	public static final RegistryObject<EntityType<EntityPresentProjectile>> PRESENT = registerEntity("present", EntityType.Builder.<EntityPresentProjectile>of(EntityPresentProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(new ResourceLocation(Fireplace.MODID, name).toString()));
	}
}
