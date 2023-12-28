package com.min01.fireplace.raid;

import com.min01.fireplace.misc.IKaratRaid;

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
		if (p_19467_ instanceof ServerPlayer serverplayer && !p_19467_.isSpectator()) 
		{
			if(!serverplayer.level.isClientSide)
			{
				ServerLevel serverlevel = (ServerLevel) serverplayer.level;
				if(serverlevel != null)
				{
					if (serverlevel.getDifficulty() == Difficulty.PEACEFUL)
					{
						return;
					}
					
		            if (serverlevel.isVillage(p_19467_.blockPosition()))
		            {
		            	((IKaratRaid)serverlevel).getKaratRaids().createOrExtendRaid(serverplayer);
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
