package com.finderfeed.solarcraft.content.entities.dungeon_ray_controller;

import com.finderfeed.solarcraft.content.entities.DungeonRay;
import com.finderfeed.solarcraft.local_library.helpers.CompoundNBTHelper;
import com.finderfeed.solarcraft.packet_handler.packet_system.FDPacketUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DungeonRayController extends Entity {

    public static boolean DEBUG = true;
    private List<DungeonRayHandler> handlers = new ArrayList<>();
    private List<UUID> rayIds = new ArrayList<>();

    public static final EntityDataAccessor<Integer> CURRENT_SELECTED_HANDLER = SynchedEntityData.defineId(DungeonRayController.class, EntityDataSerializers.INT);


    public DungeonRayController(EntityType<?> entity, Level level) {
        super(entity, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide){
            if (DEBUG){
                FDPacketUtil.sendToTrackingEntity(this,new SendHandlersToClient(this));
            } else {
                this.moveRays();
            }
        }else{

        }
    }

    private void moveRays(){
        List<DungeonRay> rays = this.getRayEntities((ServerLevel) level);
        for (int i = 0; i < handlers.size();i++){
            DungeonRay ray = rays.get(i);
            DungeonRayHandler handler = handlers.get(i);
            if (ray != null) {
                handler.tickRay(this.blockPosition(), ray);
            }
        }
    }

    public List<DungeonRayHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<DungeonRayHandler> handlers){
        this.handlers = handlers;
    }

    public List<UUID> getRayIds() {
        return rayIds;
    }

    public List<DungeonRay> getRayEntities(ServerLevel serverLevel){
        List<DungeonRay> rays = new ArrayList<>();
        for (UUID uuid : this.rayIds){
            rays.add((DungeonRay) serverLevel.getEntity(uuid));
        }
        return rays;
    }

    @Override
    public boolean save(CompoundTag tag) {
        handlersToTag(this.getHandlers(),tag);
        CompoundNBTHelper.saveUUIDList(tag, rayIds,"rays");
        return super.save(tag);
    }

    public static CompoundTag handlersToTag(List<DungeonRayHandler> handlers,CompoundTag tag){
        for (int i = 0; i < handlers.size();i++){
            var h = handlers.get(i);
            CompoundTag t = h.toTag();
            tag.put("handler"+i,t);
        }
        tag.putInt("handlersLen",handlers.size());
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.rayIds = CompoundNBTHelper.loadUUIDList(tag,"rays");
        this.handlers = handlersFromTag(tag);
        for (int i = 0; i < tag.getInt("handlersLen");i++){
            DungeonRayHandler handler = DungeonRayHandler.fromTag(tag.getCompound("handler"+i));
            this.handlers.add(handler);
        }
    }

    public static List<DungeonRayHandler> handlersFromTag(CompoundTag tag){
        List<DungeonRayHandler> handlers = new ArrayList<>();
        for (int i = 0; i < tag.getInt("handlersLen");i++){
            DungeonRayHandler handler = DungeonRayHandler.fromTag(tag.getCompound("handler"+i));
            handlers.add(handler);
        }
        return handlers;
    }

    @Nullable
    public DungeonRayHandler getCurrentSelectedHandler(){
        if (this.getCurrentSelectedHandlerId() < handlers.size()){
            return handlers.get(this.getCurrentSelectedHandlerId());
        }else{
            return null;
        }
    }

    public void removeSelectedHandler(){
        if (this.getCurrentSelectedHandlerId() < handlers.size()){
            handlers.remove(this.getCurrentSelectedHandlerId());
            this.cycleCurrentSelectedHandler();
        }
    }

    public int getCurrentSelectedHandlerId(){
        return this.entityData.get(CURRENT_SELECTED_HANDLER);
    }

    public void cycleCurrentSelectedHandler(){
        int len = this.handlers.size();
        int c = this.getCurrentSelectedHandlerId();
        this.entityData.set(CURRENT_SELECTED_HANDLER,((c + 1) % len));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CURRENT_SELECTED_HANDLER,0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }


    @Override
    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return true;
    }
}
