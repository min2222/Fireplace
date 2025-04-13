package com.min01.fireplace.config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class FireplaceConfig
{
	public static final FireplaceConfig CONFIG;
	public static final ForgeConfigSpec CONFIG_SPEC;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatSummoningBlackList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatProjectileBlackList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatSummoningWhiteList;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> karatProjectileWhiteList;
	public static ForgeConfigSpec.BooleanValue useBlackListForSummoning;
	public static ForgeConfigSpec.BooleanValue useBlackListForProjectile;
	public static ConfigValue<List<? extends String>> karatRaidMembers;
	public static ConfigValue<Integer> maxWave;
	
    static 
    {
    	Pair<FireplaceConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(FireplaceConfig::new);
    	CONFIG = pair.getLeft();
    	CONFIG_SPEC = pair.getRight();
    }
	
    public FireplaceConfig(ForgeConfigSpec.Builder config) 
    {
        config.push("Settings");
        FireplaceConfig.karatSummoningBlackList = config.comment(new String[] { "if you put any mob name or mod id in this list, karat can't summon listed mobs or mobs from listed mods. example : minecraft:zombie, modid example : minecraft" }).defineList("karatSummoningBlackList", Arrays.asList(new String[] { "fireplace:karat_feng", "minecraft:allay", "minecraft:fox", "minecraft:ocelot", "minecraft:cat", "minecraft:panda", "minecraft:dolphin", "minecraft:axolotl", "too_much_bosses:screen_shake", "leosillagers:snowing_cloud", "the_light:elite_titan", "the_light:king_titan", "minsuniverse:dimensional_administrator", "alternacraft:tylosaurus", "ars_nouveau:animated_head", "tokusatsu_hero_completion_plan", "twilightforest:chain_block"}), String.class::isInstance);
        FireplaceConfig.karatProjectileBlackList = config.comment(new String[] { "if you put any projectile name or mod id in this list, karat can't shoot listed projectiles or projectiles from listed mods. example : minecraft:arrow, modid example : minecraft" }).defineList("karatProjectileBlackList", Arrays.asList(new String[] { "minecraft:snowball", "minecraft:firework_rocket", "tokusatsu_hero_completion_plan"}), String.class::isInstance);
        FireplaceConfig.useBlackListForSummoning = config.comment("use blacklist for karat's summoning, false to use whitelist").define("useBlackListForSummoning", true);
        FireplaceConfig.useBlackListForProjectile = config.comment("use blacklist for karat's projectile shooting, false to use whitelist").define("useBlackListForProjectile", true);
        FireplaceConfig.karatSummoningWhiteList = config.comment(new String[] { "put any mod id here, then karat only summon mobs from listed mods. example : minecraft" }).defineList("karatSummoningWhiteList", Arrays.asList(new String[] { "minecraft" }), String.class::isInstance);
        FireplaceConfig.karatProjectileWhiteList = config.comment(new String[] { "put any mod id here, then karat only shoot projectiles from listed mods. example : minecraft" }).defineList("karatProjectileWhiteList", Arrays.asList(new String[] { "minecraft" }), String.class::isInstance);
        FireplaceConfig.karatRaidMembers = config.comment("put any type of karat id for ultra karatted raid. example : fireplace:karat_feng=1,5,0,0,0,0,0").define("karatRaidMembers", Arrays.asList(new String[] {
        		"fireplace:carrot_fang=3,5,8,10,15,10,10,15,20,30", 
        		"fireplace:snowy_feng=3,5,5,4,6,6,5,8,12,20", 
        		"fireplace:evoker_feng=10,15,10,8,7,7,7,9,15,20", 
        		"fireplace:santa_feng=6,7,8,9,10,10,15,10,15,20", 
        		"fireplace:vampire_feng=8,7,8,9,10,12,15,20,15,20", 
        		"fireplace:fire_feng=5,7,8,9,10,10,20,15,10,30", 
        		"fireplace:necro_feng=4,5,6,8,7,8,10,15,20,30",
        		"fireplace:alien_feng=0,0,0,0,0,0,0,0,0,0"}), String.class::isInstance);
        FireplaceConfig.maxWave = config.comment("max wave number for ultra karatted raid").define("maxWave", 10);
        config.pop();
    }
}
