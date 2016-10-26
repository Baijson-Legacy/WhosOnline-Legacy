package mod.baijson.whosonline;

import mod.baijson.whosonline.assets.Constants;
import mod.baijson.whosonline.config.Settings;
import mod.baijson.whosonline.twitch.Executer;
import mod.baijson.whosonline.twitch.Listener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * File created by Baijson.
 * <p>
 * <p>
 * 'WhosOnline'
 * <p>
 * A Mod that notifies players when their favorite streamer goes online!
 * Uses the Twitch API to retrieve data, and informs the player by their own preferences.
 */
@Mod( modid = Constants.MOD_ID, name = Constants.MOD_NM, version = Constants.VERSION,
	  guiFactory = Constants.CONFGUI, clientSideOnly = true )
public class WhosOnline {

	/**
	 * Instance
	 */
	@Mod.Instance( value = Constants.MOD_ID )
	static public WhosOnline instance;

	/**
	 * @param event
	 */
	@Mod.EventHandler
	public void init ( FMLPreInitializationEvent event ) {
		Settings.init ( event );

		MinecraftForge.EVENT_BUS.register ( Settings.instance );
		MinecraftForge.EVENT_BUS.register ( Listener.instance );

		Listener.instance.init ( false );
	}

	/**
	 * @param event
	 */
	@Mod.EventHandler
	public void load ( FMLInitializationEvent event ) {
		instance = this;

	}

	/**
	 * @param event
	 */
	@Mod.EventHandler
	public void onServerStartingEvent ( FMLServerStartingEvent event ) {
		event.registerServerCommand ( new Executer ( ) );
	}
}
