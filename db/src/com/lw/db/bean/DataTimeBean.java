package com.lw.db.bean;

import java.util.Date;
import java.util.Map;

public class DataTimeBean {
 

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Map getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map dataMap) {
		this.dataMap = dataMap;
	}
	public boolean isDel() {
		return isDel;
	}
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}
	public boolean isBatData() {
		return isBatData;
	}
	public void setBatData(boolean isBatData) {
		this.isBatData = isBatData;
	}
	
	
	private Date updateTime;
 	private Map  dataMap;
	private boolean isDel;//�Ƿ���Ϊɾ��
	private boolean isBatData;//�Ƿ��Ǵӱ��ּ��س���������
	
	


}
