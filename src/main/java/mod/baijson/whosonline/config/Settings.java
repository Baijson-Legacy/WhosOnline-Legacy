package mod.baijson.whosonline.config;

import mod.baijson.whosonline.assets.Constants;
import mod.baijson.whosonline.twitch.Listener;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * File created by Baijson.
 */
public class Settings {

	static public Settings instance = new Settings ( );

	static public Configuration configuration;

	private ConfigCategory varApiCategory = new ConfigCategory ( "Twitch API Settings" );

	private boolean varEnabled = true;
	private int varInterval = 6;
	private String varUsername = "";
	private String oldUsername = "";
	private String varClientID = Constants.API_CLIENT_ID;


	/**
	 * @param event
	 */
	static public void init ( FMLPreInitializationEvent event ) {
		configuration = new Configuration ( event.getSuggestedConfigurationFile ( ), "1.0", false );
		instance.sync ( false );
	}

	/**
	 * @param load
	 */
	public void sync ( boolean load ) {
		try {
			if ( load ) {
				configuration.load ( );
			}
			instance.load ( configuration );
		} catch ( Exception e ) {
			// do nothing.
		} finally {
			if ( configuration.hasChanged ( ) ) {
				configuration.save ( );
			}
		}
	}

	/**
	 * @param settings
	 */
	public void load ( Configuration settings ) {
		Property property;

		property = settings.get ( Configuration.CATEGORY_GENERAL, "Enabled", varEnabled, "Wheter or not this Mod is enabled." );
		varEnabled = property.getBoolean ( );

		property = settings.get ( Configuration.CATEGORY_GENERAL, "Interval", varInterval, "Update interval in minutes. default=6" );
		varInterval = property.getInt ( );

		property = settings.get ( Configuration.CATEGORY_GENERAL, "Username", varUsername, "The Player's Twitch Username. Needed to pull the correct follows list." );
		varUsername = property.getString ( );

		property = settings.get ( varApiCategory.getQualifiedName ( ), "Twitch Client-ID", varClientID, "Twitch API, Client ID. - You are not required to change this field. \nChanging this will not grant you any special functionality or whatever." );
		varClientID = property.getString ( );
	}

	/**
	 * @param event
	 */
	@SubscribeEvent( priority = EventPriority.NORMAL )
	public void onConfigChangedEvent ( ConfigChangedEvent.OnConfigChangedEvent event ) {
		if ( event.getModID ( ).equals ( Constants.MOD_ID ) ) {
			instance.sync ( false );

			if ( !oldUsername.equals ( varUsername ) ) {
				Listener.instance.init ( false );
			}

			oldUsername = varUsername;
		}
	}

	/**
	 * @return
	 */
	public boolean getEnabled () {
		return varEnabled;
	}

	/**
	 * @return
	 */
	public int getInterval () {
		return varInterval > 0 ? varInterval : 6;
	}

	/**
	 * @return
	 */
	public String getUsername () {
		return varUsername;
	}

	/**
	 * @return
	 */
	public String getClientID () {
		return varClientID.isEmpty ( ) ? Constants.API_CLIENT_ID : varClientID;
	}
}
