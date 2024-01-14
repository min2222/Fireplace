package com.min01.fireplace.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.model.ModelKaratFeng;
import com.min01.fireplace.misc.EntityTimer;
import com.min01.fireplace.network.EntityTimerSyncPacket;
import com.min01.fireplace.network.FireplaceNetwork;
import com.replaymod.replay.ReplayModReplay;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.server.ServerLifecycleHooks;

public class FireplaceUtil 
{
	public static final Map<UUID, EntityTimer> TIMER_MAP = new HashMap<>();
	public static final Map<UUID, EntityTimer> CLIENT_TIMER_MAP = new HashMap<>();
	public static final EntityTimer ENTITY_TIMER = new EntityTimer(20.0F, 0L);
	
	public static final String[] UUID = new String[] { "karatUUID", "necroFengUUID" };
	public static final String KARAT_UUID = UUID[0];
	public static final String NECRO_UUID = UUID[1];
	public static final List<Mob> NECRO_LIST = new ArrayList<>();
	public static final Map<LivingEntity, Integer> GRAVITY_MAP = new HashMap<>();
	
	public static final String REPLAYMOD = "replaymod";
	public static final String MINS_UNIVERSE = "minsuniverse";
	
	public static boolean isNotReplay()
	{
		if(FireplaceUtil.isModLoaded(REPLAYMOD))
		{
			if(ReplayModReplay.instance.getReplayHandler() == null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	public static boolean hasMU()
	{
		return FireplaceUtil.isModLoaded(MINS_UNIVERSE);
	}
	
    public static void setTickrate(Entity entity, float tickrate)
    {
    	if(!hasMU())
    	{
        	if(!entity.level.isClientSide)
        	{
            	for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
            	{
            		FireplaceNetwork.CHANNEL.sendTo(new EntityTimerSyncPacket(entity.getUUID(), tickrate, false), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            	}
            	
        		if(TIMER_MAP.containsKey(entity.getUUID()))
        		{
        			TIMER_MAP.remove(entity.getUUID());
        		}
        		
    			TIMER_MAP.put(entity.getUUID(), new EntityTimer(tickrate, 0));
        	}
    	}
    }
    
    public static void resetTickrate(Entity entity)
    {
    	if(!hasMU())
    	{
        	if(!entity.level.isClientSide)
        	{
            	for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
            	{
            		FireplaceNetwork.CHANNEL.sendTo(new EntityTimerSyncPacket(entity.getUUID(), 0, true), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
            	}
            	
        		if(TIMER_MAP.containsKey(entity.getUUID()))
        		{
        			TIMER_MAP.remove(entity.getUUID());
        		}
        	}
    	}
    }
    
	public static boolean isModLoaded(String modid)
	{
		return ModList.get().isLoaded(modid);
	}
	
	public static LivingEntity createBreath(Level level, LivingEntity caster, Vec3 startPosition, double range)
	{
		Vec3 direction = caster.getViewVector(0);
		Vec3 endpoint = startPosition.add(direction.scale(range));
		HitResult result = FireplaceUtil.rayTrace(level, startPosition, endpoint, 2, LivingEntity.class, (entity) -> entity == caster);
        if(result instanceof EntityHitResult entityHit)
        {
        	if(entityHit.getEntity() instanceof LivingEntity living)
        	{
        		return living;
        	}
        }
        return null;
	}
	
	public static void createBreathParticle(Level level, ParticleOptions particle, LivingEntity caster, Vec3 startPosition, double range, double particleVelocity, double particleSpacing, double particleJitter)
	{
		Vec3 direction = caster.getViewVector(0);
        spawnParticleRay(level, particle, startPosition, direction, caster, range, particleVelocity, particleSpacing, particleJitter);
	}
	
    public static void spawnParticleRay(Level level, ParticleOptions particle, Vec3 origin, Vec3 direction, @Nullable LivingEntity caster, double distance, double particleVelocity, double particleSpacing, double particleJitter)
    {
        Vec3 velocity = direction.scale(particleVelocity);

        for(double d = particleSpacing; d <= distance; d += particleSpacing) 
        {
            double x = origin.x + d * direction.x + particleJitter * (level.random.nextDouble() * 2 - 1);
            double y = origin.y + d * direction.y + particleJitter * (level.random.nextDouble() * 2 - 1);
            double z = origin.z + d * direction.z + particleJitter * (level.random.nextDouble() * 2 - 1);
            level.addParticle(particle, x, y, z, velocity.x, velocity.y, velocity.z);
        }
    }
    
    @Nullable
    public static HitResult rayTrace(Level world, Vec3 origin, Vec3 endpoint, float aimAssist, Class<? extends Entity> entityType, Predicate<? super Entity> filter) 
    {
        float borderSize = 1 + aimAssist;

        AABB searchVolume = new AABB(origin.x, origin.y, origin.z, endpoint.x, endpoint.y, endpoint.z).inflate(borderSize, borderSize, borderSize);

        List<? extends Entity> entities = world.getEntitiesOfClass(entityType, searchVolume);
        entities.removeIf(filter);

        HitResult result = world.clip(new ClipContext(origin, endpoint, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));

        if (result != null)
        {
            endpoint = result.getLocation();
        }

        Entity closestHitEntity = null;
        Vec3 closestHitPosition = endpoint;
        AABB entityBounds;
        Vec3 intercept = null;

        for (Entity entity : entities)
        {
            float fuzziness = entity instanceof LivingEntity ? aimAssist : 0;

            entityBounds = entity.getBoundingBox();

            if (entityBounds != null)
            {
                float entityBorderSize = entity.getPickRadius();
                if (entityBorderSize != 0)
                {
                    entityBounds = entityBounds.inflate(entityBorderSize, entityBorderSize, entityBorderSize);
                }

                if (fuzziness != 0) entityBounds = entityBounds.inflate(fuzziness, fuzziness, fuzziness);

                Optional<Vec3> hit = entityBounds.clip(origin, endpoint);
                if (hit.isPresent())
                {
                    intercept = hit.get();
                }
            }

            if (intercept != null) 
            {
                float currentHitDistance = (float) intercept.distanceTo(origin);
                float closestHitDistance = (float) closestHitPosition.distanceTo(origin);
                if (currentHitDistance < closestHitDistance) 
                {
                    closestHitEntity = entity;
                    closestHitPosition = intercept;
                }
            }
        }

        if (closestHitEntity != null) 
        {
            result = new EntityHitResult(closestHitEntity, closestHitPosition);
        }

        return result;
    }
	
    public static void tick(LivingEntity owner, LivingEntity morph)
    {
        LivingEntity livingInstance = morph;

        syncEntityPosRot(livingInstance, owner);

        if(livingInstance.canUpdate())
        {
        	livingInstance.tick();
        }

        syncEntity(livingInstance, owner);

        livingInstance.getEntityData().clearDirty();
    }
    
    public static void syncEntity(LivingEntity living, LivingEntity owner)
    {
    	syncEntityPosRot(living, owner);
        
        living.animationPosition = owner.animationPosition;
        living.animationSpeed = owner.animationSpeed;

        living.setDeltaMovement(owner.getDeltaMovement());

        living.horizontalCollision = owner.horizontalCollision;
        living.verticalCollision = owner.verticalCollision;
        living.setOnGround(owner.isOnGround());
        living.setSwimming(owner.isSwimming());
        living.setSprinting(owner.isSprinting());

        living.setHealth(living.getMaxHealth() * (owner.getHealth() / owner.getMaxHealth()));
        living.hurtTime = owner.hurtTime;
        living.deathTime = owner.deathTime;
        living.fallDistance = owner.fallDistance;

        living.swingTime = owner.swingTime;
        living.swinging = owner.swinging;
        living.swingingArm = owner.swingingArm;
        living.attackAnim = owner.attackAnim;
        living.oAttackAnim = owner.oAttackAnim;
    }
	
    public static void syncEntityPosRot(LivingEntity living, LivingEntity player)
    {
        living.tickCount = player.tickCount;

        living.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        living.xOld = player.xOld;
        living.yOld = player.yOld;
        living.zOld = player.zOld;

        living.xo = player.xo;
        living.yo = player.yo;
        living.zo = player.zo;

        living.yRotO = player.yRotO;
        living.xRotO = player.xRotO;

        living.yHeadRot = player.yHeadRot;
        living.yHeadRotO = player.yHeadRotO;

        living.yBodyRot = player.yBodyRot;
        living.yBodyRotO = player.yBodyRotO;
    }
	
	public static void addSpreadingParticle(int maxCount, Level level, ParticleOptions particle, Vec3 position)
	{
    	for(int i = 0; i < maxCount; i++)
    	{
    		level.addAlwaysVisibleParticle(particle, position.x, position.y, position.z, level.random.nextGaussian() * 0.2D, level.random.nextGaussian() * 0.2D, level.random.nextGaussian() * 0.2D);
    	}
	}
	
	public static <T extends AbstractKaratFeng> ModelKaratFeng<T> getKaratModel(EntityRendererProvider.Context context)
	{
		return new ModelKaratFeng<>(context.bakeLayer(ModelLayers.PLAYER), false);
	}
	
	public static ResourceLocation getKaratTexture(String name)
	{
		return new ResourceLocation(Fireplace.MODID, "textures/entity/" + name + ".png");
	}
	
	public static Vec3 getEntityShootVector(Entity from, Entity to)
	{
		Vec3 motion = new Vec3(to.getX() - from.getX(), to.getEyeY() - from.getY(), to.getZ() - from.getZ()).normalize();
		return motion;
	}
	
	public static void shootFromRotation(Entity entity, Entity owner, float p_37253_, float p_37254_, float p_37255_, float p_37256_, float p_37257_)
	{
		float f = -Mth.sin(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
		float f1 = -Mth.sin((p_37253_ + p_37255_) * ((float)Math.PI / 180F));
		float f2 = Mth.cos(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
		shoot(entity, (double)f, (double)f1, (double)f2, p_37256_, p_37257_);
		Vec3 vec3 = owner.getDeltaMovement();
		entity.setDeltaMovement(entity.getDeltaMovement().add(vec3.x, owner.isOnGround() ? 0.0D : vec3.y, vec3.z));
	}
	
	public static void shoot(Entity entity, double p_37266_, double p_37267_, double p_37268_, float p_37269_, float p_37270_) 
	{
		Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().add(entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), entity.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_)).scale((double)p_37269_);
		entity.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		entity.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
		entity.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
		entity.yRotO = entity.getYRot();
		entity.xRotO = entity.getXRot();
	}
}
