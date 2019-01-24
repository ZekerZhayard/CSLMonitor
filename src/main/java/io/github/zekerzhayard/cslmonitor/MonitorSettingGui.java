package io.github.zekerzhayard.cslmonitor;

import net.minecraft.client.gui.GuiScreen;

public class MonitorSettingGui extends GuiScreen {
    @Override()
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
    }

    @Override()
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        MonitorRenderer.x = mouseX;
        MonitorRenderer.y = mouseY;
    }

    @Override()
    public void onGuiClosed() {
        CSLMonitor.config.get(CSLMonitor.MODID, "x", 0).set(MonitorRenderer.x);
        CSLMonitor.config.get(CSLMonitor.MODID, "y", 0).set(MonitorRenderer.y);
        CSLMonitor.config.save();
    }
}