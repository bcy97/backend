package com.backend.util;

import com.backend.vo.DataPacket;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class SocketConnect {

    /// 每个包数据区最大8 * 1024
    private static final int MAX_PACKET_SIZE = 8 * 1024;

    private static SocketAddress getSocketAddress() {
        String ip = "192.168.1.106";
        int port = 10001;

        return new InetSocketAddress(ip, port);
    }

    /***
     * 接收数据
     * */
    private static ByteBuffer receiveData(InputStream is, Logger logger) throws IOException {
        List<Byte> list = new ArrayList<>();
        DataPacket dp;
        byte[] bDatas;
        try {
            while (true) {
                byte[] bHead = new byte[12];
                is.read(bHead, 0, 12);
                dp = new DataPacket();
                dp.toDataPacketHead(bHead);

                if (0 == dp.getLength())
                    break;

                bDatas = new byte[dp.getLength()];
                is.read(bDatas, 0, bDatas.length);
                for (byte item : bDatas)
                    list.add(item);

                is.read(bDatas, 0, 1);

                if (0 == dp.getTailFlag())
                    break;
            }
        }catch(Exception e){
            System.out.println("接收数据超时!" + e.getMessage());
            logger.error("接收数据超时!" + e.getMessage());
        }

        ByteBuffer bb = ByteBuffer.allocate(list.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()])));

        return bb;
    }

    /***
     * 要发送的数据包
     * @param sendDatas
     *      发送请求的数据
     * @param cmd
     *      发送请求的命令
     * */
    public static ByteBuffer getData(byte[] sendDatas, short cmd, Logger logger) {

        Socket socket = new Socket();
        try {
            socket.connect(getSocketAddress());
            socket.setSoTimeout(500);
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            List<DataPacket> dps = toDataPackets(sendDatas, cmd);
            for (DataPacket dp : dps) {
                byte[] bDatas = dp.serialize();
                os.write(bDatas, 0, bDatas.length);
            }

            ByteBuffer bb = receiveData(is,logger);
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

    /****
     * 转换成数据包
     * */
    private static List<DataPacket> toDataPackets(byte[] sendDatas, short cmd) {
        byte[] temp = null;
        byte tailFlag = 0;
        List<DataPacket> dps = new ArrayList<DataPacket>();
        for (int i = 0; i <= sendDatas.length / MAX_PACKET_SIZE; i++) {
            if ((sendDatas.length / MAX_PACKET_SIZE) == i) {// 最后一个包
                temp = new byte[sendDatas.length % MAX_PACKET_SIZE];
                tailFlag = 0;
            } else {
                temp = new byte[MAX_PACKET_SIZE];
                tailFlag = 1;
            }
            System.arraycopy(sendDatas, i * MAX_PACKET_SIZE, temp, 0, temp.length);
            dps.add(new DataPacket(cmd, temp));
        }
        return dps;
    }
}
