package cn.qdevelop.demo.file;

import java.io.InputStream;
import java.util.Map;

import cn.qdevelop.service.IOutput;
import cn.qdevelop.service.UploadControl;

public class SimpleFileModel extends UploadControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3476377785909384545L;

	@Override
	public void init(Map<String, String> args) {
		
	}

	@Override
	protected String setFileStoreRootPath() {
		return null;
	}

	@Override
	protected String[] setFileAllowType() {
		return null;
	}

	@Override
	protected boolean disposeFile(InputStream file, String fileName, String storeName, long size) {
		return false;
	}

	@Override
	protected String execute(Map<String, String> args, String[] storeName, IOutput output) {
		return null;
	}


}
