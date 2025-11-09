package net.kyrptonaught.customportalapi.mixin;

import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityPortalDetectionMixin {
    @Shadow
    private World world;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract boolean canUsePortals(boolean ignoreCreative);

    @Inject(method = "tick", at = @At("HEAD"))
    private void checkCustomPortalCollision(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        // Only check server-side players
        if (!(entity instanceof ServerPlayerEntity)) return;
        if (!(this.world instanceof ServerWorld serverWorld)) return;
        
        BlockPos pos = this.getBlockPos();
        BlockState state = serverWorld.getBlockState(pos);
        
        // Only proceed if standing on custom portal and can use portals
        if (state.getBlock() instanceof CustomPortalBlock && this.canUsePortals(false)) {
            entity.tryUsePortal((CustomPortalBlock) state.getBlock(), pos);
        }
    }
}
