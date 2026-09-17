package jds.bibliocraft.events;

import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.rendering.ClipboardItemStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderClipboardText
{
	@SubscribeEvent
	public void renderItem(RenderSpecificHandEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = event.getItemStack();
		if (Config.enableClipboard && mc.player != null && mc.gameSettings.thirdPersonView == 0
				&& !stack.isEmpty() && stack.getItem() == ItemClipboard.instance)
		{
			ClipboardItemStackRenderer.renderTextForNextHand(stack);
		}
	}
}
