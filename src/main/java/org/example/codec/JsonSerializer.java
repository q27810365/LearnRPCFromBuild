package org.example.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.common.RPCRequest;
import org.example.common.RPCResponse;

/*
    JSON序列化
    序列化不包含对象类的信息
 */
public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType) {
            case 0:
                RPCRequest rpcRequest = JSON.parseObject(bytes, RPCRequest.class);

                if (rpcRequest.getParams() == null) return rpcRequest;

                Object[] objects = new Object[rpcRequest.getParams().length];
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramsType = rpcRequest.getParamsTypes()[i];
                    if (!paramsType.isAssignableFrom(rpcRequest.getParams()[i].getClass())) {
                        objects[i] = JSONObject.toJavaObject((JSONObject) rpcRequest.getParams()[i], rpcRequest.getParamsTypes()[i]);
                    } else {
                        objects[i] = rpcRequest.getParams()[i];
                    }
                }
                rpcRequest.setParams(objects);
                obj = rpcRequest;
                break;
            case 1:
                RPCResponse rpcResponse = JSON.parseObject(bytes, RPCResponse.class);
                Class<?> dataType = rpcResponse.getDataType();
                if (!dataType.isAssignableFrom(rpcResponse.getData().getClass())) {
                    rpcResponse.setData(JSONObject.toJavaObject((JSONObject) rpcResponse.getData(), dataType));
                }
                obj = rpcResponse;
                break;
            default:
                System.out.println("不支持此种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
