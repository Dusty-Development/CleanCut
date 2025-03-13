package net.dustley.clean_cut.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ForgorFleshBlock extends PillarBlock {
    public static final BooleanProperty BLINKING = BooleanProperty.of("blinking");

    public ForgorFleshBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(BLINKING, false));
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack stack = player.getMainHandStack();

        if (!world.isClient) { // Ensure this runs only on the server
            if (stack.isOf(Items.SPIDER_EYE) && !state.get(BLINKING)) {
                world.setBlockState(pos, state.with(BLINKING, true));
                stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hit);
    }



    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BLINKING,AXIS);
    }
}
