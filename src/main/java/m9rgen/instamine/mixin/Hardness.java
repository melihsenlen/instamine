package m9rgen.instamine.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import m9rgen.instamine.Instamine;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class Hardness {
    @Inject(
        method = "getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F",
        at = @At("HEAD"),
        cancellable = true
    )

    private void override(Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        BlockState state = (BlockState)(Object) this;
        Block block = state.getBlock();
        
        if (!Instamine.enabled) return;
        if (!Instamine.BLOCK_SET.contains(block)) return;

        float speed = player.getDestroySpeed(state);
        boolean harvest = player.hasCorrectToolForDrops(state);
        float progress = speed / Instamine.hardness / (harvest ? 30f : 100f);
        cir.setReturnValue(progress);
    }
}