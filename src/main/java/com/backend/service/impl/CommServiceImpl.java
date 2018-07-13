package com.backend.service.impl;

import com.backend.service.CommService;
import com.backend.util.Constants;
import com.backend.util.Utils;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.DataPacket;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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

/***
 * 通讯服务，提供和服务端拿数据的各个接口
 *
 * @author Lin
 * */
@Service
public class CommServiceImpl implements CommService {

    static Logger logger = Logger.getLogger("CommService");

    public CommServiceImpl() {

    }

    /***
     * 通过id获取实时的数据
     * */
    public Object[] getRealData(int[] ids) {
        byte[] datas = Utils.idArrToBytes(ids);
        DataPacket dp = new DataPacket(Constants.CC_REALDATA, datas);

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

            return parseRealData(bb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    /***
     * 解析实时数据
     * */
    private Object[] parseRealData(ByteBuffer bb) {
        List<Object> list = new ArrayList<Object>();
        int size = bb.position();
        bb.flip();

        int id = -1;
        byte valid = -1;
        while (true) {
            if (size - bb.position() < 4)
                break;
            id = bb.getInt();
            // 当id不存在时的处理
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(-1);
                continue;
            }
            valid = bb.get();
            // 当有效位为-1时，上位机不给数值的数据
            if (Constants.CC_IS_NULL == valid) {// 有效位是空
                list.add(getNullData(id));
                continue;
            }
            list.add(getData(bb, id, valid));
        }
        return list.toArray();
    }

    /***
     * 获取数据
     * */
    private Object getData(ByteBuffer bb, int id, byte valid) {
        try {
            switch (Utils.getTypeInId(id)) {
                case Constants.IDACC:
                    AcValue acV = new AcValue();
                    acV.setValue(bb.getLong());
                    acV.sethValue(bb.getInt());
                    acV.setValid(valid);
                    logger.error("电度\t单元号:" + Utils.getUnitNoInId(id) + "\t点号:"
                            + Utils.getPtNoInId(id) + "\t值:" + acV.getValue()
                            + "\t小时值:" + acV.gethValue() + "\t有效位:"
                            + acV.getValid());
                    return acV;
                case Constants.IDAN:
                    AnValue anV = new AnValue();
                    anV.setValue(bb.getFloat());
                    anV.setValid(valid);
                    logger.error("遥测\t单元号:" + Utils.getUnitNoInId(id) + "\t点号:"
                            + Utils.getPtNoInId(id) + "\t值:" + anV.getValue()
                            + "\t有效为:" + anV.getValid());
                    return anV;
                case Constants.IDST:
                    StValue stV = new StValue();
                    stV.setValid(valid);
                    stV.setValue(bb.get());
                    logger.error("遥信\t单元号:" + Utils.getUnitNoInId(id) + "\t点号:"
                            + Utils.getPtNoInId(id) + "\t值:" + stV.getValue()
                            + "\t有效为:" + stV.getValid());
                    return stV;
                default:
                    return -1;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return -1;
        }
    }

    /***
     * 获取一个空数据
     * */
    private Object getNullData(int id) {
        switch (Utils.getTypeInId(id)) {
            case Constants.IDACC:
                return new AcValue();
            case Constants.IDAN:
                return new AnValue();
            case Constants.IDST:
                return new StValue();
            default:
                return 0;
        }
    }

    /***
     * 接收数据
     * */
    private void receiveData(ByteBuffer bb, InputStream is) throws IOException {
        DataPacket dp;
        byte[] bDatas;
        while (true) {
            byte[] bHead = new byte[12];
            is.read(bHead, 0, 12);// 先读12个字节的数据头
            dp = new DataPacket();
            dp.toDataPacketHead(bHead);

            bDatas = new byte[dp.getLength()];
            is.read(bDatas, 0, bDatas.length);
            bb.put(bDatas);

            is.read(bDatas, 0, 1);// 读一个字节的包尾

            if (0 == dp.getTailFlag())
                break;
        }
    }

    /***
     * 获取socket地址
     *
     * @return
     * */
    private SocketAddress getSocketAddress() {
        String ip = "127.0.0.1";
        int port = 8888;
//        String ip = "192.168.1.119";
//        int port = 10001;

        return new InetSocketAddress(ip, port);
    }

}
