package jds.bibliocraft.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.lwjgl.opengl.GL11;

public class ClipboardItemStackRenderer extends TileEntityItemStackRenderer
{
	private static ItemStack pendingHandStack;
	private IBakedModel modelToRender;
	private IBakedModel nonBuiltInModel;

	public static void renderTextForNextHand(ItemStack stack)
	{
		pendingHandStack = stack;
	}

	@Override
	public void renderByItem(ItemStack stack)
	{
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem renderItem = mc.getRenderItem();
		IBakedModel model = renderItem.getItemModelWithOverrides(stack, mc.world, mc.player);
		boolean renderText = pendingHandStack != null && ItemStack.areItemStacksEqual(pendingHandStack, stack);
		pendingHandStack = null;

		GlStateManager.pushMatrix();
		try
		{
			GlStateManager.translate(0.5D, 0.5D, 0.5D);
			renderItem.renderItem(stack, getNonBuiltInModel(model));
		}
		finally
		{
			GlStateManager.popMatrix();
		}
		if (renderText)
		{
			renderText(mc, stack);
		}
	}

	private IBakedModel getNonBuiltInModel(IBakedModel model)
	{
		if (modelToRender != model)
		{
			modelToRender = model;
			nonBuiltInModel = new BakedModelWrapper<IBakedModel>(model)
			{
				@Override
				public boolean isBuiltInRenderer()
				{
					return false;
				}
			};
		}
		return nonBuiltInModel;
	}

	private void renderText(Minecraft mc, ItemStack stack)
	{
		ClipboardTextLayout textLayout = getTextLayout(stack);
		GlStateManager.pushMatrix();
		try
		{
			GlStateManager.translate(ClipboardTextLayout.MODEL_TEXT_X, 0.0D, 0.0D);
			GlStateManager.depthFunc(GL11.GL_ALWAYS);
			GlStateManager.depthMask(false);
			for (int row = 0; row < ClipboardTextLayout.ROW_COUNT; row++)
			{
				GlStateManager.pushMatrix();
				try
				{
					String text = textLayout.getText(row);
					int textWidth = mc.fontRenderer.getStringWidth(text);
					float textScale = ClipboardTextLayout.getDisplayScale(row, textWidth);
					GlStateManager.translate(0.0D, textLayout.getModelY(row),
							textLayout.getHandModelZ(row, textWidth * (double)textScale));
					GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					float renderScale = ClipboardTextLayout.MODEL_TEXT_SCALE * textScale;
					GlStateManager.scale(renderScale, renderScale, renderScale);
					mc.fontRenderer.drawString(text, 0, 0, 0x000000, false);
				}
				finally
				{
					GlStateManager.popMatrix();
				}
			}
		}
		finally
		{
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			GlStateManager.depthMask(true);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}

	private ClipboardTextLayout getTextLayout(ItemStack stack)
	{
		NBTTagCompound clipboardTags = stack.getTagCompound();
		if (clipboardTags == null)
		{
			return ClipboardTextLayout.fromPage(null);
		}

		int currentPage = Math.max(1, clipboardTags.getInteger("currentPage"));
		return ClipboardTextLayout.fromPage(clipboardTags.getCompoundTag("page" + currentPage));
	}
}
