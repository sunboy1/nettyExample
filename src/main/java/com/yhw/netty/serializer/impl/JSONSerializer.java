package com.yhw.netty.serializer.impl;

import com.alibaba.fastjson.JSONObject;
import com.yhw.netty.serializer.Serializer;
import com.yhw.netty.serializer.SerializerAlogrithm;

public class JSONSerializer implements Serializer {


    @Override
    public byte getSerializerAlogrithm() {
//        JSONObject.toJSON
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSONObject.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSONObject.parseObject(bytes,clazz);
    }
}
