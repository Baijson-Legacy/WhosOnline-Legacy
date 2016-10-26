package mod.baijson.whosonline.twitch;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mod.baijson.whosonline.assets.Constants;
import mod.baijson.whosonline.config.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * File created by Baijson.
 */
public class Twitch {

	private JsonParser jsonParser = new JsonParser ( );

	/**
	 * @param location
	 *
	 * @return
	 */
	public JsonElement load ( String location ) {
		JsonElement element = null;
		try {
			BufferedReader reader = new BufferedReader ( new InputStreamReader (
				  new URL ( getTwitchUrl ( location ) ).openStream ( )
			) );
			element = jsonParser.parse ( reader );
			reader.close ( );
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}
		return element;
	}

	/**
	 * @param parameter
	 *
	 * @return
	 */
	private String getTwitchUrl ( String parameter ) {
		String location = parameter.replace ( "$channel", Settings.instance.getUsername ( ) );
		String clientId = ( Settings.instance.getClientID ( ).isEmpty ( ) ? Constants.API_CLIENT_ID : Settings.instance.getClientID ( ) );

		return Constants.API_BASEURL + String.format ( "%s%s%s", location, location.contains ( "?" ) ? "&client_id=" : "?client_id=", clientId );
	}
}
