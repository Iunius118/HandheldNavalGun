package iunius118.mods.handheldnavalgun.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageGunShot implements IMessage
{

    UUID gunUUID;
    int fuse;

    public MessageGunShot()
    {
    }

    public MessageGunShot(UUID gunUniqueId, int fuseTick)
    {
        this.gunUUID = gunUniqueId;
        this.fuse = fuseTick;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        long mostSigBits = buf.readLong();
        long leastSigBits = buf.readLong();
        this.gunUUID = new UUID(mostSigBits, leastSigBits);
        this.fuse = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(gunUUID.getMostSignificantBits());
        buf.writeLong(gunUUID.getLeastSignificantBits());
        buf.writeInt(this.fuse);
    }

}
