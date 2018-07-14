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

    private static SocketAddress getSocketAddress() {
        String ip = "127.0.0.1";
        int port = 8888;

        return new InetSocketAddress(ip, port);
    }

    private static void receiveData(ByteBuffer bb, InputStream is) throws IOException {
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

    public static void getData(ByteBuffer bb, DataPacket dp, byte[] datas, Logger logger) {
        System.arraycopy(bb.array(), 0, datas, 0, datas.length);

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            receiveData(bb, is);
            is.close();
            os.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
