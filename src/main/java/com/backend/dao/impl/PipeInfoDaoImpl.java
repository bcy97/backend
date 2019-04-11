package com.backend.dao.impl;


import com.backend.dao.PipeInfoDao;
import com.backend.util.Constants;
import com.backend.util.SocketConnect;
import com.backend.util.Utils;
import com.backend.vo.PipeInfo;
import com.backend.vo.PipeMaintRecord;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class PipeInfoDaoImpl implements PipeInfoDao {
    private Logger logger = Logger.getLogger("PipeInfoDaoImpl");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");
    private Utils utils;

    @Autowired
    public PipeInfoDaoImpl(Utils utils){this.utils = utils;}

	@SuppressWarnings("deprecation")
	@Override
	public List<PipeInfo> getPipeInfos(byte pipeType, List<String> enames, String companyId) throws Exception {
		ByteBuf requestBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		byte[] byteData = null;
		requestBuf.writeByte(pipeType);
		requestBuf.writeByte(0);
		if(enames == null) {
			requestBuf.writeByte(0);
		}else {
			requestBuf.writeByte(enames.size());
			for (String ename : enames) {
				byteData = ename.getBytes("UTF-8");
				requestBuf.writeByte(byteData.length);
				requestBuf.writeBytes(byteData);
			}
		}
		byteData = new byte[requestBuf.readableBytes()];
		requestBuf.readBytes(byteData);
		
		ByteBuffer buff = SocketConnect.getData(byteData,Constants.CC_SELECT_PIPEINFO,logger,true, companyId);
		buff.flip();
		
		ByteBuf responseBuf = Unpooled.wrappedBuffer(buff).order(ByteOrder.LITTLE_ENDIAN);
		
		byte type;
		int ret = -1;
		List<PipeInfo> pipeInfos = new ArrayList<>();
		while(responseBuf.readableBytes() > 0) {
			type = responseBuf.getByte(responseBuf.readerIndex());
			PipeInfo pipeInfo = PipeInfo.createPipeInfo(type);
			ret = pipeInfo.unSerialize(responseBuf);
			if(ret < 0) break;
			pipeInfos.add(pipeInfo);
		}
		
		return pipeInfos;
	}

	@SuppressWarnings("deprecation")
	@Override
	public PipeInfo getPipeInfo(byte pipeType, String ename, String companyId) throws Exception {
		ByteBuf requestBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		byte[] byteData = null;
		requestBuf.writeByte(pipeType);
		requestBuf.writeByte(0);
		requestBuf.writeByte(1);
		byteData = ename.getBytes("UTF-8");
		requestBuf.writeByte(byteData.length);
		requestBuf.writeBytes(byteData);
		byteData = new byte[requestBuf.readableBytes()];
		requestBuf.readBytes(byteData);
		
		ByteBuffer buff = SocketConnect.getData(byteData,Constants.CC_SELECT_PIPEINFO,logger,true, companyId);
		buff.flip();
		
		PipeInfo pipeInfo = null;
		if(buff.hasRemaining()) {
			ByteBuf responseBuf = Unpooled.wrappedBuffer(buff).order(ByteOrder.LITTLE_ENDIAN);
			
			byte type;
			type = responseBuf.getByte(responseBuf.readerIndex());
			pipeInfo = PipeInfo.createPipeInfo(type);
			if(pipeInfo.unSerialize(responseBuf) < 0) {
				throw new Exception("数据解析异常");
			}
		}
		
		return pipeInfo;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<PipeMaintRecord> getPipeMaintRecords(byte pipeType, String pipeId, String companyId) throws Exception {
		ByteBuf requestBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		byte[] byteData = null;
		requestBuf.writeByte(pipeType);
		requestBuf.writeByte(0);
		byteData = pipeId.getBytes("UTF-8");
		requestBuf.writeByte(byteData.length);
		requestBuf.writeBytes(byteData);
		byteData = new byte[requestBuf.readableBytes()];
		requestBuf.readBytes(byteData);
		
		ByteBuffer buff = SocketConnect.getData(byteData,Constants.CC_SELECT_PIPEMAINTRECORD,logger,true, companyId);
		buff.flip();
		
		List<PipeMaintRecord> records = null;
		if(buff.hasRemaining()) {
			ByteBuf responseBuf = Unpooled.wrappedBuffer(buff).order(ByteOrder.LITTLE_ENDIAN);
			
			int ret = -1;
			records = new ArrayList<>();
			while(responseBuf.readableBytes() > 0) {
				PipeMaintRecord record = new PipeMaintRecord();
				ret = record.unSerialize(responseBuf);
				if(ret < 0) throw new Exception("数据解析异常");
				records.add(record);
			}
		}
		
		return records;
	}
}
