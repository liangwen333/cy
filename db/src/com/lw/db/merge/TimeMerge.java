package com.lw.db.merge;

import java.util.Timer;

/**
 * 
 * @author lenovo
 *  ��ʱ��
 */
public class TimeMerge {
	private static boolean isStart=false;
	
	/**
	 * ������ʱ��
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
