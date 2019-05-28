package com.yhw.netty.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 协议包
 * 通信过程中的 Java 对象
 */
@Data
public abstract class Packet {

    /**
     * 协议版本(不序列化，反序列化)
     */
    @JSONField(deserialize = false,serialize = false)
    private  Byte version = 1;


    /**
     * 获取指令(不序列化)
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
