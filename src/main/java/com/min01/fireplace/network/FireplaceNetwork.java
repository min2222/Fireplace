package com.min01.fireplace.network;

import com.min01.fireplace.Fireplace;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class FireplaceNetwork 
{
	public static int ID;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = 
    		NetworkRegistry.newSimpleChannel(new ResourceLocation(Fireplace.MODID, "fireplace_channel"), 
    				() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void registerMessages()
	{
		CHANNEL.registerMessage(ID++, KaratDataSyncPacket.class, KaratDataSyncPacket::encode, KaratDataSyncPacket::new, KaratDataSyncPacket.Handler::onMessage);
		CHANNEL.registerMessage(ID++, UpdateBossBarPacket.class, UpdateBossBarPacket::encode, UpdateBossBarPacket::new, UpdateBossBarPacket.Handler::onMessage);
	}
	
    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) 
    {
        CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
	
    public static <MSG> void sendToAll(MSG message) 
    {
    	for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
    	{
    		CHANNEL.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    	}
    }
}
