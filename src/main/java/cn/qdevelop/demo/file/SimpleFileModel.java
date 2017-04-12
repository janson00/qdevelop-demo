package cn.qdevelop.demo.file;

import java.io.InputStream;
import java.util.Map;

import cn.qdevelop.service.UploadControl;

public class SimpleFileModel extends UploadControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean disposeFile(InputStream file, String fileName, String storeName, long size) {
		return false;
	}

	@Override
	protected String execute(Map<String, String> args, String[] storeName, StringBuffer output) {
		output.append("{}");
		return null;
	}

}
