package com.lw.db;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		te();
 	}
	
	
	public static void te(){
		final ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap();
		   new  Thread(){
			public void run() {
				for(int i=0;i<1000000;i++){
					concurrentHashMap.put("a"+i, "b");
				}
			}}.start();
			
			 new  Thread(){
	  			public void run() {
	  					 
	  				    for(Map.Entry<String, String> entry:concurrentHashMap.entrySet()){
	  				    	       String key=entry.getKey();
	  				    	try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  				    	System.out.println(key+",s="+concurrentHashMap.size());
	  				    	concurrentHashMap.remove(key);
	  				    }
	 			}}.start();
	}

}
