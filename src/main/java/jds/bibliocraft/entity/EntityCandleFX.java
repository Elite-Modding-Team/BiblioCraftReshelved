package jds.bibliocraft.entity;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityCandleFX extends Particle
{
	 /** the scale of the flame FX */
    private float flameScale;

    public EntityCandleFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.motionX = this.motionX * 0.009999999776482582D + par8;
        this.motionY = this.motionY * 0.009999999776482582D + par10;
        this.motionZ = this.motionZ * 0.009999999776482582D + par12;
        this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.flameScale = this.particleScale;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.setParticleTextureIndex(48);
    }

    @Override
	public void renderParticle(BufferBuilder renderer, Entity entityIn, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float var8 = (this.particleAge + par2) / this.particleMaxAge;
        var8 = MathHelper.clamp(var8, 0.0F, 1.0F);
        this.particleScale = this.flameScale * 0.3F * (1.0F - var8 * var8 * 0.5F);
        super.renderParticle(renderer, entityIn, par2, par3, par4, par5, par6, par7);
    }

    @Override
	public int getBrightnessForRender(float par1)
    {
		float age = (this.particleAge + par1) / this.particleMaxAge;
		age = MathHelper.clamp(age, 0.0F, 1.0F);
		int brightness = super.getBrightnessForRender(par1);
		int blockLight = brightness & 255;
		int skyLight = brightness >> 16 & 255;
		blockLight += (int)(age * 15.0F * 16.0F);
		if (blockLight > 240)
		{
			blockLight = 240;
		}
		return blockLight | skyLight << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            //this.setDead();
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
