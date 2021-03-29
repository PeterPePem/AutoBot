package me.bebeli555.autobot.mods.other;

import me.bebeli555.autobot.AutoBot;
import me.bebeli555.autobot.gui.Group;
import me.bebeli555.autobot.gui.Mode;
import me.bebeli555.autobot.gui.Setting;
import me.bebeli555.autobot.mods.bots.crystalpvpbot.Surround;
import me.bebeli555.autobot.utils.BlockUtil;
import me.bebeli555.autobot.utils.InventoryUtil;
import me.bebeli555.autobot.utils.MiningUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AutoEnderChestMiner extends AutoBot {
	public static Thread thread;
	
	public static Setting delay = new Setting(Mode.INTEGER, "Delay", 350, "Amount to wait after a place/break in milliseconds");
	
	public AutoEnderChestMiner() {
		super(Group.OTHER, "AutoEnderChestMiner", "Places and mines enderchests for obsidian");
	}
	
	@Override
	public void onEnabled() {
		thread = new Thread() {
			public void run() {
				while(thread != null && thread.equals(this)) {
					loop();
					
					AutoBot.sleep(50);
				}
			}
		};
		
		thread.start();
	}
	
	@Override
	public void onDisabled() {
		AutoBot.EVENT_BUS.unsubscribe(MiningUtil.miningUtil);
		clearStatus();
		thread = null;
	}
	
	public void loop() {
		BlockPos[] placements = new BlockPos[] {new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1),
											    new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1)};
		BlockPos availableSpot = null;
		for (BlockPos pos : placements) {
			pos = getPlayerPos().add(pos.getX(), pos.getY(), pos.getZ());
			
			if (getBlock(pos) == Blocks.ENDER_CHEST) {
				setStatus("Mining Enderchest");
				MiningUtil.mine(pos, true);
				sleep(delay.intValue());
				return;
			} else if (BlockUtil.canPlaceBlock(pos) && isSolid(pos.add(0, -1, 0))) {
				availableSpot = pos;
				break;
			}
		}

		if (!InventoryUtil.hasBlock(Blocks.ENDER_CHEST)) {
			sendMessage("You dont have any enderchests in your inventory", true);
			toggleModule();
			return;
		} else if (mc.player.posY > 255) {
			sendMessage("Cant place enderchests on the build limit", true);
			toggleModule();
			return;
		}

		if (availableSpot != null) {
			setStatus("Placing Enderchest");
			Surround.center();
			BlockUtil.placeBlock(Blocks.ENDER_CHEST, availableSpot, true);
			sleep(delay.intValue());
		} else {
			sendMessage("No spot found to place an enderchest nearby", true);
			toggleModule();
		}
	}
}
