package com.lw.db.bean;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TempFileBean {
	public List<Map> getTempDatalist() {
		return tempDatalist;
	}
	public void setTempDatalist(List<Map> tempDatalist) {
		this.tempDatalist = tempDatalist;
	}
 
	//��ʱ����
	private List<Map>  tempDatalist ;
	//��ʱ�����ļ�
	private  File[]  tempFilelist ;
	public File[] getTempFilelist() {
		return tempFilelist;
	}
	public void setTempFilelist(File[] tempFilelist) {
		this.tempFilelist = tempFilelist;
	}

}
