package com.lw.db.file;

import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @author lenovo
 *临时数据服务
 */
public class TempData {
	public static  final String fileAfter=".temp";
	private final String classKey="classKey";
	private final String dataMapKey="dataMapKey";
	private final String typeKey="typeKey";
	private final String createTime="createTime";
	private final int type=0;
	
	private static int num=0;
    //取得临时文件的文件名 
	public String getFileName(String path){
		String tempFileName=path+"/"+System.currentTimeMillis()+getNum()+fileAfter;
		return tempFileName;
	}
	/**
	 * 
	 * @param HashMap
	 * @param classed
	 * @param type 0:新增或修改  ，1：删除
	 * @return
	 */
  public HashMap getTempDataMap(HashMap tempDataMap,Object classed,int type){
	  HashMap temppHashMap = new HashMap();
	  temppHashMap.put(classKey, classed);
	  temppHashMap.put(dataMapKey, tempDataMap);
	  temppHashMap.put(typeKey, type);
	  temppHashMap.put(createTime, new Date());
	  return temppHashMap;
  }
	
  public Date  getTime(HashMap tempDataMap){
	 return (Date) tempDataMap.get(createTime);
  }
  public Object  getClassType(HashMap tempDataMap){
	 return   tempDataMap.get(classKey);
  }
  public HashMap  getData(HashMap tempDataMap){
		 return (HashMap) tempDataMap.get(dataMapKey);
	  }
  public boolean  isDelete(HashMap tempDataMap){
	 int type =(Integer)  tempDataMap.get(typeKey);
	 if(type==1){
		 return true;
	 }else{
		 return false; 
	 }
   }
 	private synchronized int getNum(){
		num++;
		return num;
	}
	
}
