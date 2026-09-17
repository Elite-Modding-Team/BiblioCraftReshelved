package jds.bibliocraft.rendering;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.entity.AbtractSteve;
import jds.bibliocraft.entity.ModelDummy;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class TileEntityArmorStandRenderer extends TileEntitySpecialRenderer
{
	private AbtractSteve steve;
	private World renderWorld;
	private RenderManager renderManager;
	private RenderPlayer armorRenderer;
	private ModelDummy modelDummy= new ModelDummy();
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float what)
	{
		if (tile != null && tile instanceof TileEntityArmorStand)
		{
			TileEntityArmorStand stand = (TileEntityArmorStand)tile;
			World world = getWorld();
			if (stand.getIsBottomStand() && world != null)
			{
				ensureRenderObjects(world);

				float degreeAngle = 0.0f;
				switch (stand.getAngle())
				{
					case SOUTH:
					{
						degreeAngle = 90.0f; 
						break;
					}
					case WEST:
					{
						degreeAngle = 180.0f;  
						break;
					}
					case NORTH:
					{
						degreeAngle = 270.0f;  
						break;
					}
					default:break;
				}
				steve.inventory.armorInventory.set(3, copyStack(stand.getStackInSlot(0)));
				steve.inventory.armorInventory.set(2, copyStack(stand.getStackInSlot(1)));
				steve.inventory.armorInventory.set(1, copyStack(stand.getStackInSlot(2)));
				steve.inventory.armorInventory.set(0, copyStack(stand.getStackInSlot(3)));
				double worldX = stand.getPos().getX() + 0.5D;
				double worldY = stand.getPos().getY() + 0.07D;
				double worldZ = stand.getPos().getZ() + 0.5D;
				steve.setPosition(worldX, worldY, worldZ);
				steve.lastTickPosX = steve.prevPosX = worldX;
				steve.lastTickPosY = steve.prevPosY = worldY;
				steve.lastTickPosZ = steve.prevPosZ = worldZ;
				steve.renderYawOffset = steve.prevRenderYawOffset = degreeAngle;
				steve.rotationYaw = steve.prevRotationYaw = degreeAngle;
				steve.rotationYawHead = steve.prevRotationYawHead = degreeAngle;
				steve.rotationPitch = steve.prevRotationPitch = 0.0F;
				GlStateManager.pushMatrix();
				GlStateManager.enableLighting();
		        GlStateManager.enableBlend();
				double xPos = worldX - this.rendererDispatcher.entityX;
				double yPos = worldY - this.rendererDispatcher.entityY;
				double zPos = worldZ - this.rendererDispatcher.entityZ;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				armorRenderer.doRender(steve, xPos, yPos, zPos, degreeAngle, partialTicks);
		        GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				
				// TODO: The glint effect on the armor stand doesn't work yet.
				//GlStateManager.pushMatrix();
				//GlStateManager.translate(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.06, tile.getPos().getZ() + 0.5);
				//GlStateManager.enableLighting();
				//bindTexture(CommonProxy.BLUEWOOL);
				//GlStateManager.scale(20.0, 20.0, 20.0);
				//modelDummy.renderHead();
				//modelDummy.renderChest();
				//enchant(0);
				//enchant(1);
				//enchant(2);
				//GlStateManager.popMatrix();
			}
		}
	}

	private void ensureRenderObjects(World world)
	{
		if (steve == null || renderWorld != world)
		{
			renderWorld = world;
			renderManager = Minecraft.getMinecraft().getRenderManager();
			steve = new AbtractSteve(world);
			armorRenderer = new ArmorOnlyRenderPlayer(renderManager);
		}
	}

	private ItemStack copyStack(ItemStack stack)
	{
		return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
	}

	/** Uses the normal player armor layers while suppressing the player model itself. */
	private static class ArmorOnlyRenderPlayer extends RenderPlayer
	{
		private ArmorOnlyRenderPlayer(RenderManager renderManager)
		{
			super(renderManager);
			this.layerRenderers.clear();
			this.addLayer(new ArmorOnlyLayer(this));
		}

		@Override
		protected void renderModel(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount,
                                   float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
		{
		}

		@Override
		protected boolean canRenderName(AbstractClientPlayer entity)
		{
			return false;
		}
	}

	/**
	 * Gives Forge armor integrations the same invisible-entity hint they use for
	 * selecting an armor-stand model, while leaving the selected model visible
	 * when it is actually rendered.
	 */
	private static class ArmorOnlyLayer extends LayerBipedArmor
	{
		private ArmorOnlyLayer(ArmorOnlyRenderPlayer renderer)
		{
			super(renderer);
		}

		@Override
		protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model)
		{
			if (entity instanceof AbtractSteve)
			{
				AbtractSteve fakePlayer = (AbtractSteve) entity;
				fakePlayer.setArmorModelLookup(true);
				try
				{
					return ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
				}
				finally
				{
					fakePlayer.setArmorModelLookup(false);
				}
			}
			return super.getArmorModelHook(entity, itemStack, slot, model);
		}
	}
	
	public void enchant(int armorType)
	{
		float tickModifier = Minecraft.getSystemTime() % 3000L / 3000.0F * 48.0F;
		bindTexture(CommonProxy.GLINT_PNG);
		GlStateManager.enableBlend();
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.depthMask(false);
        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.color(0.5F * 0.76F, 0.25F * 0.76F, 0.8F * 0.76F, 1.0F);
            GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            float var23 = tickModifier * (0.001F + i * 0.003F) * 20.0F;
            float var24 = 0.33333334F;
            GlStateManager.scale(var24, var24, var24);
            GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, var23, 0.0F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            switch (armorType)
            {
            case 0:{modelDummy.renderHead(); break;}
            case 1:{modelDummy.renderChest(); break;}
            case 2:{modelDummy.renderLegs(); break;}
            case 3:{modelDummy.renderFeet(); break;}
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.depthMask(true);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
	}
}
