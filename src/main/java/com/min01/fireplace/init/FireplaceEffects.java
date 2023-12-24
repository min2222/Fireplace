package com.min01.fireplace.init;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.raid.EffectKaratOmen;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FireplaceEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Fireplace.MODID);
	
	public static final RegistryObject<MobEffect> KARAT_OMEN = EFFECTS.register("karat_omen", () -> new EffectKaratOmen());
}
