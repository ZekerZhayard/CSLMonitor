package io.github.zekerzhayard.cslmonitor;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class MonitorCommand extends CommandBase {
    @Override()
    public String getCommandName() {
        return "cslmonitor";
    }

    @Override()
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    @Override()
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            if (args[0].equalsIgnoreCase("show")) {
                MonitorRenderer.showBoard = !MonitorRenderer.showBoard;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            MonitorRenderer.showGui = true;
        }
	}
    
    @Override()
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}