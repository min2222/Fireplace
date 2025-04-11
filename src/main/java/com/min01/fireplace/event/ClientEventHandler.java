package com.min01.fireplace.event;

import com.min01.fireplace.Fireplace;
import com.min01.fireplace.entity.FireplaceEntities;
import com.min01.fireplace.entity.model.ModelAlienFeng;
import com.min01.fireplace.entity.model.ModelEvokerFeng;
import com.min01.fireplace.entity.model.ModelFireFeng;
import com.min01.fireplace.entity.model.ModelPresent;
import com.min01.fireplace.entity.model.ModelSnowyFeng;
import com.min01.fireplace.entity.model.ModelUFO;
import com.min01.fireplace.entity.renderer.CarrotFangRenderer;
import com.min01.fireplace.entity.renderer.EvokerFengRenderer;
import com.min01.fireplace.entity.renderer.FireFengRenderer;
import com.min01.fireplace.entity.renderer.KaratFengRenderer;
import com.min01.fireplace.entity.renderer.PresentRenderer;
import com.min01.fireplace.entity.renderer.SimpleKaratFengRenderer;
import com.min01.fireplace.entity.renderer.SnowyFengRenderer;
import com.min01.fireplace.entity.renderer.UFORenderer;
import com.min01.fireplace.entity.renderer.VampireFengRenderer;
import com.min01.fireplace.item.model.ModelKingsStaff;
import com.min01.fireplace.misc.FireplaceArmPose;
import com.min01.fireplace.util.FireplaceUtil;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Fireplace.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler 
{
	public static HumanoidModel.ArmPose KING_STAFF;
	
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(ModelKingsStaff.LAYER_LOCATION, ModelKingsStaff::createBodyLayer);
    	event.registerLayerDefinition(ModelSnowyFeng.LAYER_LOCATION, ModelSnowyFeng::createBodyLayer);
    	event.registerLayerDefinition(ModelEvokerFeng.LAYER_LOCATION, ModelEvokerFeng::createBodyLayer);
    	event.registerLayerDefinition(ModelPresent.LAYER_LOCATION, ModelPresent::createBodyLayer);
    	event.registerLayerDefinition(ModelFireFeng.LAYER_LOCATION, ModelFireFeng::createBodyLayer);
    	event.registerLayerDefinition(ModelAlienFeng.LAYER_LOCATION, ModelAlienFeng::createBodyLayer);
    	event.registerLayerDefinition(ModelUFO.LAYER_LOCATION, ModelUFO::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event)
    {
    	KING_STAFF = HumanoidModel.ArmPose.create("KING_STAFF", false, new FireplaceArmPose());
    }
    
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerEntityRenderer(FireplaceEntities.KARAT_FENG.get(), KaratFengRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.CARROT_FANG.get(), CarrotFangRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.CARROT.get(), (context) -> new ThrownItemRenderer<>(context));
    	event.registerEntityRenderer(FireplaceEntities.SNOWY_FENG.get(), SnowyFengRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.EVOKER_FENG.get(), EvokerFengRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.SANTA_FENG.get(), (context) -> new SimpleKaratFengRenderer<>(context, FireplaceUtil.getKaratModel(context), FireplaceUtil.getKaratTexture("santa_feng")));
    	event.registerEntityRenderer(FireplaceEntities.VAMPIRE_FENG.get(), VampireFengRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.PRESENT.get(), PresentRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.FIRE_FENG.get(), FireFengRenderer::new);
    	event.registerEntityRenderer(FireplaceEntities.NECRO_FENG.get(), (context) -> new SimpleKaratFengRenderer<>(context, FireplaceUtil.getKaratModel(context), FireplaceUtil.getKaratTexture("necro_feng")));
    	event.registerEntityRenderer(FireplaceEntities.ALIEN_FENG.get(), (context) -> new SimpleKaratFengRenderer<>(context, new ModelAlienFeng(context.bakeLayer(ModelAlienFeng.LAYER_LOCATION)), FireplaceUtil.getKaratTexture("alien_feng")));
    	event.registerEntityRenderer(FireplaceEntities.UFO.get(), UFORenderer::new);
    }
}
