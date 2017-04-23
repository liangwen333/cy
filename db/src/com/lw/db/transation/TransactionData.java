package com.lw.db.transation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
 

import com.cy.invoke.proxy.ProxyConfig;
 
 

/**
 * 
* @Description: 
* @author liangwen 
* @date 2016-2-2 ����4:08:25 
*
*/
 
public class TransactionData {
 	private static TransactionData transactionData = new TransactionData();
 	public static Logger log = Logger.getLogger(TransactionData.class);
    private static Map<Long ,Map> threadDataMap= new ConcurrentHashMap();//�������Ӻ��޸ĵ�����
    private static Map<Long ,Map> threadRemoveDataMap= new ConcurrentHashMap();//����ɾ�� ��ID
   
      public static TransactionData getInstance(){
    	return transactionData;
    }
    /**
     * 
     * @param fileName  �������ļ�
     * @param data     
     * @return true : ʹ��������ж����ύ
     */
     public boolean put(Class classTemp,Map data){
    	 if(ProxyConfig.getProxyConfig().getTransation()){
    		 Map<Class ,Map> threadData= getThreadDataMap();
    		 Map mapdate= threadData.get(classTemp);
    		 if(mapdate==null){
    			 mapdate = data;
    		 }else{
    			 mapdate.putAll(data);
    		 }
    	     threadData.put(classTemp, mapdate); 
    	    
    	     return true;
    	 }
    	 return false;
    }
     
 
/**
 * ������Ҫɾ��������ID
 * @param fileName
 * @param id
 * @param jsonStorage
 * @return
 */
   public boolean remove(Class classTemp,Long id){
   	if(ProxyConfig.getProxyConfig().getTransation()){
   		Map<Class ,List> threadData= getThreadRemoveDataMap();
       	List dataList = threadData.get(classTemp);
       	if(dataList==null){
       		dataList= new ArrayList();
       		threadData.put(classTemp, dataList);
       	}
       	dataList.add(id);
       	return true;
   	}
   	return false;
    }
     
  
        
   public void remoteThreadData(){
	   Thread thread=Thread.currentThread();
	   long threadId=thread.getId();
	   threadDataMap.remove(threadId);
	   threadRemoveDataMap.remove(threadId);
    }
   
  
 
   
    
     /**
      * ȡ�ñ���Ҫ����������±��������
      * @return  key:�ļ�����VAlue���ļ�����
      */
  public  Map<Class ,Map> getThreadDataMap(){
	 Thread thread=Thread.currentThread();
  	long threadId=thread.getId();
  	Map threadData=threadDataMap.get(threadId);
  	if(threadData==null){
  		log.info("�����̶߳�Ӧ����");
  		threadData = new HashMap();
  		threadDataMap.put(threadId, threadData);
   	 }
    return threadData;
   }  
   /**
    * ȡ�ñ���Ҫ�����Ҫɾ��������
    * @return
    */
  public  Map<Class ,List> getThreadRemoveDataMap(){
	 Thread thread=Thread.currentThread();
  	long threadId=thread.getId();
  	Map<Class ,List> threadData=threadRemoveDataMap.get(threadId);
  	if(threadData==null){
  		log.info("�����̶߳�Ӧ����");
  		threadData = new HashMap();
  		threadRemoveDataMap.put(threadId, threadData);
   	 }
    return threadData;
   }  
    
}
