package com.y3r9.c47.dog.demo;

import com.y3r9.c47.dog.util.ByteUtil;
import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;
import org.msgpack.type.Value;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The class MsgPackDemo
 *
 * @version 1.0
 */
public final class MsgPackDemo {

    public static void main(String[] args) {
        MessagePack msgPack = new MessagePack();
        BufferPacker bp = msgPack.createBufferPacker();
        try {
            bp.writeArrayBegin(3);
            bp.write(1);
            bp.write(2);
            bp.write(3);
            bp.write(4);
            bp.write(5);
            byte[] bytes = bp.toByteArray();
            String str = ByteUtil.bytesToHexString(bytes);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
