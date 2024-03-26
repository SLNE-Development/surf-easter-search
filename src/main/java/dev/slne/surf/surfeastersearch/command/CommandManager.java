package dev.slne.surf.surfeastersearch.command;

public class CommandManager {

  public static final CommandManager INSTANCE = new CommandManager();

  private CommandManager() {
  }

  public void registerCommands() {
    new EasterCommand().register();
  }
}
