package com.min01.fireplace.raid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractKaratFeng;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class KaratRaidMembers
{
	public static final Map<EntityType<? extends AbstractKaratFeng>, Integer[]> CUSTOM_RAID_MEMBERS = new HashMap<EntityType<? extends AbstractKaratFeng>, Integer[]>();
	
	@SuppressWarnings("unchecked")
	public static void registerWaveMembers()
	{
		for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
		{
			ResourceLocation resourcelocation = EntityType.getKey(type);
			List<? extends String> list = FireplaceConfig.karatRaidMembers.get();
			for(int length = 0; length < list.size(); length++)
			{
				String string = list.get(length);
				if(string.split("=").length > 1)
				{
					String mobid = string.split("=")[0];
					String count = string.split("=")[1];
					if(mobid.contains(resourcelocation.toString()))
					{
						String[] counts = count.split(",");
						translateToWaves((EntityType<? extends AbstractKaratFeng>) type, counts);
					}
				}
			}
		}
	}
	
    private static void translateToWaves(EntityType<? extends AbstractKaratFeng> type, String[] list)
    {
    	List<Integer> intList = new ArrayList<>();
    	//since list start from zero, if you set max wave to 7, total wave will be 8, so we need to - 1 from value
    	for(int i = 0; i < FireplaceConfig.maxWave.get() - 1; i++)
    	{
    		intList.add(Integer.parseInt(list[i]));
    	}
    	
        CUSTOM_RAID_MEMBERS.put(type, intList.toArray(new Integer[FireplaceConfig.maxWave.get() - 1]));
    }
}
