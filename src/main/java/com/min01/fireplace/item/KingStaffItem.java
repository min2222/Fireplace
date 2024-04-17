package com.min01.fireplace.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.event.ClientEventHandler;
import com.min01.fireplace.item.renderer.KingsStaffRenderer;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

public class KingStaffItem extends Item
{
	public KingStaffItem() 
	{
		super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).setNoRepair().durability(1000));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_)
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
		p_41433_.startUsingItem(p_41434_);
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) 
	{
		int tick = this.getUseDuration(p_41430_) - p_41431_ + 1;
    	if(p_41428_.isClientSide)
    	{
            float f = p_41429_.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)p_41429_.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            p_41428_.addParticle(ParticleTypes.FLAME, true, p_41429_.getX() + (double)f1 * 0.6D, p_41429_.getY() + 1.8D, p_41429_.getZ() + (double)f2 * 0.6D, 0, 0, 0);
            p_41428_.addParticle(ParticleTypes.FLAME, true, p_41429_.getX() - (double)f1 * 0.6D, p_41429_.getY() + 1.8D, p_41429_.getZ() - (double)f2 * 0.6D, 0, 0, 0);
    	}
		if(tick > 20)
		{
			List<LivingEntity> entityList = new ArrayList<>();
			for(EntityType<?> type : ForgeRegistries.ENTITY_TYPES)
			{
				if(type != EntityType.ENDER_DRAGON)
				{
					Entity entity = type.create(p_41428_);
					if(entity instanceof LivingEntity living)
					{
						if(!(living instanceof AbstractKaratFeng))
						{
							entityList.add(living);
						}
					}
				}
			}
			int rand = (int)Math.floor(Math.random()*entityList.size());
			LivingEntity living = entityList.get(rand);
			if(!p_41428_.isClientSide)
			{
				FireplaceUtil.shootFromRotation(living, p_41429_, p_41429_.getXRot(), p_41429_.getYRot(), 0.0F, 1.5F, 1.0F);
				living.setPos(p_41429_.position());
				if(living instanceof Mob mob)
				{
					mob.finalizeSpawn((ServerLevelAccessor) p_41428_, p_41428_.getCurrentDifficultyAt(p_41429_.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				}
				p_41428_.addFreshEntity(living);
			}
			
			p_41429_.releaseUsingItem();
			
			if(p_41429_ instanceof Player player)
			{
				player.getCooldowns().addCooldown(this, 40);
				p_41430_.hurtAndBreak(1, player, (p_40665_) ->
				{
					p_40665_.broadcastBreakEvent(player.getUsedItemHand());
				});
			}
		}
	}
	
	@Override
	public int getUseDuration(ItemStack p_43107_)
	{
		return 72000;
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
    		
    		@Override
    		public @Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) 
    		{
    			return ClientEventHandler.KING_STAFF;
    		}
		});
	}
    
    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) 
    {
    	return UseAnim.CUSTOM;
    }
}
