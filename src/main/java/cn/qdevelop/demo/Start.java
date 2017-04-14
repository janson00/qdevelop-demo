package cn.qdevelop.demo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.qdevelop.common.exception.QDevelopException;
import cn.qdevelop.core.DatabaseFactory;

public class Start {

	static Random r = new Random();
	static String[] strDict = "qwertyuiopasdfghjklzxcvbnm".split("");
	static int[] numDict = new int[]{0,1,2,3,4,5,6,7,8,9};

	public static String getUserName(){
		StringBuffer str = new StringBuffer();
		for(int i=0;i<6;i++){
			str.append(strDict[r.nextInt(strDict.length)]);
		}
		return str.toString();
	}

	public static String getProductName(){
		StringBuffer str = new StringBuffer();
		for(int i=0;i<20;i++){
			str.append(strDict[r.nextInt(strDict.length)]);
		}
		return str.toString();
	}

	public static String getMobile(){
		StringBuffer str = new StringBuffer();
		str.append("1");
		for(int i=0;i<10;i++){
			str.append(numDict[r.nextInt(numDict.length)]);
		}
		return str.toString();
	}

	public static int getStore(){
		return numDict[r.nextInt(numDict.length)]*1000 
				+ numDict[r.nextInt(numDict.length)]*100 
				+ numDict[r.nextInt(numDict.length)]*10 
				+ numDict[r.nextInt(numDict.length)];
	}

	public static double getPrice(){
		return getStore() * 0.01;
	}

	/**
	 * 往数据库内增加数据
	 */
	public  static void loadTestData(){
		ArrayList<Integer> uids = new ArrayList<Integer>();
		for(int i=0;i<1000;i++){
			try {
				Map<String,Object> query = new HashMap<String,Object>();
				query.put("index", "users-insert-auto");
				query.put("utime", new Date());
				query.put("user_name", getUserName());
				query.put("status", r.nextInt(3));
				query.put("age", r.nextInt(2));
				query.put("ctime", new Date());
				query.put("mobile",getMobile() );
				int id = DatabaseFactory.getInstance().insertDBReturnAutoID(query);
				uids.add(id);
			} catch (QDevelopException e) {
				e.printStackTrace();
			}
		}
		int len = uids.size();
		
		Connection conn = null;
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("index", "SimpleAPIModel-check-insert");
		try {
			conn = DatabaseFactory.getInstance().getConnectByQuery(query);
			for(int i=0;i<10000;i++){
				query.put("product_name", getProductName());
				query.put("price", getPrice());
				query.put("store", getStore());
				query.put("action","insert");
				query.put("dg",System.currentTimeMillis());
				query.put("status", r.nextInt(3));
				query.put("uid",uids.get(r.nextInt(len)));
				DatabaseFactory.getInstance().updateDatabaseSelfControl(query, conn);
			}
		} catch (QDevelopException e) {
			e.printStackTrace();
		}finally{
			query.clear();
			DatabaseFactory.getInstance().closeConnection(conn);
		}
	}

	public void test(){
//		//$[uid],$[dg],$[price],$[store],$[status],'$[product_name]',$[ctime]
//		List<Object[]> products = new ArrayList<Object[]>();
//		Map<String,Object> productsQuery = new HashMap<String,Object>();
//		productsQuery.put("index", "products-insert-auto");
//		productsQuery.put("uid", "?");
//		productsQuery.put("dg", "?");
//		productsQuery.put("price", "?");
//		productsQuery.put("store", "?");
//		productsQuery.put("status", "?");
//		productsQuery.put("product_name", "?");
//		productsQuery.put("ctime", "?");
//		
//		//$[uid],'$[product_name]','$[action]',$[pid],$[ctime]
//		List<Object[]> productsLog = new ArrayList<Object[]>();
//		Map<String,Object> productsLogQuery = new HashMap<String,Object>();
//		productsLogQuery.put("index", "products_log-insert-auto");
//		productsLogQuery.put("uid", "?");
//		productsLogQuery.put("product_name", "?");
//		productsLogQuery.put("pid", "?");
//		productsLogQuery.put("ctime", "?");
//		
//		for(int i=0;i<10000;i++){
//			Object[] p = new Object[7];
//			Object[] pl = new Object[5];
//			String productName = getProductName();
//			Integer uid = uids.get(r.nextInt(len));
//			p[0] = uid;
//			pl[0] = uid;
//			
//			
//		}
	}

	/**
	 * 手动根据数据库配置，指定表生成sqlConfig模版
	 * @param args
	 */
	public static void main(String[] args) {
		//		QDevelopHelper.createSQLConfig("qd_product_center_write", "products");
		//		QDevelopHelper.createSQLConfig("qd_product_center_write", "products_log");
		//		QDevelopHelper.createSQLConfig("qd_user_center", "users");
		//将项目下生成好的文件手动转移至src/main/resources/common-sqls/demo下
//		loadTestData();
		
		System.out.println(String.valueOf(System.currentTimeMillis()).length());



	}

}
