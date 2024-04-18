package com.min01.fireplace.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.fireplace.event.ClientEventHandlerForge;
import com.min01.fireplace.misc.FireplaceBossBarType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class UpdateBossBarPacket 
{
    private final UUID bossBar;
    private final FireplaceBossBarType barType;
	
	public UpdateBossBarPacket(UUID bossBar, FireplaceBossBarType barType) 
	{
		this.bossBar = bossBar;
		this.barType = barType;
	}

	public UpdateBossBarPacket(FriendlyByteBuf buf)
	{
		this.bossBar = buf.readUUID();
		this.barType = FireplaceBossBarType.values()[buf.readInt()];
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.bossBar);
		buf.writeInt(this.barType.ordinal());
	}
	
	public static class Handler 
	{
		public static boolean onMessage(UpdateBossBarPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
		        if(message.barType == FireplaceBossBarType.NONE)
		        {
		            ClientEventHandlerForge.BOSS_BAR_MAP.remove(message.bossBar);
		        }
		        else
		        {
		        	ClientEventHandlerForge.BOSS_BAR_MAP.put(message.bossBar, message.barType);
		        }
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
