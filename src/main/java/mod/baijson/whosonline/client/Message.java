package mod.baijson.whosonline.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * File created by Baijson.
 */
public class Message {

	/**
	 * Send specified TextComponent to player.
	 *
	 * @param message
	 */
	static public void show ( ITextComponent message ) {
		Minecraft minecraft = Minecraft.getMinecraft ( );
		EntityPlayer player = minecraft.thePlayer;

		if ( player != null ) {
			player.addChatComponentMessage ( message );
		}
	}

	/**
	 *
	 */
	static public void debug ( String message ) {
		debug ( Level.INFO, message );
	}

	/**
	 *
	 */
	static public void debug ( Level level, String message ) {
		FMLLog.log ( level, message );
	}
}
