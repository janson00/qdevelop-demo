package cn.qdevelop.demo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.qdevelop.common.exception.QDevelopException;
import cn.qdevelop.core.DatabaseFactory;

public class Start {

	static Random r = new Random();
	static String[] strDict = "赵钱孙李周吴郑王冯陈楮卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闽席季麻强贾路娄危江童颜郭梅盛林刁锺徐丘骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麹家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘斜厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍郤璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逑盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐锺离宇文长孙慕容鲜于闾丘司徒司空丌官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓拔夹谷宰父谷梁晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况后有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟".split("");
	static int[] numDict = new int[]{0,1,2,3,4,5,6,7,8,9};

	public static String getUserName(){
		StringBuffer str = new StringBuffer();
		for(int i=0;i<3;i++){
			str.append(strDict[r.nextInt(strDict.length)]);
		}
		return str.toString();
	}

	public static String getProductName(){
		StringBuffer str = new StringBuffer();
		int size = r.nextInt(12);
		if(size<6){
			size = 5;
		}
		for(int i=0;i<size;i++){
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
	public  static void loadTestData(int userNumber,int productNumber){
		ArrayList<Integer> uids = new ArrayList<Integer>();
		for(int i=0;i<userNumber;i++){
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
			for(int i=0;i<productNumber;i++){
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
	
	public static void insertUserBatch(){
		Map<String,Object> userInfo = new HashMap<String,Object>();
		userInfo.put("index", "users-insert-auto");
		userInfo.put("utime", "?");
		userInfo.put("user_name", "?");
		userInfo.put("status", "?");
		userInfo.put("age", "?");
		userInfo.put("ctime", "?");
		userInfo.put("mobile", "?");
		
		ArrayList<Object[]> values = new ArrayList<Object[]>();
		for(int i=0;i<1100;i++){
			Object[] user = new Object[6];
			user[0] = new Date();
			user[1] = getUserName();
			user[2] = r.nextInt(3);
			user[3] = r.nextInt(50);
			user[4] = new Date();
			user[5] = getMobile();
			if(i==1009){
				user[2] = "asdasd";
			}
			values.add(user);
		}
		
		try {
			DatabaseFactory.getInstance().updateBatch(userInfo, values);
		} catch (QDevelopException e) {
			e.printStackTrace();
		}
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
		loadTestData(10,100);
		
//		System.out.println(String.valueOf(System.currentTimeMillis()).length());

		
//		insertUserBatch();


	}

}
