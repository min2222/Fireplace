package com.min01.fireplace.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.min01.fireplace.entity.goal.DodgeArrowsGoal;
import com.min01.fireplace.entity.goal.KaratBuffMobsGoal;
import com.min01.fireplace.entity.goal.KaratEatingGoldenAppleGoal;
import com.min01.fireplace.entity.goal.KaratMeleeAttackGoal;
import com.min01.fireplace.entity.goal.KaratMovingGoal;
import com.min01.fireplace.entity.goal.KaratRangedAttackGoal;
import com.min01.fireplace.entity.goal.KaratShootProjectileGoal;
import com.min01.fireplace.entity.goal.KaratSplashPotionsGoal;
import com.min01.fireplace.entity.goal.KaratSummonMobGoal;
import com.min01.fireplace.entity.goal.KaratUsingShieldGoal;
import com.min01.fireplace.init.FireplaceItems;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityKaratFeng extends AbstractKaratFeng
{
	public ArrayList<LivingEntity> entityList = new ArrayList<>();
	public static final EntityDataAccessor<Float> MAX_SUMMONING_HP = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> EQUIP_INDEX = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> EQUIP_TICK = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_MELEE = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_CHANGE_EQUIP = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_SHIELDING = SynchedEntityData.defineId(EntityKaratFeng.class, EntityDataSerializers.BOOLEAN);
	public FlyingMoveControl flyingControl = new FlyingMoveControl(this, 20, false);
	public Item[] diamondSet = {Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.SHIELD, Items.DIAMOND_SWORD};
	public Item[] netheriteSet = {Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS, Items.SHIELD, Items.NETHERITE_AXE};
	public EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND};
	
	public EntityKaratFeng(EntityType<? extends Monster> p_21368_, Level p_21369_) 
	{
		super(p_21368_, p_21369_);
		this.moveControl = this.flyingControl;
		this.setNoGravity(true);
	}
    
    @Override
    protected void registerGoals() 
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(4, new KaratSummonMobGoal(this));
    	this.goalSelector.addGoal(4, new KaratMeleeAttackGoal(this, 0.65D, false));
    	this.goalSelector.addGoal(4, new KaratBuffMobsGoal(this));
    	this.goalSelector.addGoal(4, new KaratShootProjectileGoal(this));
    	this.goalSelector.addGoal(4, new KaratUsingShieldGoal(this));
    	this.goalSelector.addGoal(4, new KaratEatingGoldenAppleGoal(this));
    	this.goalSelector.addGoal(4, new KaratSplashPotionsGoal(this));
    	this.goalSelector.addGoal(4, new KaratRangedAttackGoal(this));
    	this.goalSelector.addGoal(4, new KaratMovingGoal(this));
    	this.goalSelector.addGoal(4, new DodgeArrowsGoal(this, 100.0F));
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(MAX_SUMMONING_HP, 300.0F);
    	this.entityData.define(EQUIP_INDEX, 0);
    	this.entityData.define(EQUIP_TICK, 0);
    	this.entityData.define(IS_FLYING, true);
    	this.entityData.define(IS_MELEE, false);
    	this.entityData.define(IS_CHANGE_EQUIP, false);
    	this.entityData.define(IS_SHIELDING, false);
    }
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 200.0D)
    			.add(Attributes.MOVEMENT_SPEED, 0.35D)
        		.add(Attributes.ARMOR, 2)
        		.add(Attributes.ARMOR_TOUGHNESS, 2)
        		.add(Attributes.ATTACK_DAMAGE, 4)
        		.add(Attributes.FLYING_SPEED, 0.45D)
        		.add(Attributes.FOLLOW_RANGE, 200.0D);
    }
    
    @Override
    protected PathNavigation createNavigation(Level p_186262_) 
    {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return this.isFlying() ? flyingpathnavigation : super.createNavigation(p_186262_);
    }
    
	@Override
	public void travel(Vec3 p_218382_) 
	{
		if(this.isFlying())
		{
			if(this.isEffectiveAi() || this.isControlledByLocalInstance())
			{
				this.moveRelative(this.getSpeed(), p_218382_);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale((double)0.5));
			}
			this.calculateEntityAnimation(this, false);
		}
		else
		{
			super.travel(p_218382_);
		}
	}
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, SpawnGroupData p_21437_, CompoundTag p_21438_) 
    {
    	this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
    	this.clearSummonedEntities();
    	return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();

        this.navigation = this.createNavigation(this.level);
    	
    	if(this.level.isClientSide && this.isPreparingSkill())
    	{
            float f = this.yBodyRot * ((float)Math.PI / 180F) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);
            this.level.addParticle(ParticleTypes.FLAME, true, this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, 0, 0, 0);
            this.level.addParticle(ParticleTypes.FLAME, true, this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, 0, 0, 0);
    	}
    	
    	if(!this.isChangeEquip())
    	{
        	if(this.getTarget() != null)
        	{
            	this.lookAt(Anchor.EYES, this.getTarget().getEyePosition());
        		if(this.isMelee())
        		{
        			this.getNavigation().moveTo(this.getTarget(), this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
        		}
        	}
        	
        	if(!this.isMelee())
        	{
            	if(this.getHealth() <= this.getMaxHealth() / 2 || this.getRemainSummoningHP() <= 0)
            	{
            		this.setHealth(this.getMaxHealth() / 2);
            		this.clearSummonedEntities();
            		this.startMelee();
            		this.setIsChangeEquip(true);
            	}
        	}
    	}
    	else
    	{
    		switch(this.getPhase())
    		{
    		case 0:
    			this.equipDiamond();
    			break;
    		case 1:
    			this.equipNetherite();
    			break;
    		case 2:
    			this.equipNetheriteWithEnchantment();
    			break;
    		}
    	}
    	
    	if(this.isFlying())
    	{
        	this.resetFallDistance();
    	}
    }
    
    @Override
    public void remove(RemovalReason p_146834_) 
    {
    	super.remove(p_146834_);
    	this.clearSummonedEntities();
    }
    
    @Override
    public void die(DamageSource p_21014_) 
    {
    	int phase = this.getPhase();
    	
    	if(p_21014_.isBypassInvul() || phase >= 2)
    	{
        	super.die(p_21014_);
        	
        	if(p_21014_.getEntity() != null && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
        	{
        		this.spawnAtLocation(FireplaceItems.KING_STAFF.get());
        	}
    	}
    	else
    	{
        	switch(phase)
        	{
        	case 0:
        		this.changePhase();
    			this.setRemainSummoningHP(600.0F);
        		this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.55D);
        		break;
        	case 1:
        		this.changePhase();
    			this.setRemainSummoningHP(900.0F);
        		this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.65D);
        		break;
        	}
    	}
    }
    
    public void equipDiamond()
    {
    	if(this.getEquipIndex() <= 5)
    	{
        	Item item = this.diamondSet[this.getEquipIndex()];
        	EquipmentSlot slots = this.slots[this.getEquipIndex()];
        	ItemStack stack = new ItemStack(item);
        	this.setEquipTick(this.getEquipTick() + 1);
        	if(this.getEquipTick() >= 20)
        	{
            	this.setItemSlot(slots, stack);
            	this.swing(InteractionHand.MAIN_HAND);
            	this.playSound(SoundEvents.ARMOR_EQUIP_DIAMOND);
            	this.setEquipIndex(this.getEquipIndex() + 1);
            	this.setEquipTick(0);
        	}
        	else
        	{
        		this.setEquipTick(this.getEquipTick() + 1);
            	this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        	}
    	}
    	else
    	{
    		this.setIsChangeEquip(false);
    		this.setEquipIndex(0);
    	}
    }
    
    public void equipNetherite()
    {
    	if(this.getEquipIndex() <= 5)
    	{
        	Item item = this.netheriteSet[this.getEquipIndex()];
        	EquipmentSlot slots = this.slots[this.getEquipIndex()];
        	ItemStack stack = new ItemStack(item);
        	this.setEquipTick(this.getEquipTick() + 1);
        	if(this.getEquipTick() >= 20)
        	{
            	this.setItemSlot(slots, stack);
            	this.swing(InteractionHand.MAIN_HAND);
            	this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE);
            	this.setEquipIndex(this.getEquipIndex() + 1);
            	this.setEquipTick(0);
        	}
        	else
        	{
        		this.setEquipTick(this.getEquipTick() + 1);
            	this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        	}
    	}
    	else
    	{
    		this.setIsChangeEquip(false);
    		this.setEquipIndex(0);
    	}
    }
    
    public void equipNetheriteWithEnchantment()
    {
    	if(this.getEquipIndex() <= 5)
    	{
			Item enchantedNetheriteHelmet = Items.NETHERITE_HELMET;
			Item enchantedNetheriteChestplate = Items.NETHERITE_CHESTPLATE;
			Item enchantedNetheriteLeggings = Items.NETHERITE_LEGGINGS;
			Item enchantedNetheriteBoots = Items.NETHERITE_BOOTS;
			Item enchantedNetheriteAxe = Items.NETHERITE_AXE;
			ItemStack helmetStack = new ItemStack(enchantedNetheriteHelmet);
			ItemStack chestStack = new ItemStack(enchantedNetheriteChestplate);
			ItemStack legsStack = new ItemStack(enchantedNetheriteLeggings);
			ItemStack feetsStack = new ItemStack(enchantedNetheriteBoots);
			ItemStack handStack = new ItemStack(enchantedNetheriteAxe);
			handStack.enchant(Enchantments.SHARPNESS, 5);
			helmetStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			chestStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			legsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			feetsStack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
			ItemStack[] enchantedNetheriteSet = {helmetStack, chestStack, legsStack, feetsStack, new ItemStack(Items.SHIELD), handStack};
        	EquipmentSlot slots = this.slots[this.getEquipIndex()];
        	ItemStack stack = enchantedNetheriteSet[this.getEquipIndex()];
        	this.setEquipTick(this.getEquipTick() + 1);
        	if(this.getEquipTick() >= 20)
        	{
            	this.setItemSlot(slots, stack.copy());
            	this.swing(InteractionHand.MAIN_HAND);
            	this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE);
            	this.setEquipIndex(this.getEquipIndex() + 1);
            	this.setEquipTick(0);
        	}
        	else
        	{
        		this.setEquipTick(this.getEquipTick() + 1);
            	this.setItemSlot(EquipmentSlot.MAINHAND, stack.copy());
        	}
    	}
    	else
    	{
    		this.setIsChangeEquip(false);
    		this.setEquipIndex(0);
    	}
    }
    
    public void changePhase()
    {
		this.setHealth(200.0F);
		this.setPhase(this.getPhase() + 1);
		this.clearSummonedEntities();
		this.startFlying();
    }
    
    public void startFlying()
    {
		this.setNoGravity(true);
		this.setIsFlying(true);
		this.setIsMelee(false);
		this.moveControl = this.flyingControl;
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(FireplaceItems.KING_STAFF.get()));
    }
    
    public void startMelee()
    {
		this.setNoGravity(false);
		this.setIsFlying(false);
		this.setIsMelee(true);
		this.moveControl = new MoveControl(this);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65D);
    }
    
    public void clearSummonedEntities()
    {
    	for(int i = 0; i < this.entityList.size(); i++)
    	{
    		LivingEntity living = this.entityList.get(i);
    		if(living.isAlive())
    		{
    			living.discard();
    		}
    	}
    	
    	if(!this.level.isClientSide)
    	{
    		ServerLevel serverlevel = (ServerLevel) this.level;
    		List<Entity> list = Lists.newArrayList(serverlevel.getAllEntities().iterator());
    		for(int i = 0; i < list.size(); i++)
    		{
    			Entity entity = list.get(i);
    			if(entity != null)
    			{
        			if(entity.getPersistentData() != null)
        			{
            			if(entity.getPersistentData().contains(FireplaceUtil.KARAT_UUID))
            			{
            				UUID uuid = entity.getPersistentData().getUUID(FireplaceUtil.KARAT_UUID);
            				if(uuid.equals(this.getUUID()))
            				{
            					entity.discard();
            				}
            			}
        			}
    			}
    		}
    	}

    	this.entityList.clear();
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_)
    {
        if(this.canBlockDamageSource(p_21016_))
        {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.8f + this.level.random.nextFloat() * 0.4f);
            return false;
        }
        if(this.isChangeEquip() && !p_21016_.isBypassInvul())
        {
        	return false;
        }
    	return super.hurt(p_21016_, p_21017_);
    }
    
    private boolean canBlockDamageSource(DamageSource damageSourceIn)
    {
        Entity entity = damageSourceIn.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow) 
        {
        	AbstractArrow abstractarrowentity = (AbstractArrow) entity;
            if (abstractarrowentity.getPierceLevel() > 0) 
            {
                flag = true;
            }
        }
        if (!damageSourceIn.isBypassArmor() && this.isBlocking() && !flag) 
        {
        	Vec3 vec3d2 = damageSourceIn.getSourcePosition();
            if (vec3d2 != null)
            {
                Vec3 vec3d3 = this.getViewVector(1.0f);
                Vec3 vec3d4 = vec3d2.vectorTo(this.position()).normalize();
                vec3d4 = new Vec3(vec3d4.x, 0.0, vec3d4.z);
                if (vec3d4.dot(vec3d3) < 0.0) 
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setEquipTick(int value)
    {
    	this.entityData.set(EQUIP_TICK, value);
    }
    
    public int getEquipTick()
    {
    	return this.entityData.get(EQUIP_TICK);
    }
    
    public void setEquipIndex(int value)
    {
    	this.entityData.set(EQUIP_INDEX, value);
    }
    
    public int getEquipIndex()
    {
    	return this.entityData.get(EQUIP_INDEX);
    }
    
    public void setIsMelee(boolean value)
    {
    	this.entityData.set(IS_MELEE, value);
    }
    
    public boolean isMelee()
    {
    	return this.entityData.get(IS_MELEE);
    }
    
    public void setIsFlying(boolean value)
    {
    	this.entityData.set(IS_FLYING, value);
    }
    
    public boolean isFlying()
    {
    	return this.entityData.get(IS_FLYING);
    }
    
    public void setRemainSummoningHP(float value)
    {
    	this.entityData.set(MAX_SUMMONING_HP, value);
    }
    
    public float getRemainSummoningHP()
    {
    	return this.entityData.get(MAX_SUMMONING_HP);
    }
    
    public void setIsChangeEquip(boolean value)
    {
    	this.entityData.set(IS_CHANGE_EQUIP, value);
    }
    
    public boolean isChangeEquip()
    {
    	return this.entityData.get(IS_CHANGE_EQUIP);
    }
    
    public void setShielding(boolean value)
    {
    	this.entityData.set(IS_SHIELDING, value);
    }
    
    public boolean isShielding()
    {
    	return this.entityData.get(IS_SHIELDING);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setIsFlying(p_21450_.getBoolean("isFlying"));
    	this.setPhase(p_21450_.getInt("Phase"));
    	this.setIsChangeEquip(p_21450_.getBoolean("changeEquip"));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isFlying", this.isFlying());
    	p_21484_.putInt("Phase", this.getPhase());
    	p_21484_.putBoolean("changeEquip", this.isChangeEquip());
    }
    
    @Override
    protected float getEquipmentDropChance(EquipmentSlot p_21520_) 
    {
    	return 0;
    }
    
    @Override
    public boolean shouldShowName() 
    {
    	return true;
    }
	
    @Override
    public Component getCustomName() 
    {
    	return Component.literal("KaratFeng");
    }

	@Override
	public int getMaxHurtCount() 
	{
		return 6;
	}
}
