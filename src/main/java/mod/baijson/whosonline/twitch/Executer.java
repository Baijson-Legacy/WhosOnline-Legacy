package mod.baijson.whosonline.twitch;

import mod.baijson.whosonline.assets.Constants;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.awt.*;
import java.net.URI;

/**
 * File created by Baijson.
 */
public class Executer extends CommandBase {

	@Override
	public String getCommandName () {
		return "whosonline";
	}

	@Override
	public String getCommandUsage ( ICommandSender sender ) {
		return "/whosonline reload, <streamer>";
	}

	@Override
	public void execute ( MinecraftServer server, ICommandSender sender, String[] args ) throws CommandException {
		if ( args.length > 0 ) {
			if ( args[ 0 ].toLowerCase ( ).equals ( "reload" ) ) {
				Listener.instance.init ( true );
			} else {
				try {
					if ( Desktop.isDesktopSupported ( ) && args.length > 0 ) {
						Desktop.getDesktop ( ).browse ( new URI ( Constants.WEB_BASEURL + "/" + args[ 0 ].toLowerCase ( ) ) );
					}
				} catch ( Exception e ) {
					e.printStackTrace ( );
				}
			}
		}
	}
}
