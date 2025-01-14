package net.geforcemods.securitycraft.network.client;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

import net.geforcemods.securitycraft.ConfigHandler;
import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.util.ClientUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendTip
{
	public static HashMap<String, String> tipsWithLink = new HashMap<>();

	static
	{
		tipsWithLink.put("trello", "https://trello.com/b/dbCNZwx0/securitycraft");
		tipsWithLink.put("patreon", "https://www.patreon.com/Geforce");
		tipsWithLink.put("discord", "https://discord.gg/U8DvBAW");
	}

	public SendTip() {}

	public static void encode(SendTip message, PacketBuffer packet) {}

	public static SendTip decode(PacketBuffer packet)
	{
		return new SendTip();
	}

	public static void onMessage(SendTip packet, Supplier<NetworkEvent.Context> ctx)
	{
		if(!ConfigHandler.CLIENT.sayThanksMessage.get())
			return;

		String tipKey = getRandomTip();
		IFormattableTextComponent message = new StringTextComponent("[")
				.append(new StringTextComponent("SecurityCraft").mergeStyle(TextFormatting.GOLD))
				.append(new StringTextComponent("] "))
				.append(ClientUtils.localize("messages.securitycraft:thanks",
						SecurityCraft.VERSION,
						ClientUtils.localize("messages.securitycraft:tip"),
						ClientUtils.localize(tipKey)));

		if(tipsWithLink.containsKey(tipKey.split("\\.")[2]))
			message = message.append(ForgeHooks.newChatWithLinks(tipsWithLink.get(tipKey.split("\\.")[2]))); //appendSibling

		SecurityCraft.proxy.getClientPlayer().sendMessage(message, Util.DUMMY_UUID);
	}

	private static String getRandomTip()
	{
		String[] tips = {
				"messages.securitycraft:tip.scHelp",
				"messages.securitycraft:tip.trello",
				"messages.securitycraft:tip.patreon",
				"messages.securitycraft:tip.discord",
				"messages.securitycraft:tip.scserver"
		};

		return tips[new Random().nextInt(tips.length)];
	}
}
