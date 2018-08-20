package cn.lifehub.config.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class JacksonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		/**
		 * 默认非空不输出，时间格式
		 */
		//objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 将 Java 对象转为 JSON 字符串
	 */
	public static <T> String toJSON(T obj) {
		String jsonStr;
		try {
			jsonStr = objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("Java 转 JSON 出错！", e);
			throw new RuntimeException(e);
		}
		return jsonStr;
	}

	/**
	 * 将 JSON 字符串转为 Java 对象
	 */
	public static <T> T fromJSON(String json, Class<T> type) {
		T obj;
		try {
			obj = objectMapper.readValue(json, type);
		} catch (Exception e) {
			logger.error("JSON 转 Java 出错！", e);
			throw new RuntimeException(e);
		}
		return obj;
	}

	public static <T> List<T> toList(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	/*
	 * public static void main(String[] args) { Objson obj = new Objson();
	 * obj.setAge(1000); obj.setBirthday(new Date()); obj.setName("jason");
	 * System.out.println(JacksonUtil.toJSON(obj)); }
	 * 
	 * static class Objson { private String name; private Integer age; private
	 * Date birthday;
	 * 
	 * public Objson() { super(); }
	 * 
	 * public Objson(String name, Integer age, Date birthday) { super();
	 * this.name = name; this.age = age; this.birthday = birthday; }
	 * 
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 * 
	 * public Integer getAge() { return age; }
	 * 
	 * public void setAge(Integer age) { this.age = age; }
	 * 
	 * @JsonFormat(pattern = "yyyy-MM-dd") public Date getBirthday() { return
	 * birthday; }
	 * 
	 * public void setBirthday(Date birthday) { this.birthday = birthday; }
	 * 
	 * }
	 */
}