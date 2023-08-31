package com.min01.fireplace.network;

import java.util.function.Supplier;

import com.min01.fireplace.entity.EntityKaratFeng;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class KaratDataSyncPacket 
{
    private DataType dataType;
    private int karatId;

    public enum DataType 
    {
        ENTITY_LIST
    }

    public KaratDataSyncPacket(DataType type, int id) 
    {
        this.dataType = type;
        this.karatId = id;
    }

    public KaratDataSyncPacket(FriendlyByteBuf buf)
    {
        this.dataType = DataType.values()[buf.readInt()];
        this.karatId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) 
    {
        buf.writeInt(this.dataType.ordinal());
        buf.writeInt(this.karatId);
    }
    
    public static class Handler 
    {
        public static boolean onMessage(KaratDataSyncPacket message, Supplier<NetworkEvent.Context> ctx) 
        {
            ctx.get().enqueueWork(() ->
            {
            	Minecraft minecraft = Minecraft.getInstance();
                switch (message.dataType)
                {
				case ENTITY_LIST:
					EntityKaratFeng karat = (EntityKaratFeng) minecraft.level.getEntity(message.karatId);
					karat.entityList = karat.entityList;
					break;
				default:
					break;
                }
            });
            ctx.get().setPacketHandled(true);
			return true;
        }
    }
}
