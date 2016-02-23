/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午3:23:31  
 */
package me.chaoshoo.xservice.core.communication;

/**
 * @Title: Command.java 
 * @Package: me.chaoshoo.xservice.core.protocol 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月15日 下午3:23:31 
 * @version:  
 */
public enum Command {
	
	UNKNOWM(0),

	SERVICE_STATUS(100),
	
	SERVICE_LIST(101),
	
	PUBLISH_SERVICE(201),
	
	REMOVE_SERVICE(202),
	
	REQUEST_SERVICE(300),
	
	CALL_SERVICE(301),

	PREPARE_FILE(400),
	
	SEND_FILE(401),
	
	GET_FILE(402),
	
	UPDATE_SLAVE_INFO(600),
	
	RESPONSE(900);
	
	private int value = 0;

    private Command(int value) {    
        this.value = value;
    }

    public static Command valueOf(int value) {  
        switch (value) {
        case 0:
        	return UNKNOWM;
        case 100:
            return SERVICE_STATUS;
        case 101:
            return SERVICE_LIST;
        case 200:
            return PUBLISH_SERVICE;
        case 201:
            return REMOVE_SERVICE;
        case 300:
            return REQUEST_SERVICE;
        case 301:
            return CALL_SERVICE;
        case 400:
            return PREPARE_FILE;
        case 401:
            return SEND_FILE;
        case 402:
        	return GET_FILE;
        case 600:
        	return UPDATE_SLAVE_INFO;
        case 900:
            return RESPONSE;
        default:
            return UNKNOWM;
        }
    }

    public int value() {
        return this.value;
    }
}
