package com.finderfeed.solarforge.client.particles.screen;

import com.finderfeed.solarforge.client.rendering.rendertypes.SolarCraftRenderTypes;
import com.finderfeed.solarforge.local_library.client.particles.TextureScreenParticle;
import net.minecraft.client.particle.ParticleRenderType;

public class TestScreenParticle extends TextureScreenParticle {
    public TestScreenParticle(int lifetime, double x, double y, double xSpeed, double ySpeed, double xAcceleration, double yAcceleration, int rCol, int gCol, int bCol, int alpha) {
        super(lifetime, x, y, xSpeed, ySpeed, xAcceleration, yAcceleration, rCol, gCol, bCol, alpha);
    }

    public TestScreenParticle(int lifetime, double x, double y, double xSpeed, double ySpeed, int rCol, int gCol, int bCol, int alpha) {
        super(lifetime, x, y, xSpeed, ySpeed, rCol, gCol, bCol, alpha);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return SolarCraftRenderTypes.ParticleRenderTypes.TEST_SCREEN_RENDER_TYPE;
    }
}
