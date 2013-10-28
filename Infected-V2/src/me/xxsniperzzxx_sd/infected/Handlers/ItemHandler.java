
package me.xxsniperzzxx_sd.infected.Handlers;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ItemHandler {

	public static ItemStack getItemStack(String Path) {
		ItemStack is;
		if (getItem(Path) != null)
		{
			is = new ItemStack(getItem(Path));
			is.setDurability(getItemData(Path));
		} else
			is = new ItemStack(Material.AIR);
		return is;
	}

	public static Integer getItemID(String Path) {
		String itemid = null;
		String string = Path;
		if (string.contains(":"))
		{
			String[] ss = string.split(":");
			itemid = ss[0];
		} else if (string.contains(","))
		{
			String[] ss = string.split(",");
			itemid = ss[0];
		} else if (string.contains("-"))
		{
			String[] ss = string.split("-");
			itemid = ss[0];
		} else if (string.contains("@"))
		{
			String[] ss = string.split("@");
			itemid = ss[0];
		} else if (string.contains("%"))
		{
			String[] ss = string.split("%");
			itemid = ss[0];
		} else
			itemid = string;
		int i = 0;
		try
		{
			i = Integer.valueOf(itemid);
		} catch (NumberFormatException nfe)
		{
			i = 0;
		}
		return i;
	}

	public static Short getItemData(String Path) {
		String itemdata = null;
		String string = Path;
		if (string.contains(":"))
		{
			String[] s = string.split(":");
			if (s[1].contains(","))
			{
				String[] ss = s[1].split(",");
				itemdata = ss[0];
			} else if (s[1].contains("-"))
			{
				String[] ss = s[1].split("-");
				itemdata = ss[0];
			} else if (s[1].contains("@"))
			{
				String[] ss = s[1].split("@");
				itemdata = ss[0];
			} else if (s[1].contains("%"))
			{
				String[] ss = s[1].split("%");
				itemdata = ss[0];
			} else
				itemdata = s[1];

		} else
			itemdata = "0";
		Short s = 0;
		try
		{
			s = Short.valueOf(itemdata);
		} catch (NumberFormatException nfe)
		{
			s = 0;
		}
		return s;
	}

	public static Integer getItemAmount(String Path) {
		String itemdata = null;
		String string = Path;
		if (string.contains(","))
		{
			String[] s = string.split(",");
			if (s[1].contains("-"))
			{
				String[] ss = s[1].split("-");
				itemdata = ss[0];
			} else if (s[1].contains("@"))
			{
				String[] ss = s[1].split("@");
				itemdata = ss[0];
			} else if (s[1].contains("%"))
			{
				String[] ss = s[1].split("%");
				itemdata = ss[0];
			} else
				itemdata = s[1];
		} else
			itemdata = "1";
		int i = 1;
		try
		{
			i = Integer.valueOf(itemdata);
		} catch (NumberFormatException nfe)
		{
			i = 1;
		}
		return i;
	}

	public static int getItemEnchant(String Path) {
		String itemdata = null;
		String string = Path;
		if (string.contains("-"))
		{
			String[] s = string.split("-");
			if (s[1].contains("@"))
			{
				String[] ss = s[1].split("@");
				itemdata = ss[0];
			} else if (s[1].contains("%"))
			{
				String[] ss = s[1].split("%");
				itemdata = ss[0];
			} else
			{
				itemdata = s[1];
			}
		} else
			itemdata = "0";
		int i = 1;
		try
		{
			i = Integer.valueOf(itemdata);
		} catch (NumberFormatException nfe)
		{
			i = 1;
		}
		return i;
	}

	public static int getItemEnchantLvl(String Path) {
		String itemdata = null;
		String string = Path;
		if (string.contains("@"))
		{
			String[] s = string.split("@");
			if (s[1].contains("-"))
			{
				String[] ss = s[1].split("-");
				itemdata = ss[0];
			} else if (s[1].contains("%"))
			{
				String[] ss = s[1].split("%");
				itemdata = ss[0];
			} else
			{
				itemdata = s[1];
			}
		} else
			itemdata = "1";
		int i = 1;
		try
		{
			i = Integer.valueOf(itemdata);
		} catch (NumberFormatException nfe)
		{
			i = 1;
		}
		return i;
	}

	public static String getItemName(String Path) {
		String itemName = null;
		if (Path.contains("%"))
		{
			String[] ss = Path.split("%");
			itemName = ChatColor.translateAlternateColorCodes('&', ss[1]);
		} else
		{
			itemName = null;
		}
		return itemName;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(String location) {
		ItemStack is = null;
		if (Material.getMaterial(getItemID(location)) != null)
			is = new ItemStack(Material.getMaterial(getItemID(location)),
					getItemAmount(String.valueOf(location)));
		else
			is = new ItemStack(Material.AIR);

		is.setDurability(getItemData(location));

		if (!(getItemName(location) == null))
		{
			ItemMeta im = is.getItemMeta();
			String name = getItemName(location).replaceAll("_", " ");
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			is.setItemMeta(im);
		}
		if (is.getType() != Material.AIR)
			if (location.contains("-"))
			{
				int i;
				String enchants[] = location.split("-");
				for (i = 1; i != enchants.length; i++)
				{
					if (enchants[i] != null)
					{
						enchants[i] = "-" + enchants[i];

						is.addUnsafeEnchantment(Enchantment.getById(getItemEnchant(enchants[i])), getItemEnchantLvl(enchants[i]));
					}
				}
			}
		return is;
	}

	@SuppressWarnings("deprecation")
	public static String getItemStackToString(ItemStack i) {
		String itemCode = "0";

		if (i.getTypeId() != 0 && i != null)
		{

			itemCode = String.valueOf(i.getTypeId());

			if (i.getDurability() != 0)
				itemCode = itemCode + ":" + i.getDurability();
			if (i.getAmount() != 1)
				itemCode = itemCode + "," + i.getAmount();
			for (Entry<Enchantment, Integer> ench : i.getEnchantments().entrySet())
			{
				itemCode = itemCode + "-" + ench.getKey().getId();
				if (ench.getValue() != 1)
					itemCode = itemCode + "@" + ench.getValue();
			}
			if (i.getItemMeta().getDisplayName() != null)
				itemCode = itemCode + "%" + i.getItemMeta().getDisplayName().replaceAll(" ", "_").replaceAll("§", "&");
		}
		return itemCode;
	}

}
