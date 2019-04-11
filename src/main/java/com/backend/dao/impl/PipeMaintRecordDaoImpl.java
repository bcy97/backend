package com.backend.dao.impl;

import com.backend.dao.PipeMaintRecordDao;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.PipeMaintRecord;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class PipeMaintRecordDaoImpl implements PipeMaintRecordDao {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");
    private Logger logger = Logger.getLogger("PipeMaintRecordDaoImpl");
    private Utils utils;

    public PipeMaintRecordDaoImpl(Utils utils){this.utils = utils;}

    @Override
    public PipeMaintRecord[] getPipeMaintRecords(String id, String pipeId, String companyId) {
        try {
            return parsePipeMaintRecord(reqPipeMaintRecord(id, pipeId, companyId));
        }catch (Exception e){
            return new PipeMaintRecord[0];
        }
    }

    /***
     * 请求管网运维记录
     * */
    private ByteBuffer reqPipeMaintRecord(String id,String pipeId, String companyId) throws UnsupportedEncodingException {
        byte[] temp = null;
        ByteBuffer bb = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);

        bb.put(utils.toByte(id));
        bb.put(utils.toByte(pipeId));

        temp = new byte[bb.position()];
        System.arraycopy(bb.array(),0,temp,0,temp.length);

        return SocketConnect.getData(temp, Constants.CC_SELECT_PIPEINFO, logger,true, companyId);
    }

    /***
     * 解析数据
     * */
    private PipeMaintRecord[] parsePipeMaintRecord(ByteBuffer bb) {
        if (null == bb || 0 == bb.position())
            return new PipeMaintRecord[0];

        bb.flip();
        int position = 0;
        byte length = 0;
        long time = 0;
        Calendar cal;
        PipeMaintRecord pipeMaintRecord;
        List<PipeMaintRecord> list = new ArrayList<PipeMaintRecord>();

        try {
            while (position < bb.position()) {
                pipeMaintRecord = new PipeMaintRecord();
                // id
                length = bb.get();
                pipeMaintRecord.setId(utils.getString(bb, length));
                position += length + 1;
                // 电缆id
                length = bb.get();
                pipeMaintRecord.setPipeId(utils.getString(bb, length));
                position += length + 1;
                // 运维时间
                time = bb.getLong();
                position += 8;
                cal = Calendar.getInstance();
                cal.setTimeInMillis(time * 1000);
                pipeMaintRecord.setMaintDate(cal.getTime());
                // 运维地点
                length = bb.get();
                pipeMaintRecord.setPosition(utils.getString(bb, length));
                position += length + 1;
                // 运维内容
                length = bb.get();
                pipeMaintRecord.setMaintContent(utils.getString(bb, length));
                position += length + 1;
                // 运维单位
                length = bb.get();
                pipeMaintRecord.setMaintUnit(utils.getString(bb, length));
                position += length + 1;
                // 审核人
                length = bb.get();
                pipeMaintRecord.setAuditor(utils.getString(bb, length));
                position += length + 1;
                // 备注
                length = bb.get();
                pipeMaintRecord.setRemark(utils.getString(bb, length));
                position += length + 1;

                list.add(pipeMaintRecord);
            }
        } catch (Exception e) {
        }

        return list.toArray(new PipeMaintRecord[list.size()]);
    }


}
