package com.min01.fireplace.sound;

import com.min01.fireplace.Fireplace;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FireplaceSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Fireplace.MODID);
	
	/*private static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(BeyondtheAbyss.MODID, name)));
    }*/
}
