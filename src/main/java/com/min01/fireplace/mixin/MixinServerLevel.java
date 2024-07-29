package com.min01.fireplace.mixin;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.fireplace.misc.IKaratRaid;
import com.min01.fireplace.raid.KaratRaid;
import com.min01.fireplace.raid.KaratRaidSaveData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends Level implements IKaratRaid
{
	@Unique
	private KaratRaidSaveData raidSaveData;
	
	protected MixinServerLevel(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) 
	{
		super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
	}
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)V")
	private void init(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey<Level> p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List<CustomSpawner> p_215008_, boolean p_215009_, RandomSequences p_288977_, CallbackInfo ci)
	{
		this.raidSaveData = ServerLevel.class.cast(this).getDataStorage().computeIfAbsent((p_184095_) ->
		{
			return KaratRaidSaveData.load(ServerLevel.class.cast(this), p_184095_);
		}, () -> 
		{
			return KaratRaidSaveData.load(ServerLevel.class.cast(this), new CompoundTag());
		}, KaratRaidSaveData.getFileId(this.dimensionTypeRegistration()));
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(BooleanSupplier p_8794_, CallbackInfo ci) 
	{
		this.raidSaveData.tick();
	}
	
	@Override
	public KaratRaid getRaidAt(BlockPos pos)
	{
		return this.raidSaveData.getNearbyRaid(pos, 9216);
	}
	
	@Override
	public KaratRaidSaveData getRaids() 
	{
		return this.raidSaveData;
	}
}
