package net.mcft.copy.core.network.packet;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.lang.reflect.Constructor;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.client.gui.GuiContainerBase;
import net.mcft.copy.core.container.ContainerBase;
import net.mcft.copy.core.container.ContainerRegistry;
import net.mcft.copy.core.network.AbstractPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

/** Opens a container along with its GUI on the client. */
public class PacketOpenGui extends AbstractPacket {
	
	public String containerId;
	public int windowId;
	public NBTTagCompound data;
	
	public PacketOpenGui() {  }
	public PacketOpenGui(ContainerBase container) {
		containerId = ContainerRegistry.getIdFromContainer(container);
		windowId = container.windowId;
		data = new NBTTagCompound();
		container.writeToCompound(data);
	}
	
	@Override
	public void encode(ChannelHandlerContext context, PacketBuffer buffer) throws IOException {
		buffer.writeStringToBuffer(containerId);
		buffer.writeInt(windowId);
		buffer.writeNBTTagCompoundToBuffer(data);
	}
	
	@Override
	public void decode(ChannelHandlerContext context, PacketBuffer buffer) throws IOException {
		containerId = buffer.readStringFromBuffer(128);
		windowId = buffer.readInt();
		data = buffer.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		ContainerBase container;
		GuiContainerBase gui;
		Class<? extends ContainerBase> containerClass =
				ContainerRegistry.getContainerFromId(containerId);
		try {
			container = ContainerBase.create(containerClass, player, data);
			container.windowId = windowId;
			Class<? extends GuiContainerBase> guiClass = container.getGuiClass();
			Constructor<? extends GuiContainerBase> guiConstructor =
					guiClass.getConstructor(ContainerBase.class);
			gui = guiConstructor.newInstance(container);
		} catch (Exception e) {
			copycore.getLogger().error("Failed creating GUI for '{}':", containerClass.getSimpleName());
			e.printStackTrace();
			return;
		}
		Minecraft.getMinecraft().displayGuiScreen(gui);
	}
	
}
