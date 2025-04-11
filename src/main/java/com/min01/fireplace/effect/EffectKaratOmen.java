package com.min01.fireplace.effect;

import com.min01.fireplace.raid.IKaratRaid;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectKaratOmen extends MobEffect
{
	public EffectKaratOmen()
	{
		super(MobEffectCategory.HARMFUL, 16744448);
	}
	
	@Override
	public void applyEffectTick(LivingEntity p_19467_, int p_19468_)
	{
		if(p_19467_ instanceof ServerPlayer player && !player.isSpectator()) 
		{
			if(!player.level.isClientSide)
			{
				ServerLevel serverLevel = (ServerLevel) player.level;
				if(serverLevel != null)
				{
					if(serverLevel.getDifficulty() == Difficulty.PEACEFUL)
					{
						return;
					}
					
		            if(serverLevel.isVillage(p_19467_.blockPosition()))
		            {
		            	((IKaratRaid)serverLevel).getRaids().createOrExtendRaid(player);
		            }
				}
			}
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_)
	{
		return true;
	}
}
