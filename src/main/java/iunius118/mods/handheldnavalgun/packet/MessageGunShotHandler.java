package iunius118.mods.handheldnavalgun.packet;

import iunius118.mods.handheldnavalgun.HandheldNavalGun;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGunShotHandler
        implements IMessageHandler<MessageGunShot, IMessage>
{

    @Override
    public IMessage onMessage(MessageGunShot message, MessageContext ctx)
    {
        synchronized (HandheldNavalGun.INSTANCE.mapShell)
        {
            HandheldNavalGun.INSTANCE.mapShell.put(message.gunUUID.toString(), message.fuse);
        }

        return null;
    }

}
