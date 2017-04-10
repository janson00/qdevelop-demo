package cn.qdevelop.demo.utils;

import cn.qdevelop.core.template.QDevelopHelper;

public class SQLTemplate {

	public static void main(String[] args) {
		QDevelopHelper.createSQLConfig("qd_product_center_write", "products");
		QDevelopHelper.createSQLConfig("qd_product_center_write", "products_log");
	}

}
