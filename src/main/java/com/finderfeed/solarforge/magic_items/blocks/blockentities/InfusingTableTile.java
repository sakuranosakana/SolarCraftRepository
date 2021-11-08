package com.finderfeed.solarforge.magic_items.blocks.blockentities;

import com.finderfeed.solarforge.for_future_library.OwnedBlock;
import com.finderfeed.solarforge.registries.tile_entities.TileEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class InfusingTableTile extends BlockEntity implements OwnedBlock {

    private UUID owner;

    public InfusingTableTile( BlockPos p_155229_, BlockState p_155230_) {
        super(TileEntitiesRegistry.INFUSING_CRAFTING_TABLE.get(), p_155229_, p_155230_);
    }


    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
