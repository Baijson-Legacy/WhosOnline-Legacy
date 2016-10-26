package mod.baijson.whosonline.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

/**
 * File created by Baijson.
 */
public class SettingsGuiFactory implements IModGuiFactory {

	@Override
	public void initialize ( Minecraft minecraft ) {
		//
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass () {
		return SettingsGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories () {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor ( RuntimeOptionCategoryElement element ) {
		return null;
	}
}
