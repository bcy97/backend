package com.backend.util;

public class Constants {
	public static final String _CONF_ = "/conf.ini";
	public static final String _DIR_UNITCFG_ = "/unitCfg/";
	public static final String _EVFAULT_TXT_ = "evfault.txt";
	//
	public static final String INI_COMM = "COMM";
	// COMM CMD
	public final static byte CC_HEAD = 0x7D;
	public final static byte CC_TAIL = 0x7E;
	public final static int CC_NOTHINGNESS = 0xFFFFFF;
	public final static byte CC_IS_NULL = (byte) 0xFF;
	public final static short CC_REALDATA = 18;//0x12
	public final static short CC_HISDATA = 26;//0x1A
	public final static short CC_EVENTDATA = 90;//0x5A
	public final static short CC_STATISDATA = 100;//0x64
	public final static short CC_USERINFO = 83;//0x54
	public final static short CC_REALCALDATA = 110;// 0x6E
	public final static short CC_CHANNELTYPE = 0;// 0x00
	public final static short CC_REMOTECONTROL = 60;//0x3C
	public final static short CC_SELECT_PIPEINFO = 0x61;
	public final static short CC_SELECT_PIPEMAINTRECORD = 0x67;
	
	public static final byte CABINET_INFO_SAVE = 0x26;
	public static final byte CABINET_INFO_DEL = 0x27;
	public static final byte CABINET_INFO_QUERY = 0x28;
	public static final byte CABINET_MAINTRECORD_SAVE = 0x29;
	public static final byte CABINET_MAINTRECORD_DEL = 0x2A;
	public static final byte CABINET_MAINTRECORD_QUERY = 0x2B;

	// history data type
	public final static byte CC_HDT_HOUS_DATA = 0x03;
	public final static byte CC_HDT_5MIN_DATA = 0x02;
	public final static byte CC_HDT_ALL = 0x00;
	// event data type
	public final static byte CC_EDT_EPD = 0x10;
	public final static byte CC_EDT_ANEPD = 0x11;
	public final static byte CC_EDT_STEPD = 0x12;
	public final static byte CC_EDT_SOE = 0x20;
	// statistic data
	public final static byte CC_SDT_AN = 0x21;
	public final static byte CC_SDT_ST = 0x22;
	public final static byte CC_SDT_AC = 0x23;
	// real statistic data & real calculate data
	public final static byte CC_RDT_STA = 0x00;
	public final static byte CC_RDT_CAL = 0x0;

	public final static int DEFAULT_PORT = 10001;

	// id
	public static final byte IDAN = 1;
	public static final byte IDACC = 2;
	public static final byte IDST = 3;
	public static final byte IDVIDEO = 4;
	public static final byte IDCTRL = 5;
	public static final byte IDOTHER = 16;

	//
	public static final String _GET_NOW_VALUE_ = "1";
	public static final String _GET_NOW_STATE_ = "2";
	public static final String _LOGIN_ = "3";

	public static final int _DEFAULT_PORT_ = 10001;
	// 在配置中灯光的类型
	public static final byte LIGHT_TYPE = 15;
	
	public static final byte PIPE_STRONGCABLE = 0x00;	//供电电缆
	public static final byte PIPE_WEAKCABLE = 0x01;		//通讯电缆
	public static final byte CABINET_HV = 0x00;			//高压柜
	public static final byte CABINET_LV = 0x01;			//低压柜
}
