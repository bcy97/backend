package com.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.netty.buffer.ByteBuf;

public class PipeMaintRecord {
    private String id;
    private String pipeId;			//电缆id
    
    @JsonFormat(pattern="yyyy年MM月dd日")
    private Date maintDate;			//运维时间
    private String position;		//运维地点
    private String maintContent;	//运维内容
    private String maintUnit;		//运维单位
    private String auditor;			//审核人
    private String remark;			//备注

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPipeId() {
        return pipeId;
    }
    public void setPipeId(String pipeId) {
        this.pipeId = pipeId;
    }
    public Date getMaintDate() {
		return maintDate;
	}
	public void setMaintDate(Date maintDate) {
		this.maintDate = maintDate;
	}
	public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getMaintContent() {
        return maintContent;
    }
    public void setMaintContent(String maintContent) {
        this.maintContent = maintContent;
    }
    public String getMaintUnit() {
        return maintUnit;
    }
    public void setMaintUnit(String maintUnit) {
        this.maintUnit = maintUnit;
    }
    public String getAuditor() {
        return auditor;
    }
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public int unSerialize(ByteBuf byteBuf) {
		int ret = 1;
		byte n = 0x00;
		byte[] byteData = null;
		
		try {
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				id = new String(byteData,"UTF-8");
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				pipeId = new String(byteData,"UTF-8");
			}
			
			long time = byteBuf.readLong();
			if(time > 0L) {
				maintDate = new Date(time * 1000);
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				position = new String(byteData,"UTF-8");
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				maintContent = new String(byteData,"UTF-8");
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				maintUnit = new String(byteData,"UTF-8");
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				auditor = new String(byteData,"UTF-8");
			}
			
			n = byteBuf.readByte();
			if(n > 0x00) {
				byteData = new byte[n];
				byteBuf.readBytes(byteData);
				remark = new String(byteData,"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = -1;
		}
		
		return ret;
	}
}
