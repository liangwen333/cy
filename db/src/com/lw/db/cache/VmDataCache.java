package com.lw.db.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class  VmDataCache {
	private  static VmDataCache vmDataCache;
	
	
	public synchronized static VmDataCache getInstance(){
		if(vmDataCache==null){
			vmDataCache = new VmDataCache();
		}
		return vmDataCache;
	}
 	/**
	 * 
	 */
 private Map<Object,ConcurrentHashMap> hashMap = new HashMap();
 
 public Map getMap(Object dataClass){
	 return  hashMap.get(dataClass);
 }
 //把数据存放在内存中
 public void setMap(Object dataClass,Map dataMap){
	 ConcurrentHashMap concurrentHashMap = hashMap.get(dataClass);
	 if(concurrentHashMap==null){
		 concurrentHashMap = new ConcurrentHashMap();
		 hashMap.put(dataClass, concurrentHashMap);
	 }
	 concurrentHashMap.putAll(dataMap);
	 
 }
	
 }
