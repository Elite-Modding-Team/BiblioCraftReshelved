package jds.bibliocraft.rendering;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityClipboard;

public class TileEntityClipboardRenderer extends TileEntityBiblioRenderer
{
	@Override
	public void render(BiblioTileEntity tileEntity, double x, double y, double z, float tick)
	{
		TileEntityClipboard tile = (TileEntityClipboard)tileEntity;
		if (tile != null)
		{
			ClipboardTextLayout textLayout = ClipboardTextLayout.fromText(tile.titletext,
					tile.button0text, tile.button1text, tile.button2text, tile.button3text,
					tile.button4text, tile.button5text, tile.button6text, tile.button7text,
					tile.button8text);
			for (int row = 0; row < ClipboardTextLayout.ROW_COUNT; row++)
			{
				String text = textLayout.getText(row);
				int textWidth = getFontRenderer().getStringWidth(text);
				float textScale = ClipboardTextLayout.getDisplayScale(row, textWidth);
				renderText(text, ClipboardTextLayout.MODEL_TEXT_X,
						textLayout.getModelY(row), textLayout.getModelZ(row,
								textWidth * (double)textScale), textScale);
			}
			String pageNum = ""+tile.currentPage;
			if (tile.currentPage > 9)
			{
				renderText(pageNum, 0.037, 0.17, 0.03);
			}
			else
			{
				renderText(pageNum, 0.037, 0.17, 0.02);
			}
		}
	}
}
