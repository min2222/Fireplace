package com.min01.fireplace.misc;

import com.min01.fireplace.network.FireplaceNetwork;
import com.min01.fireplace.network.UpdateBossBarPacket;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public class FireplaceBossEvent extends ServerBossEvent
{
	private final FireplaceBossBarType barType;
	public FireplaceBossEvent(Component p_8300_, FireplaceBossBarType barType)
	{
		super(p_8300_, BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
		this.barType = barType;
	}
	
	public FireplaceBossBarType getBarType()
	{
		return this.barType;
	}
	
	@Override
    public void addPlayer(ServerPlayer serverPlayer) 
    {
    	FireplaceNetwork.sendNonLocal(new UpdateBossBarPacket(this.getId(), this.barType), serverPlayer);
        super.addPlayer(serverPlayer);
    }

    @Override
    public void removePlayer(ServerPlayer serverPlayer) 
    {
    	FireplaceNetwork.sendNonLocal(new UpdateBossBarPacket(this.getId(), FireplaceBossBarType.NONE), serverPlayer);
        super.removePlayer(serverPlayer);
    }
}
