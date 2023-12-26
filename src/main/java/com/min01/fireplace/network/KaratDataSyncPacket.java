package com.min01.fireplace.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.EntityNecroFeng;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class KaratDataSyncPacket 
{
    private DataType dataType;
    private int karatId;

    public enum DataType 
    {
        ENTITY_LIST, NECRO_FENG_SUMMONING
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
					ArrayList<LivingEntity> list = new ArrayList<>();
					for(ServerLevel level : ServerLifecycleHooks.getCurrentServer().getAllLevels())
					{
						Entity entity = level.getEntity(message.karatId);
						if(entity instanceof EntityKaratFeng karat)
						{
							list.addAll(karat.entityList);
						}
					}
					EntityKaratFeng karat = (EntityKaratFeng) minecraft.level.getEntity(message.karatId);
					karat.entityList.addAll(list);
					break;
				case NECRO_FENG_SUMMONING:
					ArrayList<LivingEntity> necroList = new ArrayList<>();
					for(ServerLevel level : ServerLifecycleHooks.getCurrentServer().getAllLevels())
					{
						Entity entity = level.getEntity(message.karatId);
						if(entity instanceof EntityNecroFeng necro)
						{
							necroList.addAll(necro.entityList);
						}
					}
					EntityNecroFeng necro = (EntityNecroFeng) minecraft.level.getEntity(message.karatId);
					necro.entityList.addAll(necroList);
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
