package com.instamine.mixin;

import com.instamine.Instamine;
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

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class Hardness {

    private static final float STONE_HARDNESS = 1.5f;

    @Inject(
        method = "getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private void override(Player player, BlockGetter world, BlockPos pos,
                          CallbackInfoReturnable<Float> cir) {

        BlockState state = (BlockState)(Object) this;
        Block block = state.getBlock();

        if (!Instamine.BLOCK_SET.contains(block)) return;

        float speed = player.getDestroySpeed(state);
        boolean harvest = player.hasCorrectToolForDrops(state);
        float progress = speed / STONE_HARDNESS / (harvest ? 30f : 100f);
        cir.setReturnValue(progress);
    }
}