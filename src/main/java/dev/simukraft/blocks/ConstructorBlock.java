package dev.simukraft.blocks;

import dev.simukraft.SimUKraft;
import dev.simukraft.data.PlayerDataProvider;
import dev.simukraft.data.sided.ClientRuntime;
import dev.simukraft.entities.block.ConstructorTileEntity;
import dev.simukraft.init.ModTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstructorBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ConstructorBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(2F, 1F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }


    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ConstructorTileEntity) {
                ClientRuntime.openConstructorScreen(entity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack pStack) {
        super.setPlacedBy(level, pos, state, placer, pStack);
        if (level.isClientSide) return;
        if (!(placer instanceof Player)) return;

        BlockEntity entity = level.getBlockEntity(pos);
        ServerPlayer player = (ServerPlayer) placer;

        if (!(entity instanceof ConstructorTileEntity)) return;

        SimUKraft.LOGGER.debug(getClass().getName() + " - Setting group data for a constructor block.");
        player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(playerData -> {
            SimUKraft.LOGGER.debug("data exists... " + playerData.getGroupID());
            ConstructorTileEntity constructor = (ConstructorTileEntity) entity;
            constructor.setGroupID(playerData.getGroupID());
            constructor.setChanged();
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntities.CONSTRUCTOR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModTileEntities.CONSTRUCTOR.get() ? ConstructorTileEntity::tick : null;
    }
}
