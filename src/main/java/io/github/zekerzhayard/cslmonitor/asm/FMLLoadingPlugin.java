package io.github.zekerzhayard.cslmonitor.asm;

import java.util.Map;

import io.github.zekerzhayard.cslmonitor.CSLMonitor;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "CSLMonitor")
@IFMLLoadingPlugin.TransformerExclusions(value = { "io.github.zekerzhayard.cslmonitor.asm." })
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    @Override()
    public String[] getASMTransformerClass() {
        return new String[] { ClassTransformer.class.getName() };
    }

    @Override()
    public String getModContainerClass() {
        return CSLMonitor.class.getName();
    }

    @Override()
    public String getSetupClass() {
        return null;
    }

    @Override()
    public void injectData(Map<String, Object> data) {

    }

    @Override()
    public String getAccessTransformerClass() {
		return null;
	}
}