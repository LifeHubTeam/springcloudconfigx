package cn.lifehub.config.models.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.enums.ProfileTypeEnum;

@Entity
@Table(name = "t_app")
public class App extends BaseTable implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appName;
	private String appProfile;
	private String appLabel;
	private AppStateEnum appState;
	private String appVersion;
	private String appDesc;
	private ProfileTypeEnum profileType;

	public App() {
		super();
	}

	public App(String appName, String appProfile, String appLabel) {
		super();
		this.appName = appName;
		this.appProfile = appProfile;
		this.appLabel = appLabel;
	}

	public App(String appName, String appProfile, String appLabel, AppStateEnum appState, String appVersion,
			String appDesc) {
		super();
		this.appName = appName;
		this.appProfile = appProfile;
		this.appLabel = appLabel;
		this.appState = appState;
		this.appVersion = appVersion;
		this.appDesc = appDesc;
	}

	@Column(name = "app_name", length = 200, nullable = false)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "app_profile", length = 200, nullable = false)
	public String getAppProfile() {
		return appProfile;
	}

	public void setAppProfile(String appProfile) {
		this.appProfile = appProfile;
	}

	@Column(name = "app_label", length = 200, nullable = true)
	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	@Column(name = "app_state", length = 4, nullable = false)
	public AppStateEnum getAppState() {
		return appState;
	}

	public void setAppState(AppStateEnum appState) {
		this.appState = appState;
	}

	@Column(name = "app_version", length = 100, nullable = true)
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Column(name = "app_desc", length = 100, nullable = true)
	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	@Column(name = "profile_type")
	public ProfileTypeEnum getProfileType() {
		return profileType;
	}

	public void setProfileType(ProfileTypeEnum profileType) {
		this.profileType = profileType;
	}
}
