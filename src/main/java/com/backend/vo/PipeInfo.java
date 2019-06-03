package com.backend.vo;

import com.backend.util.Constants;

import io.netty.buffer.ByteBuf;

/**
 * 管线信息
 */
public abstract class PipeInfo {
    protected Byte pipeType;                //类别
    protected String id;                    //数据库标识
    protected String ename;                    //英文名称（唯一标识）
    protected String cname;                    //中文名称
    protected String remark;                //备注

    public static PipeInfo createPipeInfo(Byte pipeType) {
        PipeInfo pipeInfo = null;

        if (pipeType != null) {
            switch (pipeType) {
                case Constants.PIPE_STRONGCABLE: {
//				pipeInfo = new StrongCableInfo();
                }
                break;
                case Constants.PIPE_WEAKCABLE:
                    break;
                default:
                    break;
            }

            if (pipeInfo != null)
                pipeInfo.setPipeType(pipeType);
        }

        return pipeInfo;
    }

    public abstract int unSerialize(ByteBuf byteBuf);

    public Byte getPipeType() {
        return pipeType;
    }

    public void setPipeType(Byte pipeType) {
        this.pipeType = pipeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
