package me.bebeli555.autobot.events;

import me.zero.alpine.type.Cancellable;
import net.minecraft.network.Packet;

@SuppressWarnings("rawtypes")
public class PacketPostEvent extends Cancellable {
	public Packet packet;
	
	public PacketPostEvent(Packet packet) {
		this.packet = packet;
	}
}
