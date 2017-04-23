package com.lw.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lw.db.merge.TimeMerge;

public class DbClient {
	private static DbClient dbClient;
	private Map<Object,DbDao> daoMap = new ConcurrentHashMap();
	
 	private DbClient (){
 	}
 	public static synchronized DbClient getInstance(){
		if(dbClient==null){
			dbClient = new DbClient();
			TimeMerge timeMerge = new TimeMerge();
			timeMerge.start();
		}
		return dbClient;
	}
	
 	public <T> DbDao getDao(Class<T> dataClass) throws Exception{
  		DbDao<T> dbDao = daoMap.get(dataClass);
 		if(dbDao==null){
 			 dbDao = new DbDao<T>(dataClass);
 			daoMap.put(dataClass, dbDao);
 		}
 		return dbDao;
  	}
 	
 	
 	public <T> DbDao getDao(String id,Class classTemp) throws Exception{
 		DbDao<T> dbDao = daoMap.get(id);
 		if(dbDao==null){
			dbDao = new DbDao<T>(id,classTemp);
			daoMap.put(id, dbDao);
		}
 		return dbDao;
  	}
	 

}
