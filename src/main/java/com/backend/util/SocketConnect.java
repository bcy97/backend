package com.backend.util;

import com.backend.vo.DataPacket;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

import java.io.*;
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
    private static String userName = null;
    private static String ip = null;
    private static int port = Constants._DEFAULT_PORT_;
    private static Logger logger_ = Logger.getLogger("SocketConnect");

    private static SocketAddress getSocketAddress() {
     //   String ip = "192.168.1.104";
     //   int port = 10001;
        if(null == ip)
            loadCfg();

        return new InetSocketAddress(ip, port);
    }

    /***
     * 读配置
     * */
    private static void loadCfg(){
        File file = null;

        // 取得根目录路径
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath);
        try {
            file = ResourceUtils.getFile(rootPath + "//local.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file != null && !file.exists()) {
            logger_.error(file + " file not found!");
            return;
        }

        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(file);
        }catch (DocumentException de){
            logger_.error("加载local.xml文件出错.");
            de.printStackTrace();
        }
        Element root = doc.getRootElement();

        for(Object item : root.elements()){
            if("ServerIP".equals(((Element) item).getName()))
                ip = ((Element)item).getText();
            else if("ServerPort".equals(((Element) item).getName()))
                try {
                    port = new Integer(((Element) item).getText());
                }catch (Exception e){
                    port = Constants._DEFAULT_PORT_;
                }
            else if("userName".equals(((Element) item).getName()))
                userName = ((Element)item).getText();
        }
    }

    /***
     * 接收数据
     * */
    private static ByteBuffer receiveData(InputStream is, Logger logger) throws IOException {
        List<Byte> list = new ArrayList<>();
        DataPacket dp;
        byte[] bDatas;
        int packagCount = 0;
        int len = 0;
        try {
            while (true) {
                byte[] bHead = new byte[12];

                len = 0;
                while(len < bHead.length)
                    len += is.read(bHead,len,bHead.length - len);

                dp = new DataPacket();
                dp.toDataPacketHead(bHead);

                if (0 == dp.getLength())
                    break;

                bDatas = new byte[dp.getLength()];
                len = 0;
                while(len < bDatas.length)
                    len += is.read(bDatas,len,bDatas.length - len);

                for (byte item : bDatas)
                    list.add(item);

                bDatas = new byte[1];
                len = 0;
                while(len < bDatas.length)
                    len += is.read(bDatas,len,bDatas.length - len);

                if (0 == dp.getTailFlag())
                    break;
                packagCount++;
            }
        } catch (Exception e) {
            System.out.println("接收数据超时!" + e.getMessage() );
            logger.error("接收数据超时!" + e.getMessage() );
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
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = null;

            // 要先发一个包，告诉上位机通道类型
            bDatas = generateChannelDeclarationPackage().serialize();
            //os.write(bDatas, 0, bDatas.length);

            List<DataPacket> dps = toDataPackets(sendDatas, cmd);
            for (DataPacket dp : dps) {
                bDatas = dp.serialize();
                os.write(bDatas, 0, bDatas.length);
            }

            ByteBuffer bb = receiveData(is, logger);
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

    /***
     * 生成通道类型声明包
     * */
    private static DataPacket generateChannelDeclarationPackage(){
        if(null == userName)
            userName = "";

        byte[] strBytes = null;
        try {
            strBytes = userName.getBytes("UTF8");
        }catch (UnsupportedEncodingException ex){
            strBytes = new byte[0];
        }

        byte[] datas = new byte[strBytes.length + 2];
        datas[0] = 0x01;
        datas[1] = (byte) strBytes.length;

        if(0 != strBytes.length)
            System.arraycopy(strBytes,0,datas,2,strBytes.length);

        DataPacket dp = new DataPacket(Constants.CC_CHANNELTYPE,datas);

        return dp;
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
