package cn.lifehub.config.service;

import java.util.List;

import cn.lifehub.config.models.AppLabelInfo;
import cn.lifehub.config.models.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

//@Service
public class ConfigServiceback {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ConfigInfo> find(String application, String profile, String label) {
		String sql = "select P_KEY as pkey, P_VALUE as pvalue from T_PROPERTIES where P_APPLICATION = ? AND P_PROFILE = ?";
		Object[] params = null;
		if (!StringUtils.isEmpty(label)) {
			sql = sql + " AND P_LABEL = ?";
			params = new Object[] { application, profile, label };
		} else {
			params = new Object[] { application, profile };
		}
		List<ConfigInfo> list = jdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper<ConfigInfo>(ConfigInfo.class));
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	public List<String> getAllProfile() {
		String sql = "select P_PROFILE from T_PROPERTIES group by P_PROFILE";
		List<String> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<String>(String.class));
		return list;
	}

	public List<String> getAllApplication() {
		String sql = "select P_APPLICATION from T_PROPERTIES group by P_APPLICATION";
		List<String> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<String>(String.class));
		return list;
	}

	public List<String> getAllLabel() {
		String sql = "select P_Label from T_PROPERTIES group by P_Label";
		List<String> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<String>(String.class));
		return list;
	}

	public List<AppLabelInfo> findAllApplicationByProfile(String profile) {
		String sql = "select P_APPLICATION as application, P_LABEL as label from T_PROPERTIES where P_PROFILE = ?";
		List<AppLabelInfo> list = jdbcTemplate.query(sql, new Object[] { profile },
				new BeanPropertyRowMapper<AppLabelInfo>(AppLabelInfo.class));
		return list;
	}

}
