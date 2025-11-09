package net.kyrptonaught.customportalapi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.ColorUtil;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class CustomPortalParticle extends PortalParticle {

    protected CustomPortalParticle(ClientWorld world, double x, double y, double z,
                                   double vx, double vy, double vz, Sprite sprite) {
        super(world, x, y, z, vx, vy, vz, sprite);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<BlockStateParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(BlockStateParticleEffect effect, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz,
                                       Random random) {
            Sprite sprite = this.spriteProvider.getSprite(random);
            CustomPortalParticle p = new CustomPortalParticle(world, x, y, z, vx, vy, vz, sprite);

            Block block = effect.getBlockState().getBlock();
            PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
            if (link != null) {
                float[] rgb = ColorUtil.getColorForBlock(link.colorID);
                p.setColor(rgb[0], rgb[1], rgb[2]);
            }
            return p;
        }
    }
}
