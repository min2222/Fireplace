package com.min01.fireplace.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.min01.fireplace.util.FireplaceUtil;

public class FireplaceMixinConfigPlugin implements IMixinConfigPlugin
{
	@Override
	public void onLoad(String mixinPackage)
	{
		
	}

	@Override
	public String getRefMapperConfig() 
	{
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return mixinClassName.endsWith("EntityTimer") ? !FireplaceUtil.hasMU() : true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) 
	{
		
	}

	@Override
	public List<String> getMixins() 
	{
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) 
	{

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) 
	{
		
	}
}
