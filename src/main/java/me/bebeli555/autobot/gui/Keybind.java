package me.bebeli555.autobot.gui;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import me.bebeli555.autobot.AutoBot;
import me.bebeli555.autobot.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class Keybind extends AutoBot {
	public static HashMap<String, String> keyBinds = new HashMap<String, String>();
	
	@SubscribeEvent
	public void onKeyPress(KeyInputEvent e) {
		if (!Keyboard.isKeyDown(Keyboard.getEventKey())) {
			return;
		}
		
		String id = keyBinds.get(Keyboard.getKeyName(Keyboard.getEventKey()));
		if (id != null) {
			if (!AutoBot.initDone) {
				sendMessage("Initialization isnt done yet. Try again", true);
				return;
			}
			
			if (id.equals("Keybind")) {
				Commands.openGui = true;
				MinecraftForge.EVENT_BUS.register(Gui.gui);
			} else {
				GuiNode node = Settings.getGuiNodeFromId(id.replace("Keybind", ""));
				node.click();	
			}
		}
	}
	
	//Sets the hashmap of keybinds so checking them will take less resources than looping all the nodes
	public static void setKeybinds() {
		keyBinds.clear();
		
		for (GuiNode node : GuiNode.all) {
			if (node.isKeybind) {
				if (!node.stringValue.isEmpty()) {
					keyBinds.put(node.stringValue, node.id);
				}
			}
		}
	}
}
