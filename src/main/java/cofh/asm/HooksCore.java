package cofh.asm;

import cofh.core.CoFHProps;
import cofh.core.item.IEqualityOverrideItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class HooksCore {

	// { Vanilla hooks
	public static boolean areItemsEqualHook(ItemStack held, ItemStack lastHeld) {

		if (held.getItem() != lastHeld.getItem()) {
			return false;
		}
		Item item = held.getItem();
		if (item instanceof IEqualityOverrideItem && ((IEqualityOverrideItem) item).isLastHeldItemEqual(held, lastHeld)) {
			return true;
		}
		if (held.isItemStackDamageable() && held.getItemDamage() != lastHeld.getItemDamage()) {
			return false;
		}

		return ItemStack.areItemStackTagsEqual(held, lastHeld);
	}

	@SuppressWarnings("rawtypes")
	private static List collidingBoundingBoxes = new ArrayList();

	@SuppressWarnings("rawtypes")
	public static List getEntityCollisonBoxes(World world, Entity entity, AxisAlignedBB bb) {

		if (entity instanceof EntityItem) {
	        collidingBoundingBoxes.clear();
	        int i = MathHelper.floor_double(bb.minX);
	        int j = MathHelper.floor_double(bb.maxX + 1.0D);
	        int k = MathHelper.floor_double(bb.minY);
	        int l = MathHelper.floor_double(bb.maxY + 1.0D);
	        int i1 = MathHelper.floor_double(bb.minZ);
	        int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);

	        for (int x = i; x < j; ++x) {
	            for (int z = i1; z < j1; ++z) {
	            	boolean def = x >= -30000000 & x < 30000000 & z >= -30000000 & z < 30000000;
	                if (!world.blockExists(x, 64, z)) {
	                	continue;
	                }
	                if (def)
	                	for (int y = k - 1; y < l; ++y)
	                		world.getBlock(x, y, z).addCollisionBoxesToList(world, x, y, z, bb, collidingBoundingBoxes, entity);
	                else
	                	for (int y = k - 1; y < l; ++y)
	                		Blocks.bedrock.addCollisionBoxesToList(world, x, y, z, bb, collidingBoundingBoxes, entity);
	            }
	        }

	        return collidingBoundingBoxes;
		}
		 return world.getCollidingBoundingBoxes(entity, bb);
	}

	@SideOnly(Side.CLIENT)
	public static void tickTextures(ITickable obj) {

		if (CoFHProps.enableAnimatedTexutres) {
			obj.tick();
		}
	}

	public static boolean paneConnectsTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {

		Block block = world.getBlock(x, y, z);
		return block.func_149730_j() ||
				block.getMaterial() == Material.glass ||
				block instanceof BlockPane ||
				world.isSideSolid(x, y, z, dir.getOpposite(), false);
	}
	// }

}
