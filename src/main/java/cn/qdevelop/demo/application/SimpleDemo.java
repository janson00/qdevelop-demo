package cn.qdevelop.demo.application;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.qdevelop.common.exception.QDevelopException;
import cn.qdevelop.common.utils.MapUtil;
import cn.qdevelop.core.DatabaseFactory;
import cn.qdevelop.core.QDevelopUtils;
import cn.qdevelop.core.standard.IDBResult;

public class SimpleDemo {
	static Random r = new Random();
	public static int random(){
		return r.nextInt(100);
	}

	public static void insertProduct(){
		//		Map<String,Object> arg = MapUtil.parseMapFromStr("index=simple_product_insert&product_name=test_"+random()+"&price="+"+random()+"+".01&store="+random());
		Map<String,Object> product = new HashMap<String, Object>();
		product.put("index", "simple_product_insert");
		product.put("price", random()+"."+random());
		product.put("store", random());
		product.put("product_name", "test_"+random());
		product.put("dg", new Date().getTime());
		try {
			DatabaseFactory.getInstance().updateDatabase(product);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertProductBatch(){
		
		List<Object[]> values = new ArrayList<Object[]>();
		for(int i=0;i<10000;i++){
			if(i==1){
				values.add(new Object[]{
						"test_"+random(),//product_name
						random()+"."+random(), //price
						"1", //store
						System.currentTimeMillis()
				});
			}else{
			values.add(new Object[]{
					"test_"+random(),//product_name
					random()+"."+random(), //price
					random(), //store
					System.currentTimeMillis()
			});
			}
		}
		
		Map<String,Object> product = new HashMap<String, Object>();
		product.put("index", "simple_product_insert");
		product.put("product_name", "?");
		product.put("price", "?");
		product.put("store", "?");
		product.put("dg", "?");
		
		try {
			DatabaseFactory.getInstance().updateBatch(product, values);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void insertRepeatProduct(){
//				Map<String,Object> arg = MapUtil.parseMapFromStr("index=simple_product_insert&product_name=test_"+random()+"&price="+"+random()+"+".01&store="+random());
		Map<String,Object> product = new HashMap<String, Object>();

		product.put("index", "simple_product_insert_repeat");
//		product.put("price", random()+"."+random());
//		product.put("store", random());
//		product.put("product_name", "test_"+random());
		product.put("dg", new Date().getTime());
		product.put("product_name@price@store", "xx@12.1@10^@0@0");
		try {
			DatabaseFactory.getInstance().updateDatabase(product);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}


	public static void selectProduct(){
		Map<String,Object> arg = MapUtil.parseMapFromStr("index=simple_product_select&page=100&limit=50&store=>10&order=store");
		arg.put("ctime", ">='2017-03-14 00:53:20'&<='2017-03-14 23:53:20'");
		try {
			IDBResult rb =  DatabaseFactory.getInstance().queryDatabase(arg);
			for(int i=0;i<rb.getSize();i++){
				System.out.println(rb.getResult(i));
			}
			Map<String,Object> arg3 = MapUtil.parseMapFromStr("index=simple_product_select&page=1&limit=3&store=>10&order=store");
			arg3.put("ctime", ">='2017-03-14 00:53:20'&<='2017-03-14 23:53:20'");
			int total = DatabaseFactory.getInstance().queryDatabaseCount(arg3);
			System.out.println(total);
			
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 同数据库，多库执行操作
	 */
	public static void updateMultiSql(){
		Map<String,Object> product = new HashMap<String, Object>();
		//sql配置索引
		product.put("index", "simple_product_insert_log");

		//products表所需参数集
		product.put("price", random()+"."+random());
		product.put("store", random());

		product.put("product_name", "test_"+random());/*此参数两表公用*/

		//products_log表所需参数集
		product.put("action", "insert");
		product.put("uid", random());

		try {
			DatabaseFactory.getInstance().updateDatabase(product);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 不同数据库之间的夸库事务操作
	 */
	public static void updateMultiDatabase(){
		Map<String,Object> product = MapUtil.parseMapFromStr("index=simple_product_insert&product_name=test_"+random()+"&price=213&store="+random());
		Map<String,Object> user = MapUtil.parseMapFromStr("index=simple_user_insert&user_name=test_"+random()+"&mobile=13911114927&age="+random());
		try {
			Map[] args = new Map[]{
					product,user,user	
			};
			DatabaseFactory.getInstance().updateDataBaseMulti(args);
//			DatabaseFactory.getInstance().updateDataBaseMulti(product,user,user);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}


	public static void selectByKeyValueFormatter(){
		Map<String,Object> arg = MapUtil.parseMapFromStr("index=product_log_formatter_query&page=1&limit=5");
		try {
			IDBResult rb =  DatabaseFactory.getInstance().queryDatabase(arg);
			for(int i=0;i<rb.getSize();i++){
				System.out.println(rb.getResult(i));
			}
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		System.out.println("bigInt".equalsIgnoreCase("bigint"));
		QDevelopUtils.initAll();
//		insertRepeatProduct();
//				updateMultiSql();
//				updateMultiDatabase();
//		long s = System.currentTimeMillis();
		
//		for(int i=0;i<100;i++){
			selectProduct();
//		}
//		System.out.println(System.currentTimeMillis() - s);
		//		selectByKeyValueFormatter();
//		insertProductBatch();
//		Pattern cleanSplit = Pattern.compile("[0-9a-zA-Z\\_]+");
//		System.err.println(cleanSplit.matcher("asd_as_d@a_sd@asdd").replaceAll(""));
//		System.out.println(System.getProperty("os.name").toLowerCase());
//		String t = "/Users/janson/workspace/qdevelop-demo/target/classes/";
//		 Pattern clearProjectName = Pattern.compile("\\/bin\\/?$|\\/WEB-INF.+?$|\\/lib\\/?$|\\/target.+?$");
//		System.out.println(clearProjectName.matcher(t).replaceAll(""));
	}

}
