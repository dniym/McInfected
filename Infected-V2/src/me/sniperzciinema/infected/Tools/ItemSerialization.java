
package me.sniperzciinema.infected.Tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.util.regex.Pattern;

import net.minecraft.server.v1_6_R3.NBTBase;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class ItemSerialization {

	public static Inventory getArmorInventory(PlayerInventory inventory) {
		ItemStack[] armor = inventory.getArmorContents();
		
		CraftInventoryCustom storage = new CraftInventoryCustom(null,
				armor.length);
		for (int i = 0; i < armor.length; i++)
			storage.setItem(i, armor[i]);
		return storage;
		
	}

	public static Inventory getContentInventory(PlayerInventory inventory) {
		ItemStack[] content = inventory.getContents();
		CraftInventoryCustom storage = new CraftInventoryCustom(null,
				content.length);
		for (int i = 0; i < content.length; i++)
			storage.setItem(i, content[i]);
		return storage;
	}

	public static String toBase64(Inventory inventory) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagList itemList = new NBTTagList();
		// Save every element in the list
		for (int i = 0; i < inventory.getSize(); i++)
		{
			NBTTagCompound outputObject = new NBTTagCompound();
			net.minecraft.server.v1_6_R3.ItemStack craft = getCraftVersion(inventory.getItem(i));
			// Convert the item stack to a NBT compound
			if (craft != null)
				craft.save(outputObject);
			itemList.add(outputObject);
		}
		// Now save the list
		NBTBase.a(itemList, dataOutput);
		// Serialize that array
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
		// return encodeBase64(outputStream.toByteArray());
	}

	public static Inventory fromBase64(String data) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				new BigInteger(data, 32).toByteArray());
		// ByteArrayInputStream inputStream = new
		// ByteArrayInputStream(decodeBase64(data));
		// NBTTagList itemList = (NBTTagList) NBTBase.b(new
		// DataInputStream(inputStream));
		NBTTagList itemList = (NBTTagList) NBTBase.a(new DataInputStream(
				inputStream));
		Inventory inventory = new CraftInventoryCustom(null, itemList.size());
		for (int i = 0; i < itemList.size(); i++)
		{
			NBTTagCompound inputObject = (NBTTagCompound) itemList.get(i);
			// IsEmpty
			if (!inputObject.isEmpty())
			{
				inventory.setItem(i, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R3.ItemStack.createStack(inputObject)));
			}
		}
		// Serialize that array
		return inventory;
	}

	private static net.minecraft.server.v1_6_R3.ItemStack getCraftVersion(ItemStack stack) {
		if (stack != null)
			return CraftItemStack.asNMSCopy(stack);
		return null;
	}
	
	public static String getServerVersion() {

        /* compile a simple pattern to match any kind of server version, with or without the safe-guard */
        Pattern brand = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
        String version = null;

        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String version0 = pkg.substring(pkg.lastIndexOf('.') + 1);

        /* if the pattern does not match, it means that the server does not have the save-guard (libigot or old versions) */
        if (!brand.matcher(version0).matches()) {
            version0 = "";
        }

        version = version0;

        return !"".equals(version) ? version + "." : "";
    }
}