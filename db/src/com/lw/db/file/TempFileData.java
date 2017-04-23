package com.lw.db.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lw.db.bean.TempFileBean;

/**
 * 
 * @author lenovo
 *文件数据
 */
public class TempFileData {
	private final String folder="tempPath";
	
	private static TempFileData tempFileData ;
 	private FileSrv fileSrv = new FileSrv();
	private BeanToMapStrf beanToMapStrf = new BeanToMapStrf();
	private TempData tempData = new TempData();
	 
	public static synchronized TempFileData getInstance(){
		if(tempFileData==null){
			tempFileData = new TempFileData();
		}
		return tempFileData;
	}
 	public TempFileData(){
		createParent();
	}
    //保存数据
	public void saveData(Object dataClass,Map dataMap) throws Exception{
		if(dataMap==null||dataMap.size()<=0){
			
		}
		String fileName=tempData.getFileName(folder);
		Map tempMap = tempData.getTempDataMap(getSaveMap(dataMap), dataClass,0);
 		fileSrv.saveFile(tempMap, fileName);
  	}
	//
	public void saveData(Object dataClass,Object key,Object value) throws Exception{
		Map map = new HashMap();
		map.put(key, value);
		saveData(dataClass,map);
  	}
	
	public void remote(Object dataClass,Object key) throws Exception{
		String fileName=tempData.getFileName(folder);
		HashMap map = new HashMap();
		map.put(key, null);
		Map tempMap = tempData.getTempDataMap(map, dataClass,1);
 		fileSrv.saveFile(tempMap, fileName);
  	}
	
	//把Bean转换为Map
	private HashMap getSaveMap(Map dataMap) throws Exception{
		Set<Long> idSet = dataMap.keySet();
		HashMap saveMap = new HashMap();
		for (Long id : idSet) {
			Object t = dataMap.get(id);
			Object obj =(HashMap) BeanToMapStrf.convertBeanSun(t);
			// Object obj = BeanToMapStrf.convertBean(t);
			saveMap.put(id, obj);
		}
		return saveMap;
	}
	
 
	public TempFileBean  getTempList() throws Exception{
		return  fileSrv.getTempFileMap(folder, TempData.fileAfter);
	}
	 
	
	private void createParent() {
		File file = new File(folder);
		if (!file.exists()) {
			file.mkdirs();
		}
		
//		File filebak = new File("data" + "_bak");
//		if (!filebak.exists()) {
//			filebak.mkdirs();
//		}
	}
	
}
