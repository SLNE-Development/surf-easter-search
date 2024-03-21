package dev.slne.surf.surfeastersearch;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import java.util.ResourceBundle;
import me.tinyoverflow.tolker.Tolker;
import me.tinyoverflow.tolker.repositories.MessageBag;
import me.tinyoverflow.tolker.repositories.ResourceBundleBag;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SurfEasterSearchBootstrap implements PluginBootstrap {

  @Override
  public void bootstrap(@NotNull BootstrapContext context) {
  }

  @Override
  public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
    return new SurfEasterSearch();
  }
}
