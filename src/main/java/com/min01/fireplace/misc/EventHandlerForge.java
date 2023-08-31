package com.min01.fireplace.misc;

import java.util.Iterator;
import java.util.UUID;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.goal.DodgeArrowsGoal;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fireplace.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	private static final String TAG_ARROW_DODGE_CHECKED = Fireplace.MODID + "_dodge_check";
	
	@SubscribeEvent
	public static void karatSummon(LivingTickEvent event)
	{
		if(!event.getEntity().level.isClientSide)
		{
			ServerLevel serverlevel = (ServerLevel) event.getEntity().level;
			if(event.getEntity().getPersistentData().contains(FireplaceUtil.KARAT_UUID))
			{
				EntityKaratFeng karat = (EntityKaratFeng) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID(FireplaceUtil.KARAT_UUID));
				if(karat != null)
				{
					if(karat.getTarget() != null)
					{
						((Mob) event.getEntity()).setTarget(karat.getTarget());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatSummon(LivingDamageEvent event)
	{
		Entity entity = event.getSource().getEntity();
		Entity directentity = event.getSource().getEntity();
		
		if(entity != null)
		{
			if(entity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
			{
				UUID uuid = entity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(event.getEntity().getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					if(event.getEntity().getPersistentData().getUUID(FireplaceUtil.KARAT_UUID).equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
		if(directentity != null)
		{
			if(directentity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
			{
				UUID uuid = directentity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(event.getEntity().getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					if(event.getEntity().getPersistentData().getUUID(FireplaceUtil.KARAT_UUID).equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatSummon(LivingChangeTargetEvent event)
	{
		LivingEntity entity = event.getEntity();
		LivingEntity newtarget = event.getNewTarget();
		LivingEntity originaltarget = event.getOriginalTarget();
		
		if(newtarget != null && newtarget.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
		{
			if(entity.getUUID().equals(newtarget.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID)))
			{
				event.setCanceled(true);
			}
		}
		
		if(originaltarget != null && originaltarget.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
		{
			if(entity.getUUID().equals(originaltarget.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID)))
			{
				event.setCanceled(true);
			}
		}
		
		if(entity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
		{
			UUID uuid = entity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
			if(newtarget != null)
			{
				if(newtarget.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(newtarget.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					UUID newtargetUUID = newtarget.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
					if(uuid.equals(newtargetUUID))
					{
						event.setCanceled(true);
					}
				}
			}
			if(originaltarget != null)
			{
				if(originaltarget.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(originaltarget.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					UUID originaltargetUUID = originaltarget.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
					if(uuid.equals(originaltargetUUID))
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatSummon(EntityLeaveLevelEvent event)
	{
		if(event.getLevel() instanceof ServerLevel)
		{
			ServerLevel serverlevel = (ServerLevel) event.getLevel();
			if(event.getEntity().getPersistentData().contains(FireplaceUtil.KARAT_UUID))
			{
				EntityKaratFeng karat = (EntityKaratFeng) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID(FireplaceUtil.KARAT_UUID));
				if(karat != null && !karat.entityList.isEmpty())
				{
					for(Iterator<LivingEntity> itr = karat.entityList.iterator(); itr.hasNext();)
					{
						LivingEntity living = itr.next();
						if(living == event.getEntity())
						{
							itr.remove();
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatProjectile(ProjectileImpactEvent event)
	{
		Projectile proj = event.getProjectile();
		if(event.getRayTraceResult().getType() == HitResult.Type.ENTITY)
		{
			EntityHitResult hitresult = (EntityHitResult) event.getRayTraceResult();
			Entity hitentity = hitresult.getEntity();
			if(proj.getOwner() != null)
			{
				Entity owner = proj.getOwner();
				if(owner.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					UUID uuid = owner.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
					if(hitentity.getUUID().equals(uuid))
					{
						event.setCanceled(true);
					}
					if(hitentity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
					{
						UUID hituuid = hitentity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
						if(hituuid.equals(uuid))
						{
							event.setCanceled(true);
						}
					}
				}
			}
			if(proj.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
			{
				UUID uuid = proj.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
				if(hitentity.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(hitentity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					UUID hituuid = hitentity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
					if(hituuid.equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
    public static void karatSummon(EntityJoinLevelEvent event)
	{
		if(event.getEntity() instanceof Projectile)
		{
			Projectile proj = (Projectile) event.getEntity();
			if(proj.getOwner() != null)
			{
				Entity owner = proj.getOwner();
				if(owner.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
				{
					proj.getPersistentData().putUUID(FireplaceUtil.KARAT_UUID, owner.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID));
				}
			}
		}
	}
	
	@SubscribeEvent
    public static void onJoinWorld(EntityJoinLevelEvent event)
	{
        if(event.getLevel().isClientSide() || !event.getEntity().isAlive()) return;
        
        if(event.getEntity() instanceof Projectile && !event.getEntity().getPersistentData().getBoolean(TAG_ARROW_DODGE_CHECKED)) 
        {
            event.getEntity().getPersistentData().putBoolean(TAG_ARROW_DODGE_CHECKED, true);
            DodgeArrowsGoal.doDodgeCheckForArrow(event.getEntity());
        }
    }
}
