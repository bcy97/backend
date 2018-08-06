package com.backend.util;

import com.backend.vo.DataPacket;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketConnect {

    public static SocketAddress getSocketAddress() {
        String ip = "192.168.1.119";
        int port = 10001;

        return new InetSocketAddress(ip, port);
    }

    public static void receiveData(ByteBuffer bb, InputStream is) throws IOException {
        DataPacket dp;
        byte[] bDatas;
        while (true) {
            byte[] bHead = new byte[12];
            is.read(bHead, 0, 12);
            dp = new DataPacket();
            dp.toDataPacketHead(bHead);

            bDatas = new byte[dp.getLength()];
            is.read(bDatas, 0, bDatas.length);
            bb.put(bDatas);

            is.read(bDatas, 0, 1);

            if (0 == dp.getTailFlag())
                break;
        }
    }

    public static ByteBuffer getData(DataPacket dp, Logger logger) {

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            ByteBuffer bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

            return bb;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());

            return null;
        }
    }
}
