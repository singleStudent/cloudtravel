package com.cloudtravel.common.service.impl;

import com.cloudtravel.common.service.IHashService;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MurMurHash算法,性能高,碰撞率低
 */
@Service("MurMurHash")
public class HashServiceImpl implements IHashService {

    @Override
    public Long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;
        ByteOrder byteOrder = buf.order();
        //intel的x86系列用低位编址 . 即低位在前 , 高位在后
        //Motorola的PowerPC采用big endian方式存储数据. 即高位在前 , 低位在后
        buf.order(ByteOrder.LITTLE_ENDIAN);

        Long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);
        long k;
        while (buf.remaining() >= 8){
            k = buf.getLong();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }

        if(buf.remaining() > 0){
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h ^= m;
        }
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return h;
    }
}
