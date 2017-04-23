package com.lw.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class BaseDbDao<T> {
 
	private DbDao dbDao;
	public BaseDbDao(Class dataClass) throws Exception{
		DbClient dbClient=DbClient.getInstance();
	   dbDao = dbClient.getDao(dataClass);
	}
	
	public BaseDbDao(String id,Class dataClass) throws Exception{
	    DbClient dbClient=DbClient.getInstance();
	   dbDao = dbClient.getDao(id,dataClass);
	}
	
 	public void put(Long id, T bean) throws Exception {
 		dbDao.put(id, bean);
	}
	public T get(Long id) throws Exception {
		return  (T)dbDao.get(id);
	}
	
	public Map getAllData() throws Exception {
		return  dbDao.getAll();
	}
	
	public void remove(long  key) throws Exception{
		dbDao.remove(key);
	}
	
	
	public void putAll(Map datas, boolean isAll) throws Exception {
		dbDao.putAll(datas,isAll);
	}
	 
	public List<T>  getAllList(){
		return  dbDao.getAllList();
	}
	public void removeAll(String ids) throws Exception{
		String [] idArry=ids.split(",");
		for(String id:idArry){
			Long idL=Long.parseLong(id);
			dbDao.remove(idL);
		}
	}
	
	//删除文件，
	public void deleteFile(){
		dbDao.delete();
	}
	public int updateOrSave(Long id, T bean) throws Exception {
		T bean1 = get(id);
		if (bean1 == null) {
			put(id, bean);
			return 1;
		}
		updateValue(bean1, bean);
		put(id, bean1);
		return 1;
	}

	
	
	public static void updateValue(Object srcbean, Object targetBean)
			throws Exception {
		Field[] fields = targetBean.getClass().getDeclaredFields();
		for (Field field : fields) {
			// System.out.println(field);
			Object value = null;
			if (field.getType().equals(Boolean.TYPE)
					|| field.getType().equals(List.class)) {
				// value= field.getBoolean(srcbean);
				value = getValue(targetBean, field.getName());
			} else {
				value = getValue(targetBean, field.getName());
			}

			if (value == null) {
				continue;
			}

			String methodName = "set"
					+ field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);
			Method m = getMethodByName(targetBean.getClass(), methodName);
			// 所有的value.getClass()都为String，所以以下判断使用field.getType()
			if (field.getType().equals(long.class)
					|| field.getType().equals(Long.class)) {
				/*
				 * if(value!=null && Long.valueOf((String)value) > 0){
				 * BeanUtils.copyProperty(srcbean, field.getName(), value); }
				 */
				setValue(srcbean, m, value);
			}/*
			 * else if(field.getType().equals(Date.class)){
			 * BeanUtils.copyProperty(srcbean, field.getName(), new
			 * Date((String)value)); }
			 */else if (value != null) {
				// BeanUtils.copyProperty(srcbean, field.getName(), value);
				setValue(srcbean, m, value);
			}
		}
	}
	private static Object getValue(Object srcbean, String name)
			throws Exception {
		Method[] mathods = srcbean.getClass().getMethods();
		int index = -1;
		String get = "get" + name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		String is = "is" + name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		for (Method m : mathods) {
			if (m.getName().equals(get))
				index = 0;
			if (m.getName().equals(is))
				index = 1;
		}
		if (index == 0) {
			return srcbean.getClass().getMethod(get, null)
					.invoke(srcbean, null);
		} else if (index == 1) {
			return srcbean.getClass().getMethod(is, null).invoke(srcbean, null);
		} else {
			return null;
		}
	}

	private static Method getMethodByName(Class clazz, String name) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	private static void setValue(Object bean, Method method, Object value)
			throws Exception {
		if (value == null) {
			return;
		}
		if (method == null) {
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
			value = converType(paramClassTemp, value);
			method.invoke(bean, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object converType(Class type, Object value) {
		if (!value.getClass().equals(type)) {
			if (type.equals(Float.class)) {
				value = Float.parseFloat(String.valueOf(value));
			} else if (type.equals(Long.class)) {
				value = Long.parseLong(String.valueOf(value));
			} else if (type.equals(Integer.class)) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (type.equals(Double.class)) {
				value = Double.parseDouble(String.valueOf(value));
			} else if (type.equals(Date.class)) {
				value = new Date(Long.parseLong(String.valueOf(value)));
			} else if (type.equals(Date.class)) {
				value = new Date(Long.parseLong(String.valueOf(value)));
			} else if (type.isArray() && value instanceof List) {
				List list = (List) value;
				List valueArray = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					Object val = list.get(i);
					valueArray.add(converType(type.getComponentType(), val));
				}
				return valueArray.toArray();
			}
		}
		return value;
	}
}
