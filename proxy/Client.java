package com.dyn.admin.proxy;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.admin.gui.Home;
import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.server.RequestUserlistMessage;
import com.dyn.utils.PlayerLevel;
import com.rabbit.gui.RabbitGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Client implements Proxy {

	private KeyBinding adminKey;

	@Override
	public Map<String, ?> getKeyBindings() {
		Map<String, KeyBinding> keys = new HashMap();
		keys.put("admin", adminKey);
		return keys;
	}

	@Override
	public void init() {
		if (DYNServerMod.status == PlayerLevel.ADMIN) {

			MinecraftForge.EVENT_BUS.register(this);

			adminKey = new KeyBinding("key.toggle.adminui", Keyboard.KEY_M, "key.categories.toggle");

			ClientRegistry.registerKeyBinding(adminKey);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if (adminKey.isPressed()) {
			if (!Minecraft.getMinecraft().thePlayer.worldObj.isRemote) {
				PacketDispatcher.sendToServer(new RequestUserlistMessage());
			}
			RabbitGui.proxy.display(new Home());
		}
	}

	/**
	 * @see forge.reference.proxy.Proxy#renderGUI()
	 */
	@Override
	public void renderGUI() {
		// Render GUI when on call from client
	}
}