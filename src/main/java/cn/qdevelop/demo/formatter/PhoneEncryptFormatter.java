package cn.qdevelop.demo.formatter;

import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Element;

import cn.qdevelop.common.exception.QDevelopException;
import cn.qdevelop.core.formatter.AbstractResultFormatter;
import cn.qdevelop.core.standard.IDBResult;

/**
 * 自定义结果集中手机号字段名，对结果集中查询出来的手机号隐藏中间四位
 * 自定义对数据库查询出的结果集进行个性化处理
 * 需要将本类注册到qdevelop-formatter.xml中，并且在sql配置xml中使用自定义标签
 * @author janson
 *
 */
public class PhoneEncryptFormatter extends AbstractResultFormatter{
	
	String targetKey;
	
	public void initFormatter(Element conf) throws QDevelopException {
		targetKey = conf.attributeValue("result-key");
	}

	public boolean isQBQuery() {
		return false;
	}

	Pattern isPhoneNumber = Pattern.compile("^[0-9]{11}$");
	
	public void formatter(Map<String, Object> data) {
		String targetVal = String.valueOf(data.get(targetKey));
		if(isPhoneNumber.matcher(targetVal).find()){
			data.put(targetKey, (targetVal.substring(0,3)+"****"+targetVal.substring(7)));
		}
	}

	public void flush(IDBResult result) throws QDevelopException {
		
	}
	

}
