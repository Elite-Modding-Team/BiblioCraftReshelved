package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import jds.bibliocraft.tileentities.TileEntityDesk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioMCBEdit implements IMessage {
    BlockPos pos;
    int currentPage;
    ItemStack book;

    public BiblioMCBEdit() {

    }

    public BiblioMCBEdit(BlockPos pos, int currentPage, ItemStack book) {
        this.pos = pos;
        this.currentPage = currentPage;
        this.book = book;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.currentPage = buf.readInt();
        this.book = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.currentPage);
        ByteBufUtils.writeItemStack(buf, this.book);
    }

    public static class Handler implements IMessageHandler<BiblioMCBEdit, IMessage> {

        @Override
        public IMessage onMessage(BiblioMCBEdit message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (message.pos == null || message.book == null || message.book.isEmpty()
                        || !Utils.hasPointLoaded(player, message.pos)
                        || player.getDistanceSq(message.pos) > 64.0D) {
                    return;
                }

                TileEntity tile = player.world.getTileEntity(message.pos);
                if (tile instanceof TileEntityClipboard
                        && message.book.getItem() == ItemClipboard.instance
                        && player.world.getBlockState(message.pos).getBlock() == BlockClipboard.instance) {
                    TileEntityClipboard clipboardTile = (TileEntityClipboard) tile;
                    if (clipboardTile.isLocked()
                            && !player.getDisplayNameString().contains(clipboardTile.getLockee())) {
                        return;
                    }

                    clipboardTile.setInventorySlotContents(0, message.book.copy());
                    clipboardTile.getNBTData();
                    clipboardTile.markDirty();
                    clipboardTile.getWorld().notifyBlockUpdate(message.pos,
                            clipboardTile.getWorld().getBlockState(message.pos),
                            clipboardTile.getWorld().getBlockState(message.pos), 3);
                    return;
                }

                if (tile instanceof TileEntityDesk) {
                    TileEntityDesk deskTile = (TileEntityDesk) tile;
                    ItemStack storedBook = deskTile.getStackInSlot(0);
                    if (!storedBook.isEmpty()
                            && ItemStack.areItemsEqual(storedBook, message.book)
                            && storedBook.getCount() == message.book.getCount()
                            && deskTile.isItemValidForSlot(0, message.book)) {
                        deskTile.overwriteWrittenBook(message.book);
                        deskTile.setCurrentPage(message.currentPage);
                    }
                }
            });
            return null;
        }

    }
}
