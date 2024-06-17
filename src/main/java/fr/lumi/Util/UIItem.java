package fr.lumi.Util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIItem {
    public static class ItemBuilder {
        private String name = "ACMD";
        private final Material material;
        private final List<String> lore = new ArrayList<>();
        private int count = 1;

        public void setItem(Inventory inventory, int pos) {
            ItemStack item = new ItemStack(material, count);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(pos, item);
        }

        public ItemBuilder(Material material) {
            this.material = material;
        }

        public ItemBuilder(Material material, String name) {
            this.material = material;
            this.name = name;
        }

        public ItemBuilder count(int count) {
            this.count = count;
            return this;
        }

        public ItemBuilder lore(String... lines) {
            this.lore.addAll(Arrays.asList(lines));
            return this;
        }

        public ItemBuilder lore(List<String> lines) {
            this.lore.addAll(lines);
            return this;
        }

        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }
    }

}
