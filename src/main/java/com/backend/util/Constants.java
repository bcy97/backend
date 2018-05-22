package com.backend.util;

public class Constants {
    public static final String _CONF_ = "/conf.ini";
    public static final String _DIR_UNITCFG_ = "/unitCfg/";
    // ini中的节点名
    public static final String INI_COMM = "COMM";
    // 通讯命令---COMM CMD
    public final static byte CC_HEAD = 0x7D;
    public final static byte CC_TAIL = 0x7E;
    public final static short CC_REALDATA = 18;
    public final static int CC_NOTHINGNESS = 0xFFFFFF;
    public final static byte CC_IS_NULL = (byte) 0xFF;

    // 缺省的端口
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
}
