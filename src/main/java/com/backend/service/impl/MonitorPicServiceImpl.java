package com.backend.service.impl;

import com.backend.service.MonitorPicService;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.DataPacket;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitorPicServiceImpl implements MonitorPicService {

    static Logger logger = Logger.getLogger("MonitorPicServiceImpl");

    @Override
    public Object[] getRealData(int[] ids) {
        byte[] datas = Utils.idArrToBytes(ids);
        DataPacket dp = new DataPacket(Constants.CC_REALDATA, datas);
        ByteBuffer bb = null;

        Socket socket = new Socket();
        try {
            socket.connect(SocketConnect.getSocketAddress());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            byte[] bDatas = dp.serialize();
            os.write(bDatas, 0, bDatas.length);

            bb = ByteBuffer.allocate(8 * 1024);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            SocketConnect.receiveData(bb, is);
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

    @Override
    public List<String> getPicList() {
        return null;
    }

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
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(-1);
                continue;
            }
            valid = bb.get();
            if (Constants.CC_IS_NULL == valid) {
                list.add(getNullData(id));
                continue;
            }
            list.add(getData(bb, id, valid));
        }
        return list.toArray();
    }

    private Object getData(ByteBuffer bb, int id, byte valid) {
        try {
            switch (Utils.getTypeInId(id)) {
                case Constants.IDACC:
                    AcValue acV = new AcValue();
                    if (null != new CfgData().getAcO(id)) {
                        acV.setValue(bb.getLong()
                                * new CfgData().getAcO(id).getFi());
                        acV.sethValue(bb.getInt()
                                * new CfgData().getAcO(id).getFi());
                    } else {
                        acV.setValue(bb.getLong());
                        acV.sethValue(bb.getInt());
                    }
                    acV.setValid(valid);
                    return acV;
                case Constants.IDAN:
                    AnValue anV = new AnValue();
                    anV.setValue(bb.getFloat());
                    anV.setValid(valid);
                    return anV;
                case Constants.IDST:
                    StValue stV = new StValue();
                    stV.setValid(valid);
                    stV.setValue(bb.get());
                    return stV;
                default:
                    return -1;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return -1;
        }
    }

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

}
