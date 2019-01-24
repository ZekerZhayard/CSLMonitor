package io.github.zekerzhayard.cslmonitor;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CSLMonitor extends DummyModContainer {
    public final static String MODID = "cslmonitor";
    public final static String NAME = "CSLMonitor";
    static Configuration config;

    public CSLMonitor() {
        super(new ModMetadata());
        ModMetadata md = this.getMetadata();
        md.authorList = Lists.newArrayList("ZekerZhayard");
        md.credits = "GPLv2";
        md.modId = CSLMonitor.MODID;
        md.name = CSLMonitor.NAME;
        md.version = "@VERSION@";
    }

    @Override()
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe()
    public void preInit(FMLPreInitializationEvent event) {
        CSLMonitor.config = new Configuration(event.getSuggestedConfigurationFile());
        CSLMonitor.config.load();
        MonitorRenderer.x = CSLMonitor.config.get(CSLMonitor.MODID, "x", 0).getInt();
        MonitorRenderer.y = CSLMonitor.config.get(CSLMonitor.MODID, "y", 0).getInt();
        CSLMonitor.config.save();
    }

    @Subscribe()
    public void init(FMLInitializationEvent event) throws IllegalAccessException {
    	ClientCommandHandler.instance.registerCommand(new MonitorCommand());
        MinecraftForge.EVENT_BUS.register(new MonitorRenderer(Minecraft.getMinecraft()));
    }
}