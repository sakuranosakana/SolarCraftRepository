package com.finderfeed.solarforge.magic_items.items.solar_lexicon.structure.subcategory;

import com.finderfeed.solarforge.SolarForge;
import com.finderfeed.solarforge.for_future_library.helpers.RenderingTools;
import com.finderfeed.solarforge.magic_items.items.solar_lexicon.screen.*;
import com.finderfeed.solarforge.magic_items.items.solar_lexicon.unlockables.AncientFragment;
import com.finderfeed.solarforge.magic_items.items.solar_lexicon.unlockables.ProgressionHelper;
import com.finderfeed.solarforge.misc_things.IScrollable;
import com.finderfeed.solarforge.misc_things.Multiblock;
import com.finderfeed.solarforge.recipe_types.InfusingRecipe;
import com.finderfeed.solarforge.recipe_types.solar_smelting.SolarSmeltingRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SubCategory {

    public static final int FONT_HEIGHT = 8;
    public static final int BUTTONS_SIZE = 25;

    private List<AncientFragment> fragments = new ArrayList<>();
    private List<Button> buttonsToAdd = new ArrayList<>();

    private final SubCategoryBase base;

    private Integer sizeX;
    private Integer sizeY;

    public SubCategory(SubCategoryBase base) {
        this.base = base;
    }

    public SubCategoryBase getBase() {
        return base;
    }


    public void initAtPos(int x, int y) {
        for (int i = 0; i < fragments.size(); i++) {
            int buttonPosX = x + (i % 6) * BUTTONS_SIZE;
            int buttonPosY = y + (int)Math.floor((float) i / 6) * BUTTONS_SIZE;
            AncientFragment frag = fragments.get(i);
            AncientFragment.Type type = frag.getType();
            if (type == AncientFragment.Type.ITEM){
                if (frag.getRecipeType() == SolarForge.INFUSING_RECIPE_TYPE){
                    buttonsToAdd.add(constructInfusingRecipeButton(frag, ProgressionHelper.getInfusingRecipeForItem(frag.getItem().getItem()),buttonPosX,buttonPosY));
                }else if (frag.getRecipeType() == SolarForge.SOLAR_SMELTING){
                    buttonsToAdd.add(constructSmeltingRecipeButton(ProgressionHelper.getSolarSmeltingRecipeForItem(frag.getItem().getItem()),buttonPosX,buttonPosY));
                }
            }else if (type == AncientFragment.Type.INFORMATION){
                buttonsToAdd.add(constructInformationButton(frag.getIcon().getDefaultInstance(),buttonPosX,buttonPosY,frag));
            }else if (type == AncientFragment.Type.ITEMS){
                buttonsToAdd.add(constructInfusingRecipeButton(frag,getRecipesForItemList(frag.getStacks()),buttonPosX,buttonPosY));
            }else if (type == AncientFragment.Type.STRUCTURE){
                buttonsToAdd.add(constructStructureButton(frag.getStructure().getM(),buttonPosX,buttonPosY,frag));
            }else if (type == AncientFragment.Type.UPGRADE){
                buttonsToAdd.add(constructInfusingRecipeButton(frag,ProgressionHelper.UPGRADES_INFUSING_RECIPE_MAP.get(frag.getItem().getItem()),buttonPosX,buttonPosY));
            }
        }
    }

    public void renderAtPos(PoseStack matrices,int x, int y) {
        drawRectangle(matrices,getSize()[0],getSize()[1],new Point(x,y));
        int scrollX = 0;
        int scrollY = 0;
        if (Minecraft.getInstance().screen instanceof IScrollable scrollable) {
            scrollX = scrollable.getCurrentScrollX();
            scrollY = scrollable.getCurrentScrollY();
        }
        Gui.drawString(matrices,Minecraft.getInstance().font,base.getTranslation(),x+scrollX,y-FONT_HEIGHT+scrollY,0xffffff);
    }

    public void putAncientFragment(AncientFragment frag) {
        fragments.add(frag);
    }


    public int[] getSize() {
        if (sizeX == null) {
            int x;
            int y;

            if (fragments.size() >= 6) {
                x = 6 * BUTTONS_SIZE;
            } else {
                x = fragments.size() * BUTTONS_SIZE;
            }

            if (fragments.size() >= 6) {
                y = (int)Math.ceil((float)fragments.size() / 6) * BUTTONS_SIZE;
            } else {
                y = BUTTONS_SIZE;
            }
            this.sizeX = x;
            this.sizeY = y;
            return new int[]{x, y};
        } else {
            return new int[]{sizeX, sizeY};
        }
    }

    public List<Button> getButtonsToAdd() {
        return buttonsToAdd;
    }

    public ItemStackButton constructInfusingRecipeButton(AncientFragment fragment, List<InfusingRecipe> recipe, int x , int y){
        return new ItemStackButton(x,y,24,24,(button)->{
            Minecraft.getInstance().setScreen(new InformationScreen(fragment,new InfusingRecipeScreen(recipe)));
        },fragment.getIcon().getDefaultInstance(),1.5f,false,(button,matrices,mx,my)->{
            Minecraft.getInstance().screen.renderTooltip(matrices,fragment.getTranslation(),mx,my);
        });
    }


    public ItemStackButton constructInfusingRecipeButton(AncientFragment fragment,InfusingRecipe recipe,int x , int y){
        return new ItemStackButton(x,y,24,24,(button)->{
            Minecraft.getInstance().setScreen(new InformationScreen(fragment,new InfusingRecipeScreen(recipe)));
        },fragment.getIcon().getDefaultInstance(),1.5f,false,(button,matrices,mx,my)->{
            Minecraft.getInstance().screen.renderTooltip(matrices,fragment.getTranslation(),mx,my);
        });
    }


    public ItemStackButton constructSmeltingRecipeButton(SolarSmeltingRecipe recipe, int x , int y){
        return new ItemStackButton(x,y,24,24,(button)->{
            Minecraft.getInstance().setScreen(new SmeltingRecipeScreen(recipe));
        },recipe.output,1.5f,false,(button,matrices,mx,my)->{
            Minecraft.getInstance().screen.renderTooltip(matrices,recipe.output.getHoverName(),mx,my);
        });
    }


    public ItemStackButton constructInformationButton(ItemStack logo, int x , int y, AncientFragment fragment){
        return new ItemStackButton(x,y,24,24,(button)->{
            Minecraft.getInstance().setScreen(new InformationScreen(fragment,null));
        },logo,1.5f,false, (button,matrices,mx,my)->{
            Minecraft.getInstance().screen.renderTooltip(matrices,fragment.getTranslation(),mx,my);
        });
    }

    public ItemStackButton constructStructureButton(Multiblock structure, int x , int y, AncientFragment fragment){
        return new ItemStackButton(x,y,24,24,(button)->{
            Minecraft.getInstance().setScreen(new StructureScreen(structure));
        },structure.getMainBlock().asItem().getDefaultInstance(),1.5f,false, (button,matrices,mx,my)->{
            Minecraft.getInstance().screen.renderTooltip(matrices,fragment.getTranslation(),mx,my);
        });
    }
    private List<InfusingRecipe> getRecipesForItemList(List<ItemStack> stacks){
        List<InfusingRecipe> recipes = new ArrayList<>();
        stacks.forEach((stack)->{
            recipes.add(ProgressionHelper.INFUSING_RECIPE_MAP.get(stack.getItem()));
        });
        return recipes;
    }

    /**
     * @param matrices Just a posestack
     * @param x x size
     * @param y y size
     * @param p initial point
     */

    public static void drawRectangle(PoseStack matrices, int x, int y, Point p){
        if (Minecraft.getInstance().screen instanceof IScrollable scrollable) {
            int scrollX = scrollable.getCurrentScrollX();
            int scrollY = scrollable.getCurrentScrollY();
            RenderingTools.drawLine(matrices, p.x + scrollX, p.y + scrollY, p.x + x + scrollX, p.y + scrollY, 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x + x + scrollX, p.y + scrollY, p.x + x + scrollX, p.y + y + scrollY, 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x + scrollX, p.y + y + scrollY, p.x + x + scrollX, p.y + y + scrollY, 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x + scrollX, p.y + scrollY, p.x + scrollX, p.y + y + scrollY, 255, 255, 255);
        }else{
            RenderingTools.drawLine(matrices, p.x , p.y , p.x + x , p.y , 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x + x , p.y , p.x + x , p.y + y , 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x , p.y + y , p.x + x , p.y + y , 255, 255, 255);
            RenderingTools.drawLine(matrices, p.x , p.y, p.x , p.y + y , 255, 255, 255);
        }
    }
}
