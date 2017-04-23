package com.lw.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.lw.db.cache.VmDataCache;
import com.lw.db.file.BakFileData;
import com.lw.db.file.PersistentFileData;
import com.lw.db.file.TempFileData;

public class DbDao<T> {
	//private Logger log = Logger.getLogger(this.getClass());
	private T t;
	private Map<Object ,T> dataMap =null;
	private PersistentFileData persistentFileData = PersistentFileData.getInstance();
	private BakFileData bakFileData = new BakFileData();
	TempFileData  tempFileData = TempFileData.getInstance();
	private Object cl;
	public  DbDao(Class classTemp) throws Exception{
		dataMap = persistentFileData.getSaveMap(classTemp,classTemp);
		this.cl=classTemp;
 	}
	public  DbDao(String id,Class classTemp) throws Exception{
		dataMap = persistentFileData.getSaveMap(id,classTemp);
		this.cl=id;
 	}
	
	
 public void put(String  key,T value) throws Exception{
	dataMap.put(key, value);
	tempFileData.saveData(cl, key,value);
	}
public void put(long  key,T value) throws Exception{
	dataMap.put(key, value);
	tempFileData.saveData(cl, key,value);
}

public void putAll(Map map,boolean isAll ) throws Exception{
	 if(isAll){
		 dataMap.clear();
		 dataMap.putAll(map);
		 persistentFileData.saveMap(cl, dataMap);
	 }else{
		 dataMap.putAll(map);
		 tempFileData.saveData(cl,map);
	 }
 	
}




public void delete(){
	persistentFileData.deleteFile(cl);
	bakFileData.deleteFile(cl);
}


public T get(String  key){
	return  dataMap.get(key);
}
public T get(long  key){
	return  dataMap.get(key);
}
public void remove(long  key) throws Exception{
	  dataMap.remove(key);
	  tempFileData.remote(cl, key);
}

public Map<Object ,T> getAll(){
	return dataMap;
}

public List getAllList(){
	Set<Object> keySet= dataMap.keySet();
	List dataList = new ArrayList();
	for(Object key:keySet){
		dataList.add(dataMap.get(key));
	}
	return dataList;
}

public List<T> query(Filter filter){
	Set keys= dataMap.keySet();
	List list = new ArrayList();
	for(Object key:keys){
		T value= dataMap.get(key);
		if(filter.filterData(value)){
			list.add(value);
		}
		
	}
 	return list;
}
}
