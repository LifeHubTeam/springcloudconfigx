package cn.lifehub.config.models.tables;

import cn.lifehub.config.enums.AppStateEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_properties_history")
public class AppPropertiesHistory extends BaseTable implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer oldId;
	private Integer appId;
	private String key;
	private String value;
	private Boolean deleted;
	private String describle;
	private Integer modifyManId;
	private String modifyMan;
	private AppStateEnum state;

	public AppPropertiesHistory() {
		super();
	}

	public AppPropertiesHistory(Integer oldId, Integer appId, String key, String value, Boolean deleted) {
		super();
		this.oldId = oldId;
		this.appId = appId;
		this.key = key;
		this.value = value;
		this.deleted = deleted;
	}

	public AppPropertiesHistory(Integer oldId, Integer appId, String key, String value, Boolean deleted,
			String describle, String modifyMan) {
		super();
		this.oldId = oldId;
		this.appId = appId;
		this.key = key;
		this.value = value;
		this.deleted = deleted;
		this.describle = describle;
		this.modifyMan = modifyMan;
	}

	@Column(name = "old_id", nullable = false)
	public Integer getOldId() {
		return oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	@Column(name = "app_id", nullable = false)
	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	@Column(name = "p_key", length = 200, nullable = false)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "p_value", length = 2048, nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "is_deleted", length = 200, nullable = false)
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(name = "describ", length = 200, nullable = true)
	public String getDescrible() {
		return describle;
	}

	public void setDescrible(String describle) {
		this.describle = describle;
	}

	@Column(name = "modify_man_id", nullable = false)
	public Integer getModifyManId() {
		return modifyManId;
	}

	public void setModifyManId(Integer modifyManId) {
		this.modifyManId = modifyManId;
	}

	@Column(name = "modify_man", length = 200, nullable = false)
	public String getModifyMan() {
		return modifyMan;
	}

	public void setModifyMan(String modifyMan) {
		this.modifyMan = modifyMan;
	}

	@Column(name = "state", nullable = false)
	public AppStateEnum getState() {
		return state;
	}

	public void setState(AppStateEnum state) {
		this.state = state;
	}
}
