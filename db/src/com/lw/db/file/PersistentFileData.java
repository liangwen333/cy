package com.lw.db.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.lw.db.cache.VmDataCache;

/**
 * 
 * @author lenovo
 *
 */
public class PersistentFileData {
	private static PersistentFileData persistentFileData;
	private BeanToMapStrf beanToMapStrf = new BeanToMapStrf();
	private FileSrv fileSrv = new FileSrv();
	private VmDataCache vmDataCache = new VmDataCache();
	public synchronized static PersistentFileData getInstance(){
		if(persistentFileData==null){
			persistentFileData =new  PersistentFileData();
		}
		return persistentFileData;
	}
	
	public PersistentFileData(){
		createParent();
	}
	
	public Map getSaveMap(Object dataClass,Class cl) throws Exception{
		Map mapData =  vmDataCache.getMap(dataClass);
		if(mapData==null){
			String fileName=getFileName(dataClass);
			Map<Object,Map> dataMap =fileSrv.readFile(fileName);
			Map<Object,Object> beamMap= new HashMap();
			if(dataMap==null){
				dataMap = new HashMap();
			} 
			//°ÑMap×ª»»ÎªBean
			for(Map.Entry<Object,Map> entry:dataMap.entrySet()){
				try{
					Object bean= beanToMapStrf.convertMap(cl, entry.getValue());
					beamMap.put(entry.getKey(), bean);
				}catch(Exception e){
					e.printStackTrace();
				}
			
			
			}
 			vmDataCache.setMap(dataClass, beamMap);
			mapData = vmDataCache.getMap(dataClass);
		}
		return mapData;
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
	
	
	 
	public void deleteFile(Object dataClass){
		String fileName=getFileName(dataClass);
		File file=new File(fileName);
		if(file.exists()){
			file.delete(); 
		}
		Map data= vmDataCache.getMap(dataClass);
		if(data!=null){
			data.clear();
		}
 }
	
	
	
	public String getFileName(Object dataClass){
		if(dataClass instanceof Class){
			return  "data\\"+((Class)dataClass).getSimpleName()+".cl";
		}
		return (String) dataClass;
	}
	
	
	private void createParent() {
		File file = new File("data");
		if (!file.exists()) {
			file.mkdirs();
		}
 		 
	}
}
