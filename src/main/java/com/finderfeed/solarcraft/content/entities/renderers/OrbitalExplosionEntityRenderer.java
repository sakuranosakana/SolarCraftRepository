package com.finderfeed.solarcraft.content.entities.renderers;

import com.finderfeed.solarcraft.SolarCraft;
import com.finderfeed.solarcraft.client.rendering.shaders.post_chains.PostChainPlusUltra;
import com.finderfeed.solarcraft.client.rendering.shaders.post_chains.UniformPlusPlus;
import com.finderfeed.solarcraft.content.entities.OrbitalCannonExplosionEntity;
import com.finderfeed.solarcraft.events.other_events.OBJModels;
import com.finderfeed.solarcraft.local_library.helpers.RenderingTools;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;

import java.util.List;
import java.util.Map;

public class OrbitalExplosionEntityRenderer extends EntityRenderer<OrbitalCannonExplosionEntity> {

    public static final ResourceLocation LOCATION = new ResourceLocation(SolarCraft.MOD_ID,"textures/misc/orbital_explosion.png");

    private final ResourceLocation SHADER_LOCATION = new ResourceLocation(SolarCraft.MOD_ID,"shaders/post/orbital_explosion_outline.json");


    public static PostChainPlusUltra shader;

    public OrbitalExplosionEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(OrbitalCannonExplosionEntity entity, float p_114486_, float partialTicks, PoseStack matrices, MultiBufferSource src, int light) {
        matrices.pushPose();
        int radius = entity.getEntityData().get(OrbitalCannonExplosionEntity.RADIUS)*2;
        int depth = entity.getEntityData().get(OrbitalCannonExplosionEntity.DEPTH)*2;
        //matrices.scale(radius,depth,radius);

        this.renderExplosion(entity,matrices,src);

//        this.loadShader(
//               entity,SHADER_LOCATION,new UniformPlusPlus(Map.of(
//
//                ))
//        );





        matrices.popPose();

    }


    private void renderExplosion(OrbitalCannonExplosionEntity entity,PoseStack matrices,MultiBufferSource src){
        RenderType t = RenderType.entityTranslucent(LOCATION);
        List<BakedQuad> list = Minecraft.getInstance().getModelManager().getModel(OBJModels.ORBITAL_EXPLOSION_SPHERE)
                .getQuads(null, null, RandomSource.create(), ModelData.EMPTY, t);
        VertexConsumer cons = src.getBuffer(t);
        float alpha = Math.min(entity.getTimer()/20f,1);

        for (BakedQuad a : list) {
            cons.putBulkData(matrices.last(), a,new float[]{1,1,1,1}, 1, 1, 1,
                    new int[]{
                            LightTexture.FULL_BRIGHT,
                            LightTexture.FULL_BRIGHT,
                            LightTexture.FULL_BRIGHT,
                            LightTexture.FULL_BRIGHT
                    },
                    OverlayTexture.NO_OVERLAY,false);
        }
        if (src instanceof MultiBufferSource.BufferSource source){
            source.endBatch(t);
        }
    }

    private void loadShader(Entity tile, ResourceLocation location, UniformPlusPlus uniforms){
        if (shader == null){
            try {
                shader = new PostChainPlusUltra(location,uniforms);
                shader.resize(Minecraft.getInstance().getWindow().getScreenWidth(),Minecraft.getInstance().getWindow().getScreenHeight());
                RenderingTools.addActivePostShader(tile.toString(),uniforms, shader);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("Failed to load shader in RuneEnergyPylonRenderer.java");
            }

        }else{
            RenderingTools.addActivePostShader(tile.toString(),uniforms, shader);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(OrbitalCannonExplosionEntity p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(OrbitalCannonExplosionEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }
}
