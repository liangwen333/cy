package com.lw.db.merge;

import java.util.Timer;

/**
 * 
 * @author lenovo
 *  定时器
 */
public class TimeMerge {
	private static boolean isStart=false;
	
	/**
	 * 启动定时器
	 */
	public synchronized void start(){
		if(!isStart){
			MergeTimerTask mergeTimerTask  = new MergeTimerTask();
			Timer timer = new Timer(); 
			timer.schedule(mergeTimerTask, 0,30000); 
			isStart= true;
		}

	}
	
	
	

}
