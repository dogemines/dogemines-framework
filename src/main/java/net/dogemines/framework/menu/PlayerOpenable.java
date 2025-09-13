package net.dogemines.framework.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerOpenable {
    void openInventory(@NotNull Player player);
}
