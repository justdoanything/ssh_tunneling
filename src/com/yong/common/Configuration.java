package com.yong.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.yaml.snakeyaml.Yaml;

import com.yong.msg.MsgCodeConfiguration;
import com.yong.util.CommonUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Configuration {

	private static final Logger logger =  Logger.getLogger(Configuration.class);
	private final static List<Map<String, Object>> envListMap = new ArrayList<>();
	
	/**
	 * @author yongwoo
	 * @throws Exception
	 * @category Configuration
	 * @implNote Initialize context file when this program started.
	 */
	public static void initialize() throws Exception {
			PropertyConfigurator.configure(MsgCodeConfiguration.MSG_VALUE_PATH_LOG4J);
			logger.info("Complete to load log4j properties file from " + MsgCodeConfiguration.MSG_VALUE_PATH_LOG4J);
			logger.info("Trying to parse application.yml to start this program!");
			parseApplicationYml();
			logger.info("Complete to load application.yml!");
			logger.info("application map : " + envListMap.toString());
	}
	
	/**
	 * @author yongwoo
	 * @throws FileNotFoundException, Exception
	 * @category Configuration
	 * @implNote Parseing application.yml to envListMap
	 *           (This is like @Value from SpringBoot)
	 */
	private static void parseApplicationYml() throws Exception {
		try {
			Map<String, Object> yml = new Yaml().load(new FileReader(MsgCodeConfiguration.MSG_VALUE_PATH_APPLICATION));
			List envListBean = (List) ((Map) yml.get(MsgCodeConfiguration.MSG_WORD_KEY_ENVIRONMENT)).get(MsgCodeConfiguration.MSG_WORD_KEY_ENVLIST);
			logger.info("There is " + envListBean.size() + " environment in application.yml");
			
			int index = 0;
			for(Object env : envListBean) {
				logger.info(MsgCodeConfiguration.MSG_WORD_KEY_ENV + index + " : " + env.toString());
				envListMap.add(new HashMap<>());
				createYmlToMap(env, envListMap.get(index));
				index++;
			}
			
		}catch (FileNotFoundException e) { 
			e.printStackTrace(); 
			logger.error("There is a fileNotFoundException in progress : " + e.toString());
			throw new FileNotFoundException(e.toString());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("There is a exception in progress : " + e.toString());
			throw new Exception(e);
		}
	}
	
	/**
	 * @author yongwoo
	 * @throws
	 * @category Configuration
	 * @implNote Create envListMap from application.yml
	 */
	private static void createYmlToMap(Object env, Map<String, Object> envListMap) throws Exception {
//		this.doDepthFirstSearch(env, CommonUtil.ObjToString(((Map) env).get(MsgCodeConfiguration.MSG_WORD_KEY_ENV)), envListMap);
		doDepthFirstSearch(env, "", envListMap);
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Create envListMap from application.yml with DFS searching argorithm 
	 */
	private static void doDepthFirstSearch(Object env, String parentKey, Map<String, Object> envListMap) throws Exception {
		Map<String, Object> obj = CommonUtil.ObjToMap(env);
		
		for(String key : obj.keySet()) {
			if(CommonUtil.mapOrNot(obj.get(key))) {
				// Create parentKey
				parentKey += (parentKey.equals("") ? key : "." + key);
				
				// Search child node
				doDepthFirstSearch(obj.get(key), parentKey, envListMap);
				
				// End to search child node
//				parentKey = parentKey.replace("." + key, "");
				parentKey = parentKey.contains("." + key) ? parentKey.replace("." + key, "") : parentKey.replace(key, "");
				
			} else {
				// End to search parent node
				String configKey = (parentKey.equals("") ? key : parentKey + "." + key);
				envListMap.put(configKey, obj.get(key));
				logger.info(configKey + " : " + obj.get(key).toString());
			}
		}
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Get String Object from envListMap 
	 */
	public static String getString(int index, String key) throws Exception {
		return CommonUtil.ObjToString(envListMap.get(index).get(key));
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Get List Object from envListMap 
	 */
	public static List getList(int index, String key) throws Exception {
		return CommonUtil.ObjToList(envListMap.get(index).get(key));
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Get Int Object from envListMap 
	 */
	public static int getInt(int index, String key) throws Exception {
		return CommonUtil.ObjToInt(envListMap.get(index).get(key));
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Get size of envListMap 
	 */
	public static int getEnvListSize() throws Exception {
		return envListMap.size();
	}
	
	/**
	 * @author yongwoo
	 * @throws 
	 * @category Configuration
	 * @implNote Get envMap from envListMap with index 
	 */
	public static Map getEnvMap(int index) throws Exception {
		return envListMap.get(index);
	}

}
