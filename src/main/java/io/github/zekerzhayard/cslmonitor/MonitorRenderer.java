package io.github.zekerzhayard.cslmonitor;

import java.util.AbstractMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.reflect.FieldUtils;

import customskinloader.fake.FakeSkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MonitorRenderer {
    static int x = 0;
    static int y = 0;
    static boolean showBoard = true;
    static boolean showGui = false;

    private Minecraft mc;
    private ThreadPoolExecutor cslThreadPool;

    public MonitorRenderer(Minecraft mc) throws IllegalAccessException {
        this.mc = mc;
        this.cslThreadPool = (ThreadPoolExecutor) FieldUtils.readDeclaredStaticField(FakeSkinManager.class, "THREAD_POOL", true);
        this.cslThreadPool.allowCoreThreadTimeOut(true);
        this.cslThreadPool.setKeepAliveTime(1, TimeUnit.MILLISECONDS);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (MonitorRenderer.showGui) {
            MonitorRenderer.showGui = false;
            this.mc.displayGuiScreen(new MonitorSettingGui());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) throws IllegalAccessException {
        if ((this.mc.currentScreen == null || this.mc.currentScreen instanceof MonitorSettingGui) && MonitorRenderer.showBoard) {
            int baseWidth = this.mc.fontRendererObj.getStringWidth("=");
            int x = MonitorRenderer.x;
            int y = MonitorRenderer.y;
            int i = 0;
            Gui.drawRect(x - baseWidth, y - this.mc.fontRendererObj.FONT_HEIGHT, x + baseWidth * 42, y + this.mc.fontRendererObj.FONT_HEIGHT * (CSLThreadPoolMonitor.threadsList.size() + 2), 0x50000000);
            this.mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.BLUE.toString() + "Current Threads Count: " + this.cslThreadPool.getPoolSize(), x, y, 0xFFFFFF);
            for (AbstractMap.SimpleEntry<Thread, AbstractMap.SimpleEntry<Long, String>> entry : CSLThreadPoolMonitor.threadsList) {
                y += this.mc.fontRendererObj.FONT_HEIGHT;
                float time = (System.currentTimeMillis() - entry.getValue().getKey()) / 1000.0F;
                String color;
                if (time <= 2.0F) {
                    color = EnumChatFormatting.GREEN.toString();
                } else if (time <= 5.0F) {
                    color = EnumChatFormatting.YELLOW.toString();
                } else {
                    color = EnumChatFormatting.RED.toString();
                }
                this.mc.fontRendererObj.drawStringWithShadow(color + ++i, x, y, 0xFFFFFF);
                this.mc.fontRendererObj.drawStringWithShadow(color + entry.getKey().getName(), x + baseWidth * 3, y, 0xFFFFFF);
                this.mc.fontRendererObj.drawStringWithShadow(color + entry.getValue().getValue(), x + baseWidth * 23, y, 0xFFFFFF);
                this.mc.fontRendererObj.drawStringWithShadow(color + time, x + baseWidth * 36, y, 0xFFFFFF);
            }
        }
    }
}