package test;

import com.cy.vo.OrderVo;
import com.cy.vo.CommentVo;
import com.lw.db.DbClient;
import com.lw.db.DbDao;
import com.lw.db.merge.TimeMerge;

public class TestMain {
	
	 
	public static void main(String[] args) throws Exception {
		TimeMerge timeMerge = new TimeMerge();
		timeMerge.start();
		OrderVo  orderVo1 = new OrderVo();
		OrderVo  orderVo2 = new OrderVo();
		orderVo2.setOrderName("test2");
		orderVo1.setOrderName("test1");
		CommentVo commentVo = new CommentVo();
		DbClient dbClient = DbClient.getInstance();
		DbDao dbDao1 = dbClient.getDao(OrderVo.class);
//		dbDao1.put(1L, orderVo1);
//		dbDao1.put(2L, orderVo2);
//		OrderVo orderVo11=(OrderVo) dbDao1.get(1L);
//		OrderVo orderVo12=(OrderVo) dbDao1.get(2L);
		//dbDao1.remote(1L);
		OrderVo orderVo11=(OrderVo) dbDao1.get(1L);
 		DbDao dbDao = dbClient.getDao(CommentVo.class); 
		dbDao.put(1L, commentVo);
 	}
 }
