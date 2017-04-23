package com.lw.db.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lw.db.bean.TempFileBean;

/**
 * 文件的服务类
 * 
 * @author lenovo
 * 
 */
public class FileSrv {
	private Logger log = Logger.getLogger(this.getClass());

	public File saveFile(Map dataMap, String fileName) throws Exception {
		File dataFile = new File(fileName);
		if (dataFile.exists()) {

		}
		ObjectOutputStream objctOut = null;
		try {
			log.info("保存文件:" + fileName);
			FileOutputStream fileWriter = new FileOutputStream(dataFile);
			objctOut = new ObjectOutputStream(fileWriter);
			objctOut.writeObject(dataMap);
			objctOut.flush();
		} finally {
			if (objctOut != null) {
				objctOut.close();
			}
		}
		return dataFile;
	}
	
	
	

	public Map readFile(String fileName) throws Exception {
		File dataFile = new File(fileName);
		if(!dataFile.exists()){
			return null;
		}
		return readFile(dataFile);
	}

	public Map readFile(File dataFile) throws Exception {
		Map dataMap = null;

		if (!dataFile.exists()) {// 文件不存在从备分文件读并恢复数据文件
		// if(fileName.indexOf("_bak")>1&&this.isBak){//是备分文件才去读取
		// dataMap=this.reBakData();
		// }
		// return dataMap;
		}
		ObjectInputStream objectInputStream = null;
		try {
			log.info("读到文件:" + dataFile.getName());
			FileInputStream fileIn = new FileInputStream(dataFile);
			objectInputStream = new ObjectInputStream(fileIn);
			dataMap = (Map) objectInputStream.readObject();
			//} catch (Exception e) {
			//log.error("读取文件异常：fileName=" + dataFile.getName(), e);
			// if(foldName.indexOf("_bak")>1&&this.isBak){//是备分文件才去读取
			// dataMap=this.reBakData();
			// }
		} finally {
			if (objectInputStream != null) {
				objectInputStream.close();
				
			}
		}
		return dataMap;
	}

	/**
	 * 取得所有的监时文件
	 * 
	 * @param foldName
	 * @return
	 * @throws Exception
	 */

	public TempFileBean getTempFileMap(String foldName, String after)
			throws Exception {
		List<Map> list = new ArrayList();
		File foldFile = new File(foldName);
		File[] fileList = foldFile.listFiles();
		TempFileBean tempFileBean = new TempFileBean();
		for (File file : fileList) {
			if (file.getName().indexOf(after) > 0) {
				list.add(readFile(file));
			}
		}
		tempFileBean.setTempDatalist(list);
		tempFileBean.setTempFilelist(fileList);
		return tempFileBean;
	}

}
