package com.min01.fireplace.item;

import java.util.function.Consumer;

import com.min01.fireplace.item.render.KingsStaffRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class KingStaffItem extends Item
{
	public KingStaffItem() 
	{
		super(new Item.Properties());
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
    	consumer.accept(new IClientItemExtensions() 
    	{
    		@Override
    		public BlockEntityWithoutLevelRenderer getCustomRenderer() 
    		{
    			return new KingsStaffRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    		}
		});
	}
}
