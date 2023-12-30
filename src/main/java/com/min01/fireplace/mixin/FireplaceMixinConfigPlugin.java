package com.min01.fireplace.mixin;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class FireplaceMixinConfigPlugin implements IMixinConfigPlugin
{
	private final boolean hasMU = hasClass("com.min01.universe.MinsUniverse");
	
	public FireplaceMixinConfigPlugin() throws IOException 
	{
		
	}
	
	static boolean hasClass(String name) throws IOException 
	{
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.replace('.', '/') + ".class");
		if (stream != null) 
		{
			stream.close();
		}

		return stream != null;
	}
	
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
		return mixinClassName.endsWith("EntityTimer") ? !this.hasMU : true;
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
