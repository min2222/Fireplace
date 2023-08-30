package com.min01.fireplace.misc;

import java.util.Iterator;
import java.util.UUID;

import com.min01.fireplace.Main;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.goal.DodgeArrowsGoal;

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

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	private static final String TAG_ARROW_DODGE_CHECKED = Main.MODID + "_dodge_check";
	
	@SubscribeEvent
	public static void karatSummon(LivingTickEvent event)
	{
		if(!event.getEntity().level.isClientSide)
		{
			ServerLevel serverlevel = (ServerLevel) event.getEntity().level;
			if(event.getEntity().getPersistentData().contains("karatUUID"))
			{
				EntityKaratFeng karat = (EntityKaratFeng) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID("karatUUID"));
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
			if(entity.getPersistentData().contains("karatUUID"))
			{
				UUID uuid = entity.getPersistentData().getUUID("karatUUID");
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(event.getEntity().getPersistentData().contains("karatUUID"))
				{
					if(event.getEntity().getPersistentData().getUUID("karatUUID").equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
		if(directentity != null)
		{
			if(directentity.getPersistentData().contains("karatUUID"))
			{
				UUID uuid = directentity.getPersistentData().getUUID("karatUUID");
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(event.getEntity().getPersistentData().contains("karatUUID"))
				{
					if(event.getEntity().getPersistentData().getUUID("karatUUID").equals(uuid))
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
		if(entity.getPersistentData().contains("karatUUID"))
		{
			UUID uuid = entity.getPersistentData().getUUID("karatUUID");
			if(newtarget != null)
			{
				if(newtarget.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(newtarget.getPersistentData().contains("karatUUID"))
				{
					UUID newtargetUUID = newtarget.getPersistentData().getUUID("karatUUID");
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
				if(originaltarget.getPersistentData().contains("karatUUID"))
				{
					UUID originaltargetUUID = originaltarget.getPersistentData().getUUID("karatUUID");
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
			if(event.getEntity().getPersistentData().contains("karatUUID"))
			{
				EntityKaratFeng karat = (EntityKaratFeng) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID("karatUUID"));
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
				if(owner.getPersistentData().contains("karatUUID"))
				{
					UUID uuid = owner.getPersistentData().getUUID("karatUUID");
					if(hitentity.getUUID().equals(uuid))
					{
						event.setCanceled(true);
					}
					if(hitentity.getPersistentData().contains("karatUUID"))
					{
						UUID hituuid = hitentity.getPersistentData().getUUID("karatUUID");
						if(hituuid.equals(uuid))
						{
							event.setCanceled(true);
						}
					}
				}
			}
			if(proj.getPersistentData().contains("karatUUID"))
			{
				UUID uuid = proj.getPersistentData().getUUID("karatUUID");
				if(hitentity.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				if(hitentity.getPersistentData().contains("karatUUID"))
				{
					UUID hituuid = hitentity.getPersistentData().getUUID("karatUUID");
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
				if(owner.getPersistentData().contains("karatUUID"))
				{
					proj.getPersistentData().putUUID("karatUUID", owner.getPersistentData().getUUID("karatUUID"));
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
