package com.min01.fireplace.init;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.EntityCarrotFang;
import com.min01.fireplace.entity.EntityEvokerFeng;
import com.min01.fireplace.entity.EntityFireFeng;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.EntitySantaFeng;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.entity.EntityVampireFeng;
import com.min01.fireplace.entity.projectile.EntityCarrotProjectile;
import com.min01.fireplace.entity.projectile.EntityPresentProjectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FireplaceEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Fireplace.MODID);
	
	public static final RegistryObject<EntityType<EntityKaratFeng>> KARAT_FENG = registerKarat(EntityKaratFeng::new, "karat_feng", MobCategory.MISC);
	public static final RegistryObject<EntityType<EntityCarrotFang>> CARROT_FANG = registerKarat(EntityCarrotFang::new, "carrot_fang", MobCategory.MONSTER);
	public static final RegistryObject<EntityType<EntitySnowyFeng>> SNOWY_FENG = registerKarat(EntitySnowyFeng::new, "snowy_feng", MobCategory.MONSTER);
	public static final RegistryObject<EntityType<EntityEvokerFeng>> EVOKER_FENG = registerKarat(EntityEvokerFeng::new, "evoker_feng", MobCategory.MONSTER);
	public static final RegistryObject<EntityType<EntitySantaFeng>> SANTA_FENG = registerKarat(EntitySantaFeng::new, "santa_feng", MobCategory.MONSTER);
	public static final RegistryObject<EntityType<EntityVampireFeng>> VAMPIRE_FENG = registerKarat(EntityVampireFeng::new, "vampire_feng", MobCategory.MONSTER);
	public static final RegistryObject<EntityType<EntityFireFeng>> FIRE_FENG = registerKaratWithFireImmune(EntityFireFeng::new, "fire_feng", MobCategory.MONSTER);
	
	public static final RegistryObject<EntityType<EntityCarrotProjectile>> CARROT = registerProjectile(EntityCarrotProjectile::new, "carrot");
	public static final RegistryObject<EntityType<EntityPresentProjectile>> PRESENT = registerProjectile(EntityPresentProjectile::new, "present");
	
	public static <T extends Projectile> RegistryObject<EntityType<T>> registerProjectile(EntityType.EntityFactory<T> entity, String name) 
	{
		return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(entity, MobCategory.MISC).sized(0.25F, 0.25F).build(new ResourceLocation(Fireplace.MODID, name).toString()));
	}
	
	public static <T extends AbstractKaratFeng> RegistryObject<EntityType<T>> registerKaratWithFireImmune(EntityType.EntityFactory<T> entity, String name, MobCategory category) 
	{
		return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(entity, category).fireImmune().build(new ResourceLocation(Fireplace.MODID, name).toString()));
	}
	
	public static <T extends AbstractKaratFeng> RegistryObject<EntityType<T>> registerKarat(EntityType.EntityFactory<T> entity, String name, MobCategory category) 
	{
		return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(entity, category).build(new ResourceLocation(Fireplace.MODID, name).toString()));
	}
}
