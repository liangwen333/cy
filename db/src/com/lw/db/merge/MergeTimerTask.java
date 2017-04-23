package com.lw.db.merge;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lw.db.bean.DataTimeBean;
import com.lw.db.bean.TempFileBean;
import com.lw.db.cache.VmDataCache;
import com.lw.db.file.BakFileData;
import com.lw.db.file.PersistentFileData;
import com.lw.db.file.TempData;
import com.lw.db.file.TempFileData;

public class MergeTimerTask extends java.util.TimerTask {
	private Logger log = Logger.getLogger(this.getClass());
	private TempFileData tempFileData = TempFileData.getInstance();
	private BakFileData bakFileData = new BakFileData();
	private PersistentFileData persistentFileData = new PersistentFileData();
	private VmDataCache vmDataCache = VmDataCache.getInstance();
	
	private TempFileBean tempFileBean = new  TempFileBean();
	private TempData tempData = new TempData();
	@Override
	public void run() {
 		 try {
			TempFileBean tempFileBean =tempFileData.getTempList();
			List dataList  =tempFileBean.getTempDatalist();
			if(dataList.size()<=0){
				log.info("û�����ݺϲ���");
			}
			Map<Object,Map> allDataMap = dataClass(dataList);
			savaData(allDataMap);
			//ɾ���м��ļ�
			File[] files= tempFileBean.getTempFilelist();
			for(File file:files){
 				boolean result= file.delete();
				log.info("delete file,fileName="+file.getName()+",result="+result);
			}
 		} catch (Exception e) {
 			log.error("failt to merge.",e);
 			e.printStackTrace();
		}
	}
	/**
	 * �����ݽ��з���ϲ�
	 * @param dataList
	 * @return
	 * @throws Exception 
	 */
	public Map<Object,Map> dataClass(List<HashMap> dataList) throws Exception{
		 Map<Object,Map> allDataMap = new HashMap();
		for(HashMap tempDataMap:dataList){
			Object classTemp=tempData.getClassType(tempDataMap);
 			Map<Object,Map> dataMap = tempData.getData(tempDataMap);
			//����Ϊ����������
			if(dataMap==null||dataMap.size()<=0){
				log.info("datamap is null."+classTemp);
				continue;
			}
			Date createTime = tempData.getTime(tempDataMap);
			boolean isDel =  tempData.isDelete(tempDataMap);
			Map<Object,DataTimeBean> classDataMap= allDataMap.get(classTemp);
 			if(classDataMap==null){
 				//classDataMap=bakFileData.getSaveMap(classTemp);
 				if(classDataMap==null){
 					classDataMap = new HashMap();
 				} 
 				
  				allDataMap.put(classTemp, classDataMap);
			}
 			 if(dataMap==null){
 				log.error("data is null,classTemp="+classTemp+",isDel="+isDel);
 				continue;
 			 }
 			
 			for(Map.Entry<Object,Map> entry:dataMap.entrySet()){
 				Map data= entry.getValue();
 				Object key=entry.getKey();
 				Object  dataObje =classDataMap.get(key);
 				DataTimeBean dataTimeBean=null;
 				if(dataObje instanceof Map){//�ӱ����ļ����س���������
 					dataTimeBean = biuldDataTimeBean(data,createTime,isDel);
 					classDataMap.put(key, dataTimeBean);
 					continue;
 				}else{
 					dataTimeBean=classDataMap.get(key);
 				}
 				if(dataTimeBean==null
 						||createTime.getTime()>dataTimeBean.getUpdateTime().getTime()){
 					dataTimeBean = biuldDataTimeBean(data,createTime,isDel);
 					classDataMap.put(key, dataTimeBean);
   				}
  			}
 		}
 		return allDataMap;
		
	}
	
	public DataTimeBean biuldDataTimeBean(  Map<Object,Map> data,Date createTime,boolean isDel){
  		    DataTimeBean dataTimeBean = new DataTimeBean();
			dataTimeBean.setDataMap(data);
			dataTimeBean.setDel(isDel);
			dataTimeBean.setUpdateTime(createTime);
			return dataTimeBean;
 	}
	
	public void savaData(Map<Object,Map> allDataMap) throws Exception{
		 Set<Object> keys = allDataMap.keySet();
		for(Object classTemp:keys){
			Map dataMap= allDataMap.get(classTemp);
			//
 			Set<Object> dateKeys = dataMap.keySet();
			for(Object keyTemp:dateKeys){
				DataTimeBean dataTimeBean = (DataTimeBean)dataMap.get(keyTemp);
				if(dataTimeBean.isDel()){
					dataMap.remove(keyTemp);
				}else{
					dataMap.put(keyTemp, dataTimeBean.getDataMap());
				}
 			}
			//����ʱ���ڴ��е����ݺϲ���Ȼ���ٱ���
			if(vmDataCache.getMap(classTemp)!=null){
				dataMap.putAll(vmDataCache.getMap(classTemp));
			}
 			bakFileData.saveMap(classTemp, dataMap);
			persistentFileData.saveMap(classTemp, dataMap);
			
		}
		
	}

}
