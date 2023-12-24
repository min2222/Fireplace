package com.min01.fireplace.init;

import java.util.function.Supplier;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.item.KingStaffItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
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
	
	public static <T extends AbstractKaratFeng> RegistryObject<Item> registerKaratSpawnEgg(String name, Supplier<EntityType<T>> entity) 
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(entity, 15170860, 3941901, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}
}
