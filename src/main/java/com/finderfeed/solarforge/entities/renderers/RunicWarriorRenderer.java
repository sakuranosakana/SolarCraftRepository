package com.finderfeed.solarforge.entities.renderers;

import com.finderfeed.solarforge.SolarForge;
import com.finderfeed.solarforge.entities.RunicWarrior;
import com.finderfeed.solarforge.entities.models.RunicWarriorModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RunicWarriorRenderer extends MobRenderer<RunicWarrior, RunicWarriorModel> {

    public static final ResourceLocation LOC = new ResourceLocation(SolarForge.MOD_ID,"textures/entities/runic_warrior.png");

    public RunicWarriorRenderer(EntityRendererProvider.Context ctx) {
        super(ctx,new RunicWarriorModel(ctx.bakeLayer(RunicWarriorModel.LAYER_LOCATION)) , 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(RunicWarrior p_114482_) {
        return LOC;
    }
}
