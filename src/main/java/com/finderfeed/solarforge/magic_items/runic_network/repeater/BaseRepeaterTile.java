package com.finderfeed.solarforge.magic_items.runic_network.repeater;

import com.finderfeed.solarforge.Helpers;
import com.finderfeed.solarforge.for_future_library.helpers.CompoundNBTHelper;
import com.finderfeed.solarforge.for_future_library.helpers.FinderfeedMathHelper;
import com.finderfeed.solarforge.misc_things.ParticlesList;
import com.finderfeed.solarforge.misc_things.RunicEnergy;
import com.finderfeed.solarforge.registries.blocks.BlocksRegistry;
import com.finderfeed.solarforge.registries.tile_entities.TileEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//NEVER GONNA GIVE YOU UP
public class BaseRepeaterTile extends BlockEntity {

    public static double NULL = -1000000;

    //rune energy pylon position
    private RunicEnergy.Type ENERGY_TYPE;
    private List<BlockPos> CONNECTIONS = new ArrayList<>();


    public BaseRepeaterTile( BlockPos p_155229_, BlockState p_155230_) {
        super(TileEntitiesRegistry.REPEATER.get(), p_155229_, p_155230_);
    }





    public static void tick(Level world,BlockPos pos,BlockState state,BaseRepeaterTile tile){
        Block block = world.getBlockState(pos.below()).getBlock();
        if (block == BlocksRegistry.ZETA_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.ZETA);
        }else if (block == BlocksRegistry.URBA_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.URBA);
        }else if (block == BlocksRegistry.KELDA_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.KELDA);
        }else if (block == BlocksRegistry.FIRA_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.FIRA);
        }else if (block == BlocksRegistry.ARDO_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.ARDO);
        }else if (block == BlocksRegistry.TERA_RUNE_BLOCK.get()){
            tile.setEnergyType(RunicEnergy.Type.TERA);
        }else{
            tile.setEnergyType(null);
        }

        if (!world.isClientSide && (world.getGameTime() %20 == 1) ) {
            tile.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
        if (world.isClientSide && (world.getGameTime() % 15 == 1)){
            tile.CONNECTIONS.forEach(tile::handleParticlesBetween);
        }
    }

    private void handleParticlesBetween(BlockPos pos){
        Vec3 startPos = FinderfeedMathHelper.TileEntityThings.getTileEntityCenter(this);
        Vec3 endPos = FinderfeedMathHelper.TileEntityThings.getTileEntityCenter(pos);
        Vec3 vector = endPos.subtract(startPos);
        double length = vector.length();

        for (int i = 1; i <= Math.floor(length);i++){

            double rndX = level.random.nextDouble()*0.6-0.3;
            double rndY = level.random.nextDouble()*0.6-0.3;
            double rndZ = level.random.nextDouble()*0.6-0.3;

            Vec3 basePos = startPos.add(vector.normalize().multiply(i,i,i));

            level.addParticle(ParticlesList.SMALL_SOLAR_STRIKE_PARTICLE.get(),
                    basePos.x + rndX,
                    basePos.y + rndY,
                    basePos.z + rndZ,
                    rndX*0.01,rndY*0.01,rndZ*0.01);
        }
    }


    public double getMaxRange(){
        return 25;
    }

    public List<BlockPos> getConnections(){
        return CONNECTIONS;
    }

    public void setEnergyType(RunicEnergy.Type type){
        this.ENERGY_TYPE = type;
    }

    public RunicEnergy.Type getEnergyType(){
        return ENERGY_TYPE;
    }



    public void resetRepeater(BlockPos consumer){

    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        this.CONNECTIONS = CompoundNBTHelper.getBlockPosList("connections",tag);
        super.onDataPacket(net, pkt);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        CompoundNBTHelper.writeBlockPosList("connections",CONNECTIONS,tag);
        return Helpers.createTilePacket(this,tag);
    }


    public void addConnection(BlockPos pos){
        CONNECTIONS.add(pos);
    }

    public void removeConnection(BlockPos pos){
        CONNECTIONS.remove(pos);
    }



    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-16,-16,-16),worldPosition.offset(16,16,16));
    }
}
