package com.lw.db.transation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.log4j.Logger;

import com.cy.excep.CyException;
import com.cy.invoke.proxy.TransationInterface;
import com.lw.db.DbClient;
import com.lw.db.file.TempFileData;
 


public class TransationImp implements TransationInterface {
	public static Logger log = Logger.getLogger(TransationImp.class);
	TransactionData transactionData = TransactionData.getInstance();
	DbClient dbClient = DbClient.getInstance();
	TempFileData tempFileData =TempFileData.getInstance();
	public boolean commit() {
  		 try{
 		 
			 Map<Class ,Map> threadDataMap= transactionData.getThreadDataMap();
			 for(Map.Entry<Class ,Map> entry:threadDataMap.entrySet()){
				 Class fileName=entry.getKey();
					log.info("提交事务 ，修改和新加的数据。。。filename="+fileName);
				 Map<Long,Object> dataMap=entry.getValue();
				 tempFileData.saveData(fileName, dataMap);
  			  }
 			 //删除
 			 Map<Class ,List> threadRemoveDataMap = transactionData.getThreadRemoveDataMap();
  			 for(Map.Entry<Class ,List> entry:threadRemoveDataMap.entrySet()){
  				Class fileName=entry.getKey();
				 List<Long>  dataList=entry.getValue();
				 log.info("提交事务 ，删除的数据。。。fileName="+fileName);
 					 for(Long id:dataList){
 						tempFileData.remote(fileName, id);
					 }   
				 
  			 }
 		 }catch(Exception e){
			log.error("提交事务失败", e);
			throw new RuntimeException("提交事务失败");
		 }finally{
			transactionData.remoteThreadData();
		 }
  		return true;
	}

	@Override
	public void reBack() {
		 log.warn("抛出异常，导致事务回滚");
		 transactionData.remoteThreadData();
 	}

}