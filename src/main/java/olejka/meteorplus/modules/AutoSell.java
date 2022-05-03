package olejka.meteorplus.modules;

import javassist.bytecode.annotation.IntegerMemberValue;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import olejka.meteorplus.MeteorPlus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoSell extends Module {
	public AutoSell() {
		super(MeteorPlus.CATEGORY, "Auto Sell", "Auto sell items in auction.");
	}

	private final SettingGroup ASSettings = settings.createGroup("Auto Sell Settings");

	private final Setting<Item> selling_item = ASSettings.add(new ItemSetting.Builder()
		.name("Item")
		.description("Selled item.")
		.defaultValue(Items.DIAMOND)
		.build()
	);

	private final Setting<String> cost = ASSettings.add(new StringSetting.Builder()
		.name("Cost pet stack")
		.description("Item cost per stack.")
		.defaultValue("2000")
		.build()
	);

	private final Setting<String> command = ASSettings.add(new StringSetting.Builder()
		.name("Command")
		.description("Selling command.")
		.defaultValue("/ah sell {cost}")
		.build()
	);

	private final Setting<Integer> delay = ASSettings.add(new IntSetting.Builder()
		.name("Selling delay")
		.description("Selling delay.")
		.defaultValue(2000)
		.build()
	);

	private int tick = 0;
	private int slot = 0;
	private long millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	@Override
	public void onActivate() {
		millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	@EventHandler
	private void onTickEventPre(TickEvent.Pre event)
	{
		FindItemResult result = InvUtils.find(selling_item.get());
		if (result.found()) {
			if (result.count() >= 64) {
				if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= millis) {
					if (tick == 0) {
						InvUtils.move().from(result.slot()).toHotbar(8);
						mc.player.getInventory().selectedSlot = 8;
						tick++;
					}
					else if (tick == 1) {
						mc.player.sendChatMessage(command.get().replace("{cost}", cost.get()));
						millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + delay.get();
						tick = 0;
					}
				}
			} else {
				info("Not have item stack");
				toggle();
			}
		} else {
			info("Selling item not found");
			toggle();
		}
	}
}
