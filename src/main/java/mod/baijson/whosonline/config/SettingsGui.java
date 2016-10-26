package mod.baijson.whosonline.config;

import mod.baijson.whosonline.assets.Constants;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * File created by Baijson.
 */
public class SettingsGui extends GuiConfig {

	/**
	 * @param parent
	 */
	public SettingsGui ( GuiScreen parent ) {
		super ( parent, new ConfigElement ( Settings.configuration.getCategory ( Configuration.CATEGORY_GENERAL ) ).getChildElements ( ),
			  Constants.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath ( Settings.configuration.toString ( ) ) );
	}
}
