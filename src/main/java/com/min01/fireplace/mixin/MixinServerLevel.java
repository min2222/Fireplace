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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
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
	
	protected MixinServerLevel(WritableLevelData p_220352_, ResourceKey<Level> p_220353_, Holder<DimensionType> p_220354_, Supplier<ProfilerFiller> p_220355_, boolean p_220356_, boolean p_220357_, long p_220358_, int p_220359_)
	{
		super(p_220352_, p_220353_, p_220354_, p_220355_, p_220356_, p_220357_, p_220358_, p_220359_);
	}
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;Z)V")
	private void init(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey<Level> p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List<CustomSpawner> p_215008_, boolean p_215009_, CallbackInfo ci)
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
	public KaratRaid getKaratRaidAt(BlockPos pos)
	{
		return this.raidSaveData.getNearbyRaid(pos, 9216);
	}
	
	@Override
	public KaratRaidSaveData getKaratRaids() 
	{
		return this.raidSaveData;
	}
}
