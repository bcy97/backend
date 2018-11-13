package com.backend.dao.impl;

import com.backend.dao.RealDataDao;
import com.backend.util.CfgData;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.AcValue;
import com.backend.vo.AnValue;
import com.backend.vo.StValue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RealDataDaoImpl implements RealDataDao {

    static Logger logger = Logger.getLogger("RealDataDaoImpl");

    private Utils utils;

    @Autowired
    public RealDataDaoImpl(Utils utils) {
        this.utils = utils;
    }

    /***
     * 同时获取遥测、遥信、电度实时数据，遥信id返回StValue，电度id返回AcValue，遥测id返回AnValue
     * */
    @Override
    public Object[] getRealData(Integer[] ids) {
        return parseRealData(SocketConnect.getData(utils.idArrToBytes(ids), Constants.CC_REALDATA, logger,true));
    }

    /***
     * 获取遥测实时数据
     * */
    @Override
    public AnValue[] getAnRealData(Integer[] ids) {
        Object[] datas = getRealData(ids);
        AnValue[] rtnData = new AnValue[ids.length];
        for (int i = 0; i < rtnData.length; i++) {
            if (i < datas.length && datas[i] instanceof AnValue)
                rtnData[i] = (AnValue) datas[i];
            else
                rtnData[i] = new AnValue();
        }

        return rtnData;
    }

    /***
     * 获取遥信实时数据
     * */
    @Override
    public StValue[] getStRealData(Integer[] ids) {
        Object[] datas = getRealData(ids);
        StValue[] rtnData = new StValue[ids.length];
        for (int i = 0; i < rtnData.length; i++) {
            if (i < datas.length && datas[i] instanceof StValue)
                rtnData[i] = (StValue) datas[i];
            else
                rtnData[i] = new StValue();
        }

        return rtnData;
    }

    /***
     * 获取电度实时数据
     * */
    @Override
    public AcValue[] getAcRealData(Integer[] ids) {
        Object[] datas = getRealData(ids);
        AcValue[] rtnData = new AcValue[ids.length];
        for (int i = 0; i < rtnData.length; i++) {
            if (i < datas.length && datas[i] instanceof AcValue)
                rtnData[i] = (AcValue) datas[i];
            else
                rtnData[i] = new AcValue();
        }

        return rtnData;
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
            if (Constants.CC_NOTHINGNESS == id) {
                list.add(-1);
                continue;
            }
            valid = bb.get();
            if (Constants.CC_IS_NULL == valid) {
                list.add(getNullData(id));
                continue;
            }
            list.add(parseValue(bb, id, valid));
        }
        return list.toArray();
    }

    /***
     * 解析某个id的值
     * */
    private Object parseValue(ByteBuffer bb, int id, byte valid) {
        try {
            switch (utils.getTypeInId(id)) {
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
        switch (utils.getTypeInId(id)) {
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
