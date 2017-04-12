package cn.qdevelop.demo.api;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import cn.qdevelop.common.exception.QDevelopException;
import cn.qdevelop.core.DatabaseFactory;
import cn.qdevelop.core.standard.IDBResult;
import cn.qdevelop.service.APIControl;
import cn.qdevelop.service.IOutput;
import cn.qdevelop.service.IService;

/**
 * 
 * 自定义接口
 * 			   
 * 
 * @author janson
 *
 */
@WebServlet(
		//自定义接口URI，前端可直接访问；注意系统内不可重复，否则会启动报错
		urlPatterns="/app/api/updateOrInsertProductInfo",
		loadOnStartup=1,initParams={
				//value内填写需要验证必须存在的参数，其他参数将自动按数据库内的格式自动校验,可将绝大部分数据验证自动处理了
				@WebInitParam(name=IService.INIT_VALID_REQUIRED,value="product_name,price,store"),
				//value内填写忽略验证的参数，有些特殊参数可能会被误拦截为可疑hack字符
				@WebInitParam(name=IService.INIT_VALID_IGNORE,value="")
		})
public class SimpleAPIModel  extends APIControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4211364302671228332L;

	/**
	 * 实现一个根据商品名判断，存在则修改库存，不存在则增加商品的实例
	 * 该实例展示了几点：
	 * 	1、如何自定结果集
	 * 	2、如何自定义参数检查
	 * 	3、如何自己控制一系列查询和更新混用
	 */
	@Override
	protected String execute(Map<String, String> args, IOutput output) {
		DatabaseFactory df = DatabaseFactory.getInstance();
		args.put("index", "SimpleAPIModel-check-exsit");
		
		/**手动根据index再次调用参数根据数据库字段类型的校验，验证不通过可直接返回**/
		if(!validParameters(args)){
			return null;
		}
		
		Connection conn = null;
		Map<String, Object> query = new HashMap<String, Object>();
		try {
			/**自定义结果集**/
			Map<String,Object> r = new HashMap<String,Object>();
			
			/**自己获取链接进行自定义操作**/
			conn = df.getConnectByQuery(args);
			
			/**自己控制查询，不释放链接和查询用的hashmap，需要自己释放**/
			IDBResult result = df.queryDatabaseSelfControl(args, conn);
			
			query.putAll(args);
			/**判定需要的操作，存在则更新，不存在则插入**/
			if(result.getSize()>0){
				/**转到更新sql索引进行操作**/
				query.put("index", "SimpleAPIModel-check-update");
				query.put("action","update");
				query.put("uid","0");
				query.put("pid", result.getResult(0).get("pid"));
				
				/**自定义结果集**/
				r.put("act", 2);
				r.put("name", "更新");
				
			}else{
				/**转到插入sql进行操作**/
				query.put("index", "SimpleAPIModel-check-insert");
				query.put("action","insert");
				query.put("uid","0");
				query.put("ctime", new Date());
				
				/**自定义结果集**/
				r.put("act", 1);
				r.put("name", "插入");
			}
			query.put("dg", System.currentTimeMillis());
			
			/**自己控制更新操作，不释放链接和查询用的hashmap，需要自己释放**/
			boolean isSuccess = df.updateDatabaseSelfControl(query, conn);//回写至数据库
			r.put("success", isSuccess);
			
			/**设置主要输出的结果集**/
			output.setData(r);
			
		} catch (QDevelopException e) {
			e.printStackTrace();
			/**程序出错，输出统一异常信息**/
			output.setErrMsg(e.getMessage());
		}finally{
			/**自己清理**/
			args.clear();
			query.clear();
			df.closeConnection(conn);
		}
		return null;
	}

}
