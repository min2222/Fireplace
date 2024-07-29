package com.min01.fireplace.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.entity.EntityNecroFeng;
import com.min01.fireplace.entity.EntitySnowyFeng;
import com.min01.fireplace.misc.FireplaceTags;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(Projectile.class)
public class MixinProjectile 
{
	@Inject(at = @At("HEAD"), method = "onHit", cancellable = true)
    private void onHit(HitResult p_37260_, CallbackInfo ci)
    {
		Projectile proj = Projectile.class.cast(this);
		
		if(p_37260_.getType() == HitResult.Type.ENTITY)
		{
			EntityHitResult hitresult = (EntityHitResult) p_37260_;
			Entity hitentity = hitresult.getEntity();
			if(proj.getOwner() != null)
			{
				Entity owner = proj.getOwner();
				
				if(owner instanceof EntitySnowyFeng)
				{
					boolean flag = hitentity.getType().is(FireplaceTags.THE_FENGS) ? ((AbstractKaratFeng) hitentity).getCurrentRaid() == null : true;
					if(flag)
					{
						hitentity.hurt(hitentity.damageSources().mobProjectile(proj, (LivingEntity) proj.getOwner()), 3);
						hitentity.setTicksFrozen(100);
					}
				}
				
				if(owner.getType().is(FireplaceTags.THE_FENGS) && hitentity.getType().is(FireplaceTags.THE_FENGS))
				{
					if(((AbstractKaratFeng) owner).getCurrentRaid() != null && ((AbstractKaratFeng) hitentity).getCurrentRaid() != null)
					{
						ci.cancel();
					}
				}
				
				for(int i = 0; i < FireplaceUtil.UUID.length; i++)
				{
					String string = FireplaceUtil.UUID[i];
					if(owner.getPersistentData().contains(string))
					{
						UUID uuid = owner.getPersistentData().getUUID(string);
						
						//hit entity is owner?
						if(hitentity.getUUID().equals(uuid))
						{
							ci.cancel();
						}
						
						//hit entity is allied?
						if(hitentity.getPersistentData().contains(string))
						{
							UUID hituuid = hitentity.getPersistentData().getUUID(string);
							if(hituuid.equals(uuid))
							{
								ci.cancel();
							}
						}
					}
					
					if(!(owner.level instanceof ServerLevel))
						return;
					if(owner.getPersistentData().contains(string))
					{
						UUID uuid = owner.getPersistentData().getUUID(string);
						Entity uuidEntity = ((ServerLevel)owner.level).getEntity(uuid);
						if(uuidEntity instanceof EntityNecroFeng necro)
						{
							//hit entity is feng?
							if(necro != null)
							{
								if(necro.getCurrentRaid() != null && hitentity instanceof AbstractKaratFeng feng)
								{
									if(feng.getCurrentRaid() != null)
									{
										ci.cancel();
									}
								}
							}
						}
					}
					
					//hit entity is necro fengs undead?
					if(hitentity.getPersistentData().contains(string))
					{
						UUID uuid = hitentity.getPersistentData().getUUID(string);
						Entity uuidEntity = ((ServerLevel)owner.level).getEntity(uuid);
						if(uuidEntity instanceof EntityNecroFeng necro)
						{
							if(necro != null)
							{
								if(necro.getCurrentRaid() != null && owner instanceof AbstractKaratFeng feng)
								{
									if(feng.getCurrentRaid() != null)
									{
										ci.cancel();
									}
								}
							}
						}
					}
				}
			}
			else
			{
				for(int i = 0; i < FireplaceUtil.UUID.length; i++)
				{
					String string = FireplaceUtil.UUID[i];
					if(proj.getPersistentData().contains(string))
					{
						UUID uuid = proj.getPersistentData().getUUID(string);
						
						//hit entity is feng?
						if(hitentity instanceof AbstractKaratFeng feng)
						{
							if(feng.getCurrentRaid() != null)
							{
								ci.cancel();
							}
						}
						
						//hit entity is owner?
						if(hitentity.getUUID().equals(uuid))
						{
							ci.cancel();
						}
						
						//hit entity is allied?
						if(hitentity.getPersistentData().contains(string))
						{
							UUID hituuid = hitentity.getPersistentData().getUUID(string);
							if(hituuid.equals(uuid))
							{
								ci.cancel();
							}
						}
					}
				}
			}
		}
    }
}
