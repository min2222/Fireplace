package com.min01.fireplace.raid;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.min01.fireplace.Fireplace;
import com.min01.fireplace.config.FireplaceConfig;
import com.min01.fireplace.entity.AbstractKaratFeng;
import com.min01.fireplace.init.FireplaceEffects;
import com.min01.fireplace.misc.IKaratRaid;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class KaratRaid 
{
	public static final int SECTION_RADIUS_FOR_FINDING_NEW_VILLAGE_CENTER = 2;
	public static final int ATTEMPT_RAID_FARTHEST = 0;
	public static final int ATTEMPT_RAID_CLOSE = 1;
	public static final int ATTEMPT_RAID_INSIDE = 2;
	public static final int VILLAGE_SEARCH_RADIUS = 32;
	public static final int RAID_TIMEOUT_TICKS = 48000;
	public static final int NUM_SPAWN_ATTEMPTS = 3;
	public static final int VILLAGE_RADIUS_BUFFER = 16;
	public static final int POST_RAID_TICK_LIMIT = 40;
	public static final int DEFAULT_PRE_RAID_TICKS = 300;
	public static final int MAX_NO_ACTION_TIME = 2400;
	public static final int MAX_CELEBRATION_TICKS = 600;
	public static final int OUTSIDE_RAID_BOUNDS_TIMEOUT = 30;
	public static final int TICKS_PER_DAY = 24000;
	public static final int DEFAULT_MAX_BAD_OMEN_LEVEL = 5;
	public static final int LOW_MOB_THRESHOLD = 2;
	private static final Component RAID_NAME_COMPONENT = Component.translatable("event.fireplace.karatraid");
	private static final Component VICTORY = Component.translatable("event.fireplace.karatraid.victory");
	public static final Component DEFEAT = Component.translatable("event.fireplace.karatraid.defeat");
	public static final Component RAID_BAR_VICTORY_COMPONENT = RAID_NAME_COMPONENT.copy().append(" - ").append(VICTORY);
	public static final Component RAID_BAR_DEFEAT_COMPONENT = RAID_NAME_COMPONENT.copy().append(" - ").append(DEFEAT);
	public static final int HERO_OF_THE_VILLAGE_DURATION = 48000;
	public static final int VALID_RAID_RADIUS_SQR = 9216;
	public static final int RAID_REMOVAL_THRESHOLD_SQR = 12544;
	public final Map<Integer, Set<AbstractKaratFeng>> groupRaiderMap = Maps.newHashMap();
	public final Set<UUID> heroesOfTheVillage = Sets.newHashSet();
	public long ticksActive;
	public BlockPos center;
	public final ServerLevel level;
	public boolean started;
	public final int id;
	public float totalHealth;
	public int effectLevel;
	public boolean active;
	public int groupsSpawned;
	public final ServerBossEvent raidEvent;
	public int postRaidTicks;
	public int raidCooldownTicks;
	public final RandomSource random = RandomSource.create();
	public final int numGroups;
	public KaratRaid.RaidStatus status;
	public int celebrationTicks;
	public Optional<BlockPos> waveSpawnPos = Optional.empty();

	public KaratRaid(int p_37692_, ServerLevel p_37693_, BlockPos p_37694_)
	{
		this.raidEvent =  new ServerBossEvent(RAID_NAME_COMPONENT, Fireplace.ORANGE, BossEvent.BossBarOverlay.NOTCHED_10);
		this.id = p_37692_;
		this.level = p_37693_;
		this.active = true;
		this.raidCooldownTicks = 300;
		this.raidEvent.setProgress(0.0F);
		this.center = p_37694_;
		this.numGroups = this.getNumGroups(p_37693_.getDifficulty());
		this.status = KaratRaid.RaidStatus.ONGOING;
	}

	public KaratRaid(ServerLevel p_37696_, CompoundTag p_37697_) 
	{
		this.raidEvent =  new ServerBossEvent(RAID_NAME_COMPONENT, Fireplace.ORANGE, BossEvent.BossBarOverlay.NOTCHED_10);
		this.level = p_37696_;
		this.id = p_37697_.getInt("Id");
		this.started = p_37697_.getBoolean("Started");
		this.active = p_37697_.getBoolean("Active");
		this.ticksActive = p_37697_.getLong("TicksActive");
		this.effectLevel = p_37697_.getInt("EffectLevel");
		this.groupsSpawned = p_37697_.getInt("GroupsSpawned");
		this.raidCooldownTicks = p_37697_.getInt("PreRaidTicks");
		this.postRaidTicks = p_37697_.getInt("PostRaidTicks");
		this.totalHealth = p_37697_.getFloat("TotalHealth");
		this.center = new BlockPos(p_37697_.getInt("CX"), p_37697_.getInt("CY"), p_37697_.getInt("CZ"));
		this.numGroups = p_37697_.getInt("NumGroups");
		this.status = KaratRaid.RaidStatus.getByName(p_37697_.getString("Status"));
		this.heroesOfTheVillage.clear();
		if (p_37697_.contains("HeroesOfTheVillage", 9)) 
		{
			ListTag listtag = p_37697_.getList("HeroesOfTheVillage", 11);

			for (int i = 0; i < listtag.size(); ++i) 
			{
				this.heroesOfTheVillage.add(NbtUtils.loadUUID(listtag.get(i)));
			}
		}
	}

	public boolean isOver()
	{
		return this.isVictory() || this.isLoss();
	}

	public boolean isBetweenWaves() 
	{
		return this.hasFirstWaveSpawned() && this.getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0;
	}

	public boolean hasFirstWaveSpawned()
	{
		return this.groupsSpawned > 0;
	}

	public boolean isStopped()
	{
		return this.status == KaratRaid.RaidStatus.STOPPED;
	}

	public boolean isVictory() 
	{
		return this.status == KaratRaid.RaidStatus.VICTORY;
	}

	public boolean isLoss() 
	{
		return this.status == KaratRaid.RaidStatus.LOSS;
	}

	public float getTotalHealth() 
	{
		return this.totalHealth;
	}

	public Set<AbstractKaratFeng> getAllRaiders() 
	{
		Set<AbstractKaratFeng> set = Sets.newHashSet();

		for (Set<AbstractKaratFeng> set1 : this.groupRaiderMap.values()) 
		{
			set.addAll(set1);
		}

		return set;
	}

	public Level getLevel() 
	{
		return this.level;
	}

	public boolean isStarted()
	{
		return this.started;
	}

	public int getGroupsSpawned()
	{
		return this.groupsSpawned;
	}

	public Predicate<ServerPlayer> validPlayer() 
	{
		return (p_37723_) ->
		{
			BlockPos blockpos = p_37723_.blockPosition();
			return p_37723_.isAlive() && ((IKaratRaid) this.level).getRaidAt(blockpos) == this;
		};
	}

	public void updatePlayers()
	{
		Set<ServerPlayer> set = Sets.newHashSet(this.raidEvent.getPlayers());
		List<ServerPlayer> list = this.level.getPlayers(this.validPlayer());

		for (ServerPlayer serverplayer : list) 
		{
			if (!set.contains(serverplayer))
			{
				this.raidEvent.addPlayer(serverplayer);
			}
		}

		for (ServerPlayer serverplayer1 : set) 
		{
			if (!list.contains(serverplayer1))
			{
				this.raidEvent.removePlayer(serverplayer1);
			}
		}

	}

	public int getMaxEffectLevel()
	{
		return 5;
	}

	public int getEffectLevel()
	{
		return this.effectLevel;
	}

	public void setEffectLevel(int p_150219_) 
	{
		this.effectLevel = p_150219_;
	}

	public void absorbEffect(Player p_37729_) 
	{
		if (p_37729_.hasEffect(this.getEffect()))
		{
			this.effectLevel += p_37729_.getEffect(this.getEffect()).getAmplifier() + 1;
			this.effectLevel = Mth.clamp(this.effectLevel, 0, this.getMaxEffectLevel());
		}

		p_37729_.removeEffect(this.getEffect());
	}
	
	public MobEffect getEffect()
	{
		return FireplaceEffects.KARAT_OMEN.get();
	}

	public void stop()
	{
		this.active = false;
		this.raidEvent.removeAllPlayers();
		this.status = KaratRaid.RaidStatus.STOPPED;
	}

	@SuppressWarnings("deprecation")
	public void tick()
	{
		if (!this.isStopped()) 
		{
			if (this.status == KaratRaid.RaidStatus.ONGOING)
			{
				boolean flag = this.active;
				this.active = this.level.hasChunkAt(this.center);
				if (this.level.getDifficulty() == Difficulty.PEACEFUL) 
				{
					this.stop();
					return;
				}

				if (flag != this.active) 
				{
					this.raidEvent.setVisible(this.active);
				}

				if (!this.active) 
				{
					return;
				}

				if (!this.level.isVillage(this.center)) 
				{
					this.moveRaidCenterToNearbyVillageSection();
				}

				if (!this.level.isVillage(this.center)) 
				{
					if (this.groupsSpawned > 0) 
					{
						this.status = KaratRaid.RaidStatus.LOSS;
					} 
					else 
					{
						this.stop();
					}
				}

				++this.ticksActive;
				if (this.ticksActive >= 48000L)
				{
					this.stop();
					return;
				}

				int i = this.getTotalRaidersAlive();
				if (i == 0 && this.hasMoreWaves()) 
				{
					if (this.raidCooldownTicks <= 0) 
					{
						if (this.raidCooldownTicks == 0 && this.groupsSpawned > 0)
						{
							this.raidCooldownTicks = 300;
							this.raidEvent.setName(RAID_NAME_COMPONENT);
							return;
						}
					}
					else
					{
						boolean flag1 = this.waveSpawnPos.isPresent();
						boolean flag2 = !flag1 && this.raidCooldownTicks % 5 == 0;
						if (flag1 && !this.level.isPositionEntityTicking(this.waveSpawnPos.get()))
						{
							flag2 = true;
						}

						if (flag2)
						{
							int j = 0;
							if (this.raidCooldownTicks < 100)
							{
								j = 1;
							} 
							else if (this.raidCooldownTicks < 40)
							{
								j = 2;
							}

							this.waveSpawnPos = this.getValidSpawnPos(j);
						}

						if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0)
						{
							this.updatePlayers();
						}

						--this.raidCooldownTicks;
						this.raidEvent.setProgress(Mth.clamp((float) (300 - this.raidCooldownTicks) / 300.0F, 0.0F, 1.0F));
					}
				}

				if (this.ticksActive % 20L == 0L)
				{
					this.updatePlayers();
					this.updateRaiders();
					if (i > 0) 
					{
						if (i <= 2)
						{
							this.raidEvent.setName(RAID_NAME_COMPONENT.copy().append(" - ").append(Component.translatable("event.fireplace.karatraid.karats_remaining", i)));
						} 
						else 
						{
							this.raidEvent.setName(RAID_NAME_COMPONENT);
						}
					} 
					else 
					{
						this.raidEvent.setName(RAID_NAME_COMPONENT);
					}
				}

				boolean flag3 = false;
				int k = 0;

				while (this.shouldSpawnGroup()) 
				{
					BlockPos blockpos = this.waveSpawnPos.isPresent() ? this.waveSpawnPos.get() : this.findRandomSpawnPos(k, 20);
					if (blockpos != null) 
					{
						this.started = true;
						this.spawnGroup(blockpos);
						if (!flag3) 
						{
							this.playSound(blockpos);
							flag3 = true;
						}
					} 
					else 
					{
						++k;
					}

					if (k > 3)
					{
						this.stop();
						break;
					}
				}

				if (this.isStarted() && !this.hasMoreWaves() && i == 0) 
				{
					if (this.postRaidTicks < 40)
					{
						++this.postRaidTicks;
					} 
					else 
					{
						this.status = KaratRaid.RaidStatus.VICTORY;

						for (UUID uuid : this.heroesOfTheVillage)
						{
							Entity entity = this.level.getEntity(uuid);
							if (entity instanceof LivingEntity && !entity.isSpectator()) 
							{
								LivingEntity livingentity = (LivingEntity) entity;
								livingentity.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.effectLevel - 1, false, false, true));
								if (livingentity instanceof ServerPlayer) 
								{
									ServerPlayer serverplayer = (ServerPlayer) livingentity;
									serverplayer.awardStat(Stats.RAID_WIN);
									CriteriaTriggers.RAID_WIN.trigger(serverplayer);
								}
							}
						}
					}
				}

				this.setDirty();
			} 
			else if (this.isOver())
			{
				++this.celebrationTicks;
				if (this.celebrationTicks >= 600) 
				{
					this.stop();
					return;
				}

				if (this.celebrationTicks % 20 == 0)
				{
					this.updatePlayers();
					this.raidEvent.setVisible(true);
					if (this.isVictory())
					{
						this.raidEvent.setProgress(0.0F);
						this.raidEvent.setName(RAID_BAR_VICTORY_COMPONENT);
					}
					else 
					{
						this.raidEvent.setName(RAID_BAR_DEFEAT_COMPONENT);
					}
				}
			}
		}
	}

	public void moveRaidCenterToNearbyVillageSection()
	{
		Stream<SectionPos> stream = SectionPos.cube(SectionPos.of(this.center), 2);
		stream.filter(this.level::isVillage).map(SectionPos::center).min(Comparator.comparingDouble((p_37766_) ->
		{
			return p_37766_.distSqr(this.center);
		})).ifPresent(this::setCenter);
	}

	public Optional<BlockPos> getValidSpawnPos(int p_37764_) 
	{
		for (int i = 0; i < 3; ++i)
		{
			BlockPos blockpos = this.findRandomSpawnPos(p_37764_, 1);
			if (blockpos != null)
			{
				return Optional.of(blockpos);
			}
		}

		return Optional.empty();
	}

	public boolean hasMoreWaves() 
	{
		if (this.hasBonusWave()) 
		{
			return !this.hasSpawnedBonusWave();
		} 
		else 
		{
			return !this.isFinalWave();
		}
	}

	public boolean isFinalWave()
	{
		return this.getGroupsSpawned() == this.numGroups;
	}

	public boolean hasBonusWave() 
	{
		return this.effectLevel > 1;
	}

	public boolean hasSpawnedBonusWave() 
	{
		return this.getGroupsSpawned() > this.numGroups;
	}

	public boolean shouldSpawnBonusGroup() 
	{
		return this.isFinalWave() && this.getTotalRaidersAlive() == 0 && this.hasBonusWave();
	}

	public void updateRaiders()
	{
		Iterator<Set<AbstractKaratFeng>> iterator = this.groupRaiderMap.values().iterator();
		Set<AbstractKaratFeng> set = Sets.newHashSet();

		while (iterator.hasNext())
		{
			Set<AbstractKaratFeng> set1 = iterator.next();

			for (AbstractKaratFeng raider : set1) 
			{
				BlockPos blockpos = raider.blockPosition();
				if (!raider.isRemoved() && raider.level.dimension() == this.level.dimension() && !(this.center.distSqr(blockpos) >= 12544.0D))
				{
					if (raider.tickCount > 600)
					{
						if (this.level.getEntity(raider.getUUID()) == null) 
						{
							set.add(raider);
						}

						if (!this.level.isVillage(blockpos) && raider.getNoActionTime() > 2400) 
						{
							raider.setTicksOutsideRaid(raider.getTicksOutsideRaid() + 1);
						}

						if (raider.getTicksOutsideRaid() >= 30) 
						{
							set.add(raider);
						}
					}
				} 
				else 
				{
					set.add(raider);
				}
			}
		}

		for (AbstractKaratFeng raider1 : set) 
		{
			this.removeFromRaid(raider1, true);
		}

	}

	public void playSound(BlockPos p_37744_)
	{
		Collection<ServerPlayer> collection = this.raidEvent.getPlayers();
		long j = this.random.nextLong();

		for (ServerPlayer serverplayer : this.level.players()) 
		{
			Vec3 vec3 = serverplayer.position();
			Vec3 vec31 = Vec3.atCenterOf(p_37744_);
			double d0 = Math.sqrt((vec31.x - vec3.x) * (vec31.x - vec3.x) + (vec31.z - vec3.z) * (vec31.z - vec3.z));
			double d1 = vec3.x + 13.0D / d0 * (vec31.x - vec3.x);
			double d2 = vec3.z + 13.0D / d0 * (vec31.z - vec3.z);
			if (d0 <= 64.0D || collection.contains(serverplayer)) 
			{
				serverplayer.connection.send(new ClientboundSoundPacket(this.getRaidSound(), SoundSource.NEUTRAL, d1, serverplayer.getY(), d2, 64.0F, 1.0F, j));
			}
		}
	}
	
	public SoundEvent getRaidSound()
	{
		return SoundEvents.RAID_HORN;
	}

	public void spawnGroup(BlockPos p_37756_)
	{
		int i = this.groupsSpawned + 1;
		this.totalHealth = 0.0F;
		DifficultyInstance difficultyinstance = this.level.getCurrentDifficultyAt(p_37756_);
		boolean flag1 = this.shouldSpawnBonusGroup();

		Map<EntityType<? extends AbstractKaratFeng>, Integer[]> map = this.getEntityMap();
		for(Map.Entry<EntityType<? extends AbstractKaratFeng>, Integer[]> entry : map.entrySet())
		{
			int j = this.getDefaultNumSpawns(entry.getValue(), i, flag1) + this.getPotentialBonusSpawns(entry.getKey(), this.random, i, difficultyinstance, flag1);

			for (int l = 0; l < j; ++l)
			{
				AbstractKaratFeng raider = entry.getKey().create(this.level);
				this.joinRaid(i, raider, p_37756_, false);
			}
		}

		this.waveSpawnPos = Optional.empty();
		++this.groupsSpawned;
		this.updateBossbar();
		this.setDirty();
	}
	
	public Map<EntityType<? extends AbstractKaratFeng>, Integer[]> getEntityMap()
	{
		return KaratRaidMembers.CUSTOM_RAID_MEMBERS;
	}

	public void joinRaid(int p_37714_, AbstractKaratFeng p_37715_, @Nullable BlockPos p_37716_, boolean p_37717_)
	{
		boolean flag = this.addWaveMob(p_37714_, p_37715_);
		if (flag) 
		{
			p_37715_.setCurrentRaid(this);
			p_37715_.setWave(p_37714_);
			p_37715_.setCanJoinRaid(true);
			p_37715_.setTicksOutsideRaid(0);
			if (!p_37717_ && p_37716_ != null)
			{
				p_37715_.setPos((double) p_37716_.getX() + 0.5D, (double) p_37716_.getY() + 1.0D, (double) p_37716_.getZ() + 0.5D);
				p_37715_.finalizeSpawn(this.level, this.level.getCurrentDifficultyAt(p_37716_), MobSpawnType.EVENT, (SpawnGroupData) null, (CompoundTag) null);
				//p_37715_.applyRaidBuffs(p_37714_, false);
				p_37715_.setOnGround(true);
				this.level.addFreshEntityWithPassengers(p_37715_);
			}
		}
	}

	public void updateBossbar()
	{
		this.raidEvent.setProgress(Mth.clamp(this.getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F));
	}

	public float getHealthOfLivingRaiders() 
	{
		float f = 0.0F;

		for (Set<AbstractKaratFeng> set : this.groupRaiderMap.values())
		{
			for (AbstractKaratFeng raider : set) 
			{
				f += raider.getHealth();
			}
		}

		return f;
	}

	public boolean shouldSpawnGroup() 
	{
		return this.raidCooldownTicks == 0 && (this.groupsSpawned < this.numGroups || this.shouldSpawnBonusGroup()) && this.getTotalRaidersAlive() == 0;
	}

	public int getTotalRaidersAlive()
	{
		return this.groupRaiderMap.values().stream().mapToInt(Set::size).sum();
	}

	public void removeFromRaid(AbstractKaratFeng p_37741_, boolean p_37742_)
	{
		Set<AbstractKaratFeng> set = this.groupRaiderMap.get(p_37741_.getWave());
		if (set != null) 
		{
			boolean flag = set.remove(p_37741_);
			if (flag) 
			{
				if (p_37742_) 
				{
					this.totalHealth -= p_37741_.getHealth();
				}

				p_37741_.setCurrentRaid(null);
				this.updateBossbar();
				this.setDirty();
			}
		}
	}

	public void setDirty() 
	{
		this.level.getRaids().setDirty();
	}

	@SuppressWarnings("deprecation")
	@Nullable
	public BlockPos findRandomSpawnPos(int p_37708_, int p_37709_)
	{
		int i = p_37708_ == 0 ? 2 : 2 - p_37708_;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int i1 = 0; i1 < p_37709_; ++i1) 
		{
			float f = this.level.random.nextFloat() * ((float) Math.PI * 2F);
			int j = this.center.getX() + Mth.floor(Mth.cos(f) * 32.0F * (float) i) + this.level.random.nextInt(5);
			int l = this.center.getZ() + Mth.floor(Mth.sin(f) * 32.0F * (float) i) + this.level.random.nextInt(5);
			int k = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, j, l);
			blockpos$mutableblockpos.set(j, k, l);
			if (!this.level.isVillage(blockpos$mutableblockpos) || p_37708_ >= 2)
			{
				if (this.level.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10,
						blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)
						&& this.level.isPositionEntityTicking(blockpos$mutableblockpos)
						&& (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, blockpos$mutableblockpos, EntityType.RAVAGER) 
								|| this.level.getBlockState(blockpos$mutableblockpos.below()).is(Blocks.SNOW) 
								&& this.level.getBlockState(blockpos$mutableblockpos).isAir())) 
				{	
					return blockpos$mutableblockpos;
				}
			}
		}

		return null;
	}

	public boolean addWaveMob(int p_37753_, AbstractKaratFeng p_37754_)
	{
		return this.addWaveMob(p_37753_, p_37754_, true);
	}

	public boolean addWaveMob(int p_37719_, AbstractKaratFeng p_37720_, boolean p_37721_)
	{
		this.groupRaiderMap.computeIfAbsent(p_37719_, (p_37746_) ->
		{
			return Sets.newHashSet();
		});
		Set<AbstractKaratFeng> set = this.groupRaiderMap.get(p_37719_);
		AbstractKaratFeng raider = null;

		for (AbstractKaratFeng raider1 : set)
		{
			if (raider1.getUUID().equals(p_37720_.getUUID())) 
			{
				raider = raider1;
				break;
			}
		}

		if (raider != null) 
		{
			set.remove(raider);
			set.add(p_37720_);
		}

		set.add(p_37720_);
		if (p_37721_) 
		{
			this.totalHealth += p_37720_.getHealth();
		}

		this.updateBossbar();
		this.setDirty();
		return true;
	}

	public BlockPos getCenter()
	{
		return this.center;
	}

	public void setCenter(BlockPos p_37761_)
	{
		this.center = p_37761_;
	}

	public int getId()
	{
		return this.id;
	}

	public int getDefaultNumSpawns(Integer[] spawnsPerWaveBeforeBonus, int p_37732_, boolean p_37733_)
	{
		return p_37733_ ? spawnsPerWaveBeforeBonus[this.numGroups]	: spawnsPerWaveBeforeBonus[p_37732_ - 1];
	}

	public int getPotentialBonusSpawns(EntityType<? extends AbstractKaratFeng> p_219829_, RandomSource p_219830_, int p_219831_, DifficultyInstance p_219832_, boolean p_219833_)
	{
		return 0;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public CompoundTag save(CompoundTag p_37748_) 
	{
		p_37748_.putInt("Id", this.id);
		p_37748_.putBoolean("Started", this.started);
		p_37748_.putBoolean("Active", this.active);
		p_37748_.putLong("TicksActive", this.ticksActive);
		p_37748_.putInt("EffectLevel", this.effectLevel);
		p_37748_.putInt("GroupsSpawned", this.groupsSpawned);
		p_37748_.putInt("PreRaidTicks", this.raidCooldownTicks);
		p_37748_.putInt("PostRaidTicks", this.postRaidTicks);
		p_37748_.putFloat("TotalHealth", this.totalHealth);
		p_37748_.putInt("NumGroups", this.numGroups);
		p_37748_.putString("Status", this.status.getName());
		p_37748_.putInt("CX", this.center.getX());
		p_37748_.putInt("CY", this.center.getY());
		p_37748_.putInt("CZ", this.center.getZ());
		ListTag listtag = new ListTag();

		for (UUID uuid : this.heroesOfTheVillage) 
		{
			listtag.add(NbtUtils.createUUID(uuid));
		}

		p_37748_.put("HeroesOfTheVillage", listtag);
		return p_37748_;
	}

	public int getNumGroups(Difficulty p_37725_) 
	{
    	//since list start from zero, if you set max wave to 7, total wave will be 8, so we need to - 1 from value
		return FireplaceConfig.maxWave.get() - 1;
	}

	public float getEnchantOdds() 
	{
		int i = this.getEffectLevel();
		if (i == 2) 
		{
			return 0.1F;
		} 
		else if (i == 3)
		{
			return 0.25F;
		} 
		else if (i == 4) 
		{
			return 0.5F;
		} 
		else 
		{
			return i == 5 ? 0.75F : 0.0F;
		}
	}

	public void addHeroOfTheVillage(Entity p_37727_) 
	{
		this.heroesOfTheVillage.add(p_37727_.getUUID());
	}

	static enum RaidStatus
	{
		ONGOING, VICTORY, LOSS, STOPPED;

		public static final KaratRaid.RaidStatus[] VALUES = values();

		static KaratRaid.RaidStatus getByName(String p_37804_)
		{
			for (KaratRaid.RaidStatus raid$raidstatus : VALUES)
			{
				if (p_37804_.equalsIgnoreCase(raid$raidstatus.name()))
				{
					return raid$raidstatus;
				}
			}

			return ONGOING;
		}

		public String getName() 
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
