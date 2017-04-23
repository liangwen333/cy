package com.lw.db.file;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author lenovo
 * 备分文件
 */
public class BakFileData {
	FileSrv fileSrv = new FileSrv();
	
	public BakFileData(){
		createParent();
	}
	
	public Map getSaveMap(Object dataClass) throws Exception{
		String fileName=getFileName(dataClass);
		Map<Object,Map> dataMap =fileSrv.readFile(fileName);
		return dataMap;
	}
	
	public void saveMap(Object dataClass,Map dataMap) throws Exception{
		String fileName=getFileName(dataClass);
		String fileNameTemp=fileName+"_1";
		File fileTemp= fileSrv.saveFile(dataMap, fileNameTemp);
		File file=new File(fileName);
		if(file.exists()){
			file.delete();
		}
		fileTemp.renameTo(file);
	 }
	
	
	public String getFileName(Object dataClass){
		if(dataClass instanceof Class){
			return  "data_bak/"+((Class)dataClass).getName()+".cl";
		}
		return (String)dataClass;
	}
	
	public void deleteFile(Object dataClass){
		String fileName=getFileName(dataClass);
		File file=new File(fileName);
		if(file.exists()){
			file.delete();
		}
 }
	
	private void createParent() {
		 
 		File filebak = new File("data_bak");
		if (!filebak.exists()) {
			filebak.mkdirs();
		}
	}
}
