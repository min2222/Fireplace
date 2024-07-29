package com.min01.fireplace.event;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.EntityKaratFeng;
import com.min01.fireplace.entity.EntityNecroFeng;
import com.min01.fireplace.entity.goal.DodgeArrowsGoal;
import com.min01.fireplace.raid.KaratRaidMembers;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fireplace.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	private static final String TAG_ARROW_DODGE_CHECKED = Fireplace.MODID + "_dodge_check";
	
	@SubscribeEvent
	public static void registerWaveMembers(LevelEvent.Load event)
	{
		KaratRaidMembers.registerWaveMembers();       
	}
	
	@SubscribeEvent
	public static void karatExplosion(ExplosionEvent.Detonate event)
	{
    	Entity sourceMob = event.getExplosion().getDirectSourceEntity();
    	List<Entity> list = event.getAffectedEntities();
    	
		for(int i = 0; i < FireplaceUtil.UUID.length; i++)
		{
			preventExplodedByAllieOrOwner(sourceMob, list, FireplaceUtil.UUID[i]);
		}
	}
	
	public static void preventExplodedByAllieOrOwner(Entity sourceMob, List<Entity> list, String name)
	{
		//when fengs cause explode and fengs affected
    	if(sourceMob != null)
    	{
    		if(sourceMob instanceof AbstractKaratFeng sourceFeng)
    		{
    			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
    			{
    				Entity entity = itr.next();
    				if(entity instanceof AbstractKaratFeng feng)
    				{
    					if(sourceFeng.getCurrentRaid() != null && feng.getCurrentRaid() != null)
    					{
        					itr.remove();
    					}
    				}
    			}
    		}
    	}
    	
		//when summoned cause explode, and karat feng affected
    	if(sourceMob != null)
    	{
    		if(sourceMob.getPersistentData().contains(name))
    		{
    			UUID uuid = sourceMob.getPersistentData().getUUID(name);
    			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
    			{
    				Entity entity = itr.next();
    				if(entity.getUUID().equals(uuid))
    				{
    					itr.remove();
    				}
    			}
    		}
    	}
    	
		//when karat feng cause explode, and summoned affected
    	if(sourceMob != null)
    	{
			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
			{
				Entity entity = itr.next();
				if(entity.getPersistentData().contains(name))
				{
					UUID uuid = entity.getPersistentData().getUUID(name);
					if(sourceMob.getUUID().equals(uuid))
					{
						itr.remove();
					}
				}
			}
    	}
    	
		//when summoned cause explode, and summoned affected
    	//this handle necro feng undeads too
    	if(sourceMob != null)
    	{
    		if(sourceMob.getPersistentData().contains(name))
    		{
    			UUID uuid = sourceMob.getPersistentData().getUUID(name);
    			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
    			{
    				Entity entity = itr.next();
    				if(entity.getPersistentData().contains(name))
    				{
    					if(uuid.equals(entity.getPersistentData().getUUID(name)))
    					{
        					itr.remove();
    					}
    				}
    			}
    		}
    	}
    	
		//when undead cause explode, and fengs affected
    	if(sourceMob != null)
    	{
    		if(!(sourceMob.level instanceof ServerLevel))
    			return;
    		if(sourceMob.getPersistentData().contains(name))
    		{
    			UUID uuid = sourceMob.getPersistentData().getUUID(name);
    			Entity uuidEntity = ((ServerLevel)sourceMob.level).getEntity(uuid);
    			if(uuidEntity instanceof EntityNecroFeng necrofeng)
    			{
        			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
        			{
        				Entity entity = itr.next();
        				if(entity instanceof AbstractKaratFeng feng)
        				{
        					if(necrofeng != null) 
        					{
            					if(necrofeng.getCurrentRaid() != null && feng.getCurrentRaid() != null)
            					{
                					itr.remove();
            					}
        					}
        				}
        			}
    			}
    		}
    	}
    	
		//when fengs cause explode, and undead affected
    	if(sourceMob != null)
    	{
    		if(!(sourceMob.level instanceof ServerLevel))
    			return;
    		if(sourceMob instanceof AbstractKaratFeng feng)
    		{
    			for(Iterator<Entity> itr = list.iterator(); itr.hasNext();)
    			{
    				Entity entity = itr.next();
    				if(entity.getPersistentData().contains(name))
    				{
    	    			UUID uuid = entity.getPersistentData().getUUID(name);
    	    			Entity uuidEntity = ((ServerLevel)entity.level).getEntity(uuid);
    	    			if(uuidEntity instanceof EntityNecroFeng necrofeng)
    	    			{
        					if(necrofeng != null) 
        					{
            					if(necrofeng.getCurrentRaid() != null && feng.getCurrentRaid() != null)
            					{
                					itr.remove();
            					}
        					}
    	    			}
    				}
    			}
    		}
    	}
	}
	
	@SubscribeEvent
	public static void karatTick(LivingTickEvent event)
	{
		if(!event.getEntity().level.isClientSide)
		{
			ServerLevel serverlevel = (ServerLevel) event.getEntity().level;
			
			for(int i = 0; i < FireplaceUtil.UUID.length; i++)
			{
				syncTargetWithOwner(event, serverlevel, FireplaceUtil.UUID[i]);
			}
		}
	}
	
	public static void syncTargetWithOwner(LivingTickEvent event, ServerLevel serverlevel, String name)
	{
		if(event.getEntity().getPersistentData().contains(name))
		{
			Mob karat = (Mob) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID(name));
			if(karat != null)
			{
				if(karat.getTarget() != null)
				{
					((Mob) event.getEntity()).setTarget(karat.getTarget());
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatDamge(LivingDamageEvent event)
	{
		Entity entity = event.getSource().getEntity();
		Entity directentity = event.getSource().getEntity();
		
		for(int i = 0; i < FireplaceUtil.UUID.length; i++)
		{
			preventDamageOwnerOrAllie(event, entity, directentity, FireplaceUtil.UUID[i]);
		}
	}
	
	public static void preventDamageOwnerOrAllie(LivingDamageEvent event, Entity entity, Entity directentity, String name)
	{
		//when hit
		if(entity != null)
		{
			if(entity.getPersistentData().contains(name))
			{
				UUID uuid = entity.getPersistentData().getUUID(name);
				//if owner
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				
				//if same team
				if(event.getEntity().getPersistentData().contains(name))
				{
					if(event.getEntity().getPersistentData().getUUID(name).equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
		
		//when hit with non-living entity
		if(directentity != null)
		{
			if(directentity.getPersistentData().contains(name))
			{
				UUID uuid = directentity.getPersistentData().getUUID(name);
				//same above
				if(event.getEntity().getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				
				//same above
				if(event.getEntity().getPersistentData().contains(name))
				{
					if(event.getEntity().getPersistentData().getUUID(name).equals(uuid))
					{
						event.setCanceled(true);
					}
				}
			}
		}
		//when hit
		if(entity != null)
		{
			if(!(entity.level instanceof ServerLevel))
				return;
			if(entity.getPersistentData().contains(name))
			{
				UUID uuid = entity.getPersistentData().getUUID(name);
				Entity uuidEntity = ((ServerLevel)entity.level).getEntity(uuid);
    			if(uuidEntity instanceof EntityNecroFeng necro)
    			{
    				//if undead hit feng
    				if(necro != null)
    				{
    					if(necro.getCurrentRaid() != null && event.getEntity() instanceof AbstractKaratFeng feng)
    					{
    						if(feng.getCurrentRaid() != null)
    						{
    							event.setCanceled(true);
    						}
    					}
    				}
    			}
			}
			
			if(!(event.getEntity().level instanceof ServerLevel))
				return;
			//if feng hit undead
			if(event.getEntity().getPersistentData().contains(name))
			{
				UUID uuid = event.getEntity().getPersistentData().getUUID(name);
				Entity uuidEntity = ((ServerLevel)event.getEntity().level).getEntity(uuid);
    			if(uuidEntity instanceof EntityNecroFeng necro)
    			{
    				if(necro != null)
    				{
    					if(necro.getCurrentRaid() != null && entity instanceof AbstractKaratFeng feng)
    					{
    						if(feng.getCurrentRaid() != null)
    						{
    							event.setCanceled(true);
    						}
    					}
    				}
    			}
			}
		}
		
		//when hit with non-living entity
		if(directentity != null)
		{
			if(!(directentity.level instanceof ServerLevel))
				return;
			if(directentity.getPersistentData().contains(name))
			{
				UUID uuid = directentity.getPersistentData().getUUID(name);
				Entity uuidEntity = ((ServerLevel)directentity.level).getEntity(uuid);
    			if(uuidEntity instanceof EntityNecroFeng necro)
    			{
    				//same above
    				if(necro != null)
    				{
    					if(necro.getCurrentRaid() != null && event.getEntity() instanceof AbstractKaratFeng feng)
    					{
    						if(feng.getCurrentRaid() != null)
    						{
    							event.setCanceled(true);
    						}
    					}
    				}
    			}
			}

			if(!(event.getEntity().level instanceof ServerLevel))
				return;
			//same above
			if(event.getEntity().getPersistentData().contains(name))
			{
				UUID uuid = event.getEntity().getPersistentData().getUUID(name);
				Entity uuidEntity = ((ServerLevel)event.getEntity().level).getEntity(uuid);
    			if(uuidEntity instanceof EntityNecroFeng necro)
    			{
    				if(necro != null)
    				{
    					if(necro.getCurrentRaid() != null && directentity instanceof AbstractKaratFeng feng)
    					{
    						if(feng.getCurrentRaid() != null)
    						{
    							event.setCanceled(true);
    						}
    					}
    				}
    			}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatChangeTarget(LivingChangeTargetEvent event)
	{
		LivingEntity entity = event.getEntity();
		LivingEntity newtarget = event.getNewTarget();
		LivingEntity originaltarget = event.getOriginalTarget();

		for(int i = 0; i < FireplaceUtil.UUID.length; i++)
		{
			preventTargetingOwnerOrTeam(event, entity, newtarget, originaltarget, FireplaceUtil.UUID[i]);
		}
	}
	
	public static void preventTargetingOwnerOrTeam(LivingChangeTargetEvent event, Entity entity, Entity newtarget, Entity originaltarget, String name)
	{
		//when target is owner
		if(newtarget != null && newtarget.getPersistentData().contains(name))
		{
			if(entity.getUUID().equals(newtarget.getPersistentData().getUUID(name)))
			{
				event.setCanceled(true);
			}
		}
		
		//same above
		if(originaltarget != null && originaltarget.getPersistentData().contains(name))
		{
			if(entity.getUUID().equals(originaltarget.getPersistentData().getUUID(name)))
			{
				event.setCanceled(true);
			}
		}
		
		//when target is allied with same owner
		if(entity.getPersistentData().contains(name))
		{
			UUID uuid = entity.getPersistentData().getUUID(name);
			if(newtarget != null)
			{
				if(newtarget.getUUID().equals(uuid))
				{
					event.setCanceled(true);
				}
				
				if(newtarget.getPersistentData().contains(name))
				{
					UUID newtargetUUID = newtarget.getPersistentData().getUUID(name);
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
				
				if(originaltarget.getPersistentData().contains(name))
				{
					UUID originaltargetUUID = originaltarget.getPersistentData().getUUID(name);
					if(uuid.equals(originaltargetUUID))
					{
						event.setCanceled(true);
					}
				}
			}
		}
		
		if(!(entity.level instanceof ServerLevel))
			return;
		//when target is feng
		if(newtarget != null && entity.getPersistentData().contains(name))
		{
			UUID uuid = entity.getPersistentData().getUUID(name);
			Entity uuidEntity = ((ServerLevel)entity.level).getEntity(uuid);
			if(uuidEntity instanceof EntityNecroFeng necro)
			{
				if(necro != null)
				{
					if(necro.getCurrentRaid() != null && newtarget instanceof AbstractKaratFeng feng)
					{
						if(feng.getCurrentRaid() != null)
						{
							event.setCanceled(true);					
						}
					}
				}
			}
		}
		
		//same above
		if(originaltarget != null && entity.getPersistentData().contains(name))
		{
			UUID uuid = entity.getPersistentData().getUUID(name);
			Entity uuidEntity = ((ServerLevel)entity.level).getEntity(uuid);
			if(uuidEntity instanceof EntityNecroFeng necro)
			{
				if(necro != null)
				{
					if(necro.getCurrentRaid() != null && originaltarget instanceof AbstractKaratFeng feng)
					{
						if(feng.getCurrentRaid() != null)
						{
							event.setCanceled(true);					
						}
					}
				}
			}
		}
		
		//when target is undead
		if(newtarget != null && newtarget.getPersistentData().contains(name))
		{
			UUID uuid = newtarget.getPersistentData().getUUID(name);
			Entity uuidEntity = ((ServerLevel)newtarget.level).getEntity(uuid);
			if(uuidEntity instanceof EntityNecroFeng necro)
			{
				if(necro != null)
				{
					if(necro.getCurrentRaid() != null && entity instanceof AbstractKaratFeng feng)
					{
						if(feng.getCurrentRaid() != null)
						{
							event.setCanceled(true);					
						}
					}
				}
			}
		}
		
		//same above
		if(originaltarget != null && originaltarget.getPersistentData().contains(name))
		{
			UUID uuid = originaltarget.getPersistentData().getUUID(name);
			Entity uuidEntity = ((ServerLevel)originaltarget.level).getEntity(uuid);
			if(uuidEntity instanceof EntityNecroFeng necro)
			{
				if(necro != null)
				{
					if(necro.getCurrentRaid() != null && entity instanceof AbstractKaratFeng feng)
					{
						if(feng.getCurrentRaid() != null)
						{
							event.setCanceled(true);					
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void karatLeaveLevel(EntityLeaveLevelEvent event)
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
			
			if(event.getEntity().getPersistentData().contains(FireplaceUtil.NECRO_UUID))
			{
				EntityNecroFeng karat = (EntityNecroFeng) serverlevel.getEntity(event.getEntity().getPersistentData().getUUID(FireplaceUtil.NECRO_UUID));
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
    public static void karatJoinLevel(EntityJoinLevelEvent event)
	{
		if(event.getEntity() instanceof Projectile)
		{
			Projectile proj = (Projectile) event.getEntity();
			if(proj.getOwner() != null)
			{
				Entity owner = proj.getOwner();
				for(int i = 0; i < FireplaceUtil.UUID.length; i++)
				{
					syncUUIDWithProjectilOwner(owner, proj, FireplaceUtil.UUID[i]);
				}
			}
		}
	}
	
	public static void syncUUIDWithProjectilOwner(Entity owner, Projectile proj, String name)
	{
		if(owner.getPersistentData().contains(name))
		{
			proj.getPersistentData().putUUID(name, owner.getPersistentData().getUUID(name));
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
