package jds.bibliocraft.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.world.World;

import java.util.UUID;

public class AbtractSteve extends AbstractClientPlayer
{
	private boolean armorModelLookup;

	private static GameProfile gp = new GameProfile(UUID.randomUUID(), "BiblioSteve");
	
	public AbtractSteve(World world)
	{
		super(world, gp);
	}

	public void setArmorModelLookup(boolean armorModelLookup)
	{
		this.armorModelLookup = armorModelLookup;
	}

	@Override
	public boolean isInvisible()
	{
		return armorModelLookup;
	}
}
