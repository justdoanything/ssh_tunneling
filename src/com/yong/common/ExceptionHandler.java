package com.yong.common;

import com.yong.msg.MsgCodeException;

public class ExceptionHandler {

	public static void exception(String type, String code, String message) throws Exception {
		
		if(type.equals(MsgCodeException.MSG_TYPE_CONFIGURATION)) {
			switch(code) {
			case MsgCodeException.MSG_CODE_WRONG_EXECUTE_TYPE :
				throw new Exception("Wrong remote type in application.yml : " + message);
			case MsgCodeException.MSG_CODE_FILE_NOT_FOUND :
				throw new Exception("There is a fileNotFoundException in progress : " + message);
			default:
				throw new Exception(message);
			}
		}else {
			
		}
	}
}
