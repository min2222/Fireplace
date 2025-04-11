package com.min01.fireplace.item;

import java.util.function.Supplier;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.FireplaceEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FireplaceItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fireplace.MODID);
	public static final RegistryObject<Item> KING_STAFF = ITEMS.register("king_staff", () -> new KingStaffItem());
	public static final RegistryObject<Item> KARAT_FENG_SPAWN_EGG = registerKaratSpawnEgg("karat_feng_spawn_egg", () -> FireplaceEntities.KARAT_FENG.get());
	public static final RegistryObject<Item> CARROT_FANG_SPAWN_EGG = registerKaratSpawnEgg("carrot_fang_spawn_egg", () -> FireplaceEntities.CARROT_FANG.get());
	public static final RegistryObject<Item> SNOWY_FENG_SPAWN_EGG = registerKaratSpawnEgg("snowy_feng_spawn_egg", () -> FireplaceEntities.SNOWY_FENG.get());
	public static final RegistryObject<Item> EVOKER_FENG_SPAWN_EGG = registerKaratSpawnEgg("evoker_feng_spawn_egg", () -> FireplaceEntities.EVOKER_FENG.get());
	public static final RegistryObject<Item> SANTA_FENG_SPAWN_EGG = registerKaratSpawnEgg("santa_feng_spawn_egg", () -> FireplaceEntities.SANTA_FENG.get());
	public static final RegistryObject<Item> VAMPIRE_FENG_SPAWN_EGG = registerKaratSpawnEgg("vampire_feng_spawn_egg", () -> FireplaceEntities.VAMPIRE_FENG.get());
	public static final RegistryObject<Item> FIRE_FENG_SPAWN_EGG = registerKaratSpawnEgg("fire_feng_spawn_egg", () -> FireplaceEntities.FIRE_FENG.get());
	public static final RegistryObject<Item> NECRO_FENG_SPAWN_EGG = registerKaratSpawnEgg("necro_feng_spawn_egg", () -> FireplaceEntities.NECRO_FENG.get());
	public static final RegistryObject<Item> ALIEN_FENG_SPAWN_EGG = registerKaratSpawnEgg("alien_feng_spawn_egg", () -> FireplaceEntities.ALIEN_FENG.get());
	
	public static <T extends AbstractKaratFeng> RegistryObject<Item> registerKaratSpawnEgg(String name, Supplier<EntityType<T>> entity) 
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(entity, 15170860, 3941901, new Item.Properties()));
	}
}
