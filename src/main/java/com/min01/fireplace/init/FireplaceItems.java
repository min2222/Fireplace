package com.min01.fireplace.init;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.item.KingStaffItem;

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
	public static final RegistryObject<Item> KARAT_FENG_SPAWN_EGG = ITEMS.register("karat_feng_spawn_egg", () -> new ForgeSpawnEggItem(() -> FireplaceEntities.KARAT_FENG.get(), 15170860, 3941901, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
