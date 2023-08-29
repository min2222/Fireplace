package com.min01.fireplace.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FireplaceConfig
{
	private static ForgeConfigSpec.Builder common_builder;
	public static ForgeConfigSpec common_config;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatSummoningBlackList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatProjectileBlackList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatSummoningWhiteList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatProjectileWhiteList;
	public static ForgeConfigSpec.BooleanValue useBlackListForSummoning;
	public static ForgeConfigSpec.BooleanValue useBlackListForProjectile;
	
    public static void loadConfig(ForgeConfigSpec config, String path) 
    {
        CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
    
    static 
    {
    	common_builder = new ForgeConfigSpec.Builder();
    	FireplaceConfig.init(FireplaceConfig.common_builder);
        common_config = FireplaceConfig.common_builder.build();
    }
	
    public static void init(ForgeConfigSpec.Builder common) 
    {
        common.push("Common settings");
        FireplaceConfig.karatSummoningBlackList = common.comment(new String[] { "if you put any mob name or mod id in this list, karat can't summon listed mobs or mobs from listed mods. example : minecraft:zombie, modid example : minecraft" }).defineList("karatSummoningBlackList", Arrays.asList(new String[] { "fireplace:karat_feng", "minecraft:allay", "minecraft:fox", "minecraft:ocelot", "minecraft:cat", "minecraft:panda", "minecraft:dolphin", "minecraft:axolotl", "too_much_bosses:screen_shake", "leosillagers:snowing_cloud", "the_light:elite_titan", "the_light:king_titan", "minsuniverse:dimensional_administrator", "alternacraft:tylosaurus", "ars_nouveau:animated_head", "tokusatsu_hero_completion_plan", "twilightforest:chain_block"}), String.class::isInstance);
        FireplaceConfig.karatProjectileBlackList = common.comment(new String[] { "if you put any projectile name or mod id in this list, karat can't shoot listed projectiles or projectiles from listed mods. example : minecraft:arrow, modid example : minecraft" }).defineList("karatProjectileBlackList", Arrays.asList(new String[] { "minecraft:snowball", "minecraft:firework_rocket", "tokusatsu_hero_completion_plan"}), String.class::isInstance);
        FireplaceConfig.useBlackListForSummoning = common.comment("use blacklist for karat's summoning, false to use whitelist").define("useBlackListForSummoning", true);
        FireplaceConfig.useBlackListForProjectile = common.comment("use blacklist for karat's projectile shooting, false to use whitelist").define("useBlackListForProjectile", true);
        FireplaceConfig.karatSummoningWhiteList = common.comment(new String[] { "put any mod id here, then karat only summon mobs from listed mods. example : minecraft" }).defineList("karatSummoningWhiteList", Arrays.asList(new String[] { "minecraft" }), String.class::isInstance);
        FireplaceConfig.karatProjectileWhiteList = common.comment(new String[] { "put any mod id here, then karat only shoot projectiles from listed mods. example : minecraft" }).defineList("karatProjectileWhiteList", Arrays.asList(new String[] { "minecraft" }), String.class::isInstance);
        common.pop();
    }
}
