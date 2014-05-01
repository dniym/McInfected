
package me.sniperzciinema.infected.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class IconMenu implements Listener {
	
	public class OptionClickEvent {
		
		private Player		player;
		private int				position;
		private String		name;
		private boolean		close;
		private boolean		destroy;
		private ClickType	clickType;
		
		public OptionClickEvent(Player player, int position, String name, ClickType clickType)
		{
			this.player = player;
			this.position = position;
			this.name = name;
			this.close = true;
			this.destroy = false;
			this.clickType = clickType;
		}
		
		public ClickType getClickType() {
			return this.clickType;
		}
		
		public String getName() {
			return this.name;
		}
		
		public Player getPlayer() {
			return this.player;
		}
		
		public int getPosition() {
			return this.position;
		}
		
		public void setClickType(ClickType clickType) {
			this.clickType = clickType;
		}
		
		public void setWillClose(boolean close) {
			this.close = close;
		}
		
		public void setWillDestroy(boolean destroy) {
			this.destroy = destroy;
		}
		
		public boolean willClose() {
			return this.close;
		}
		
		public boolean willDestroy() {
			return this.destroy;
		}
	}
	
	public interface OptionClickEventHandler {
		
		public void onOptionClick(OptionClickEvent event);
	}
	
	private String									name;
	private int											size;
	
	private OptionClickEventHandler	handler;
	private Plugin									plugin;
	
	private String[]								optionNames;
	
	private ItemStack[]							optionIcons;
	
	public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin)
	{
		this.name = name;
		this.size = size;
		this.handler = handler;
		this.plugin = plugin;
		this.optionNames = new String[size];
		this.optionIcons = new ItemStack[size];
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void destroy() {
		HandlerList.unregisterAll(this);
		this.handler = null;
		this.plugin = null;
		this.optionNames = null;
		this.optionIcons = null;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(this.name))
		{
			event.setCancelled(true);
			int slot = event.getRawSlot();
			if ((slot >= 0) && (slot < this.size) && (this.optionNames[slot] != null))
			{
				Plugin plugin = this.plugin;
				OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot,
						this.optionNames[slot], event.getClick());
				this.handler.onOptionClick(e);
				if (e.willClose())
				{
					final Player p = (Player) event.getWhoClicked();
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						
						@Override
						public void run() {
							p.closeInventory();
						}
					}, 1);
				}
				if (e.willDestroy())
					destroy();
			}
		}
	}
	
	public void open(Player player) {
		Inventory inventory = Bukkit.createInventory(player, this.size, this.name);
		for (int i = 0; i < this.optionIcons.length; i++)
			if (this.optionIcons[i] != null)
				inventory.setItem(i, this.optionIcons[i]);
		player.openInventory(inventory);
	}
	
	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
		if (item != null)
		{
			ItemMeta im = item.getItemMeta();
			if (im != null)
			{
				if (name != null)
					im.setDisplayName(name);
				if (lore != null)
				{
					List<String> list = new ArrayList<String>();
					for (String line : lore)
						if (line.contains("<split>"))
							for (String part : line.split("<split>"))
								list.add(part);
						else
							list.add(line);
					im.setLore(list);
				}
				item.setItemMeta(im);
			}
		}
		return item;
	}
	
	public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
		this.optionNames[position] = name;
		this.optionIcons[position] = setItemNameAndLore(icon, name, info);
		return this;
	}
	
}
