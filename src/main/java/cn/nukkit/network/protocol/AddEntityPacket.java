package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AddEntityPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_ENTITY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int type;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float yaw;
    public float pitch;
    public int modifiers;
    public EntityMetadata metadata = new EntityMetadata();
    public final Object[][] links = new Object[0][3];

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarLong(this.entityUniqueId);
        this.putEntityId(this.entityRuntimeId);
        this.putUnsignedVarInt(this.type);
        this.putVector3f(x, y, z);
        this.putVector3f(speedX, speedY, speedZ);
        this.putLFloat(this.yaw * (256f / 360f));
        this.putLFloat(this.pitch * (256f / 360f));
        this.putUnsignedVarInt(modifiers);
        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.links.length);
        for (Object[] link : links) {
            this.putEntityId((long) link[0]);
            this.putEntityId((long) link[1]);
            this.putByte((byte) link[2]);
        }
    }
}
