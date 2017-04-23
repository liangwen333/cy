package com.lw.db.file;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

 public class BeanToMapStrf {
	private static List baseTypeList = new ArrayList();
	
	public BeanToMapStrf(){
		baseTypeList.add(Integer.class);
		baseTypeList.add(Float.class);
		baseTypeList.add(Double.class);
		baseTypeList.add(Byte.class);
		baseTypeList.add(Short.class);
		baseTypeList.add(String.class);
		baseTypeList.add(Date.class);
		baseTypeList.add(Long.class);
		baseTypeList.add(Boolean.class);
	}
	//把对象办转为Map
 	public static Object convertMap(Class type, Object data)
			throws Exception {
     
		if (type.equals(String.class)) {
			return data;
		}
		if  (!(data instanceof Map)) {
  			 return data;
		}
		if  (((Map )data).size()<=0) {
 			 return data;
		}
 		// 
		if ((data instanceof List)) {
			Object obj = type.newInstance(); // 鍒涘缓 JavaBean 瀵硅薄
			List list = (List) data;
			List strList = new ArrayList();
			for (Object listObj : list) {
 				strList.add(convertMap(type, listObj));
			}
			return strList;
		}
		Object obj=null;
        
        	 obj = type.newInstance(); // 鍒涘缓 JavaBean 瀵硅薄
       
	
		 
		if (!(data instanceof Map)) {
			return data;
		}
		Map map = (Map) data;
		// 缁�JavaBean 瀵硅薄鐨勫睘鎬ц祴鍊�
		Method[] methods = getAllField(type); // 鑾峰彇绫诲睘鎬�
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			String propertyName = "";
			if (methodName.indexOf("set") == 0) {
				propertyName = methodName.substring(3);
			} else {
				continue;
			}
			propertyName = propertyName.substring(0, 1).toLowerCase()
					+ propertyName.substring(1);
			//濡傛灉鏄疄渚嬬殑BaseVo涓嶈皟鐢╯etBegin 鑰岃皟鐢╯etBeginVale
		
			 
 			if (map.containsKey(propertyName)||"beginVale".equals(propertyName)) {
				// 涓嬮潰涓�彞鍙互 try 璧锋潵锛岃繖鏍峰綋涓�釜灞炴�璧嬪�澶辫触鐨勬椂鍊欏氨涓嶄細褰卞搷鍏朵粬灞炴�璧嬪�銆�
 				Object value = map.get(propertyName);
 				if (value == null) {
					if("beginVale".equals(propertyName)){
						propertyName="begin";
						value = map.get(propertyName);
				       }
 				}
				if (value == null) {
					continue;
				}
				
				if (value instanceof Map) {
					Map mapListInfoBean = (Map) value;
					String lisbBeanClassStr = (String) mapListInfoBean
							.get("lisbBeanClass");
					Object objectData =  mapListInfoBean.get("mapList");

					if (mapListInfoBean.size() == 2 && objectData != null
							&& lisbBeanClassStr != null) {
						Class listBeanType = Class.forName(lisbBeanClassStr);
						if (listBeanType != null) {
							// 鎶奙ap杞负Bean
							if(objectData instanceof  List){
								List objList = (List)objectData;
								List beanList = new ArrayList();
								for (Object objTemp : objList) {
									if (objTemp instanceof Map) {
										beanList.add(convertMap(listBeanType
												 , (Map) objTemp));
									} else {
										objTemp = converType(listBeanType,objTemp);
										beanList.add(objTemp);
									}
								}
								value = beanList;
							}else{
							   value = convertMap(listBeanType
										 ,  objectData);
							   
							}
							
						}
					}

				}
				setField(obj, method, value);
			}
		}
		return obj;
	}

    //把Bean转为Map
	public static Object convertBeanSun(Object bean) throws Exception {
		if ((bean instanceof List)) {
			List list = (List) bean;
			List strList = new ArrayList();
			for (Object listObj : list) {
				 
				strList.add(convertBeanSun(listObj));
			}
			if(strList.size()<=0){
				return null;
			}
			return strList;
			// return strList;
		}
 		Class type = bean.getClass();
		Map returnMap = new HashMap();

		Method[] methods = getAllField(type); // 鑾峰彇绫诲睘鎬�
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if (methodName.indexOf("get") != 0 && methodName.indexOf("is") != 0 ) {
				continue;
			}
			if(methodName.equals("getClass")){
				continue;
			}
 			String propertyName = "";
			if (methodName.indexOf("get") == 0) {
				propertyName = methodName.substring(3);
			} else if (methodName.indexOf("is") == 0) {
				propertyName = methodName.substring(2);
			} else {
				continue;
			}
			
			Object result = getField(bean, method);
			if (result == null) {
				continue;
			} else if (result instanceof List) {
				if (((List) result).size() <= 0) {
					continue;
				}
			} else if (result.getClass().equals(Integer.TYPE)  ) {
				if (((Integer) result) == 0) {
					continue;
				}
			} else if (result.getClass().equals(Long.TYPE)) {
				if (((Long) result) == 0) {
					continue;
				}
			} else if (result.getClass().equals(Float.TYPE)) {
				if (((Float) result) == 0.0) {
					continue;
				}
			}else if (result.getClass().equals(Double.TYPE)) {
				if (((Double) result) == 0.0) {
					continue;
				}
			}
			propertyName = propertyName.substring(0, 1).toLowerCase()
			+ propertyName.substring(1);
			if (result instanceof List) {
				List list = (List) result;
				Map  mapListInfoBean = null;
				// 鎶奓ist鐨凚ean杞彉涓篗ap
				String listBeanClass = null;
				List mapList = new ArrayList();
				for (Object listObj : list) {
					if (!baseTypeList.contains(result.getClass())) {
						mapList.add(convertBeanSun(listObj));
					} else {// 鍙戠幇涓�釜涓嶆槸Bean锛岀洿鎺ヨ烦鍑轰笉鐢ㄨ浆鎹簡
						mapList = list;
						break;
					}
				}
				listBeanClass = list.get(0).getClass().getName();
				mapListInfoBean = new HashMap();
				mapListInfoBean.put("lisbBeanClass",listBeanClass);
				mapListInfoBean.put("mapList",mapList);
				if (mapListInfoBean != null) {
					result = mapListInfoBean;
				} else {
					result = mapList;
				}
			} else if(!baseTypeList.contains(result.getClass())) {
 				Map mapListInfoBean = new HashMap();
				mapListInfoBean.put("lisbBeanClass",result.getClass().getName());
				mapListInfoBean.put("mapList",convertBeanSun(result));
			    result = mapListInfoBean;
			}
			returnMap.put(propertyName, result);
		}
		return returnMap;
	}
	
	
 

	private static void setField(Object bean, Method method, Object value)
			throws Exception {
		if (value == null) {
			return;
		}
		Class[] paramClass = method.getParameterTypes();
		if (paramClass == null || paramClass.length != 1) {
			return;
		}
		Class paramClassTemp = paramClass[0];
		if (paramClass[0].equals(Float.TYPE)) {
			paramClassTemp = Float.class;
		} else if (paramClass[0].equals(Long.TYPE)) {
			paramClassTemp = Long.class;
		} else if (paramClass[0].equals(Integer.TYPE)) {
			paramClassTemp = Integer.class;
		} else if (paramClass[0].equals(Double.TYPE)) {
			paramClassTemp = Double.class;
		}
		try {
			value=converType(paramClassTemp,value);
 			method.invoke(bean, value);
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}
	
	
	private static Object  converType(Class type,Object value){
		if (!value.getClass().equals(type)) {
			if (type.equals(Float.class)) {
				value = Float.parseFloat(String.valueOf(value));
			} else if (type.equals(Long.class)) {
				value = Long.parseLong(String.valueOf(value));
			} else if (type.equals(Integer.class)) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (type.equals(Double.class)) {
				value = Double.parseDouble(String.valueOf(value));
			}else if(type.equals(Date.class)){
				value = new Date(Long.parseLong(String.valueOf(value)));
			}else if(type.equals(Date.class)){
				value = new Date(Long.parseLong(String.valueOf(value)));
			}else if(type.equals(byte[].class)){
				value = String.valueOf(value).getBytes();
			}else if(type.isArray()&&value instanceof List){
				List list=(List)value;
				List valueArray=new ArrayList();
				for(int i=0;i<list.size();i++){
					Object val=list.get(i);
					valueArray.add(converType(type.getComponentType(),val));
				 }
	 			return valueArray.toArray();
			}
 		}
		return value;
	}
	

	private static Object getField(Object bean, Method method) throws Exception {
		String name = method.getName();
		if (name.indexOf("get") == 0 || name.indexOf("is") == 0) {
			try {
				Class [] paramClass = method.getParameterTypes();
				if(paramClass.length==0){
					return method.invoke(bean);
				}
 			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	private static Method[] getAllField(Class type) {
		Method[] methods = type.getMethods();
		return methods;
	}

	private static byte[] mapToByte(Object map) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objout = new ObjectOutputStream(byteOut);
		objout.writeObject(map);
		return byteOut.toByteArray();
	}

 

}
