package cn.qdevelop.demo.utils;

import cn.qdevelop.core.template.QDevelopHelper;

public class SQLTemplate {

	/**
	 * 手动根据数据库配置，指定表生成sqlConfig模版
	 * @param args
	 */
	public static void main(String[] args) {
		QDevelopHelper.createSQLConfig("qd_product_center_write", "products");
		QDevelopHelper.createSQLConfig("qd_product_center_write", "products_log");
	}

}
