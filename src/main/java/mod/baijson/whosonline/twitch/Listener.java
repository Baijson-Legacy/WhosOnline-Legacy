package mod.baijson.whosonline.twitch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.baijson.whosonline.client.Message;
import mod.baijson.whosonline.config.Settings;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * File created by Baijson.
 */
public class Listener {

	static public Listener instance = new Listener ( );

	static public ArrayList<String> channels = new ArrayList<String> ( );
	static public ArrayList<String> historie = new ArrayList<String> ( );

	private boolean loaded = false;
	private int counter = 0;

	/**
	 * @param inform
	 */
	public void init ( boolean inform ) {
		try {
			if ( !Settings.instance.getUsername ( ).isEmpty ( ) ) {
				JsonElement element = new Twitch ( ).load ( "/users/$channel/follows/channels" );
				JsonElement numbers = element.getAsJsonObject ( ).get ( "_total" );
				if ( numbers != null && numbers.getAsInt ( ) > 0 ) {
					loaded = false;
					instance.load ( numbers.getAsInt ( ), inform );
				} else {
					if ( inform ) {
						Message.show ( new TextComponentString ( I18n.format ( "general.inform.loaded.empty" ) ) );
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}
	}

	/**
	 * @param total
	 * @param inform
	 */
	private void load ( int total, boolean inform ) {
		try {
			// empty channels array.
			channels.clear ( );

			for ( int i = 0; i < Math.abs ( total / 25 ); i++ ) {
				//
				JsonElement element = new Twitch ( ).load ( String.format ( "%s%s%s",
					  "/users/$channel/follows/channels",
					  "?direction=DESC&limit=25&offset=",
					  Integer.toString ( 25 * i )
				) );

				JsonArray elements = element.getAsJsonObject ( ).getAsJsonArray ( "follows" );
				for ( int j = 0; j < elements.size ( ); j++ ) {
					if ( elements.get ( j ).isJsonObject ( ) ) {
						JsonObject object = elements.get ( j ).getAsJsonObject ( );
						if ( object.has ( "notifications" ) && object.get ( "notifications" ).getAsBoolean ( ) ) {
							JsonObject channel = object.get ( "channel" ).getAsJsonObject ( );
							channels.add ( channel.get ( "name" ).getAsString ( ) );
						}
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}

		// Inform the player their follower list was loaded.
		if ( inform ) {
			Message.show ( new TextComponentString ( I18n.format ( "general.inform.loaded" ) ) );
		}
		Message.debug ( String.format ( "Loaded player's follower list from Twitch. (%s)", total ) );

		loaded = true;
	}

	/**
	 *
	 */
	private void update () {
		try {
			if ( channels.size ( ) > 0 && loaded ) {
				String streamers = "";
				for ( int i = 0; i < channels.size ( ); i++ ) {
					streamers += channels.get ( i ).toLowerCase ( ) + ",";
				}

				JsonElement element = new Twitch ( ).load ( String.format ( "%s%s",
					  "/streams?channel=",
					  streamers.substring ( 0, streamers.length ( ) - 1 )
				) );

				JsonArray streams = element.getAsJsonObject ( ).getAsJsonArray ( "streams" );
				for ( int i = 0; i < streams.size ( ); i++ ) {

					JsonObject objects = streams.get ( i ).getAsJsonObject ( );
					JsonObject channel = objects.get ( "channel" ).getAsJsonObject ( );

					String streamer = channel.get ( channel.has ( "display_name" ) ? "display_name" : "name" ).getAsString ( );
					if ( !historie.contains ( streamer.toLowerCase ( ) ) ) {
						if ( instance.recent ( objects.get ( "created_at" ).getAsString ( ), Settings.instance.getInterval ( ) + 1 ) ) {
							if ( channel.get ( "game" ).getAsString ( ).toLowerCase ( ).contains ( "minecraft" ) ) {
								historie.add ( streamer.toLowerCase ( ) );
								instance.inform ( streamer );
							}
						}
					} else {
						if ( !instance.recent ( objects.get ( "created_at" ).getAsString ( ), Settings.instance.getInterval ( ) + 1 ) ) {
							historie.remove ( streamer.toLowerCase ( ) );
						}
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}
	}

	/**
	 * @param streamer
	 */
	private void inform ( String streamer ) {

		ITextComponent stream = ITextComponent.Serializer.jsonToComponent (
			  I18n.format ( "notify.message.prefix" )
		).appendSibling ( ITextComponent.Serializer.jsonToComponent (
			  I18n.format ( "notify.message.stream" ).replace ( "$streamer", streamer )
		) );

		Message.show ( stream );
	}

	/**
	 * @param compare
	 * @param range
	 *
	 * @return
	 */
	public boolean recent ( String compare, int range ) {
		DateFormat format = new SimpleDateFormat ( "yyyy-MM-dd'T'HH:mm:ssX" );
		Calendar created_dt = Calendar.getInstance ( );
		Calendar current_dt = Calendar.getInstance ( );
		try {
			created_dt.setTime ( format.parse ( compare ) );
			long difference = current_dt.getTimeInMillis ( ) - created_dt.getTimeInMillis ( );
			return difference / ( 60 * 1000 ) < range;
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}
		return false;
	}

	/**
	 * @param event
	 */
	@SubscribeEvent
	public void onServerTickEvent ( TickEvent.ClientTickEvent event ) {
		if ( Settings.instance.getEnabled ( ) && loaded ) {
			counter++;
			// 20 ticks = 1 second. 2400 ticks = 1 minute.
			if ( ( counter / 2400 ) >= Settings.instance.getInterval ( ) ) {
				instance.update ( );
				counter = 0;
			}
		} else {
			if ( counter > 0 ) counter = 0;
		}
	}
}
