package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;


@AllArgsConstructor
public class MyEncode extends MessageToByteEncoder {
    private Serializer serializer;
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //System.out.println(o.getClass());
        if (o instanceof RPCRequest) {
            byteBuf.writeShort(MessageType.REQUEST.getCode());
        } else if (o instanceof RPCResponse) {
            byteBuf.writeShort(MessageType.RESPONSE.getCode());
        }

        byteBuf.writeShort(serializer.getType());
        byte[] serialize = serializer.serialize(o);
        byteBuf.writeInt(serialize.length);
        byteBuf.writeBytes(serialize);
    }
}
