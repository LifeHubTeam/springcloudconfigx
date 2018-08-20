package cn.lifehub.config.models.json;

public class ProfileNameJson {
	public String profileName;
	public Integer profileValue;

	public ProfileNameJson() {
		super();
	}

	public ProfileNameJson(Integer profileValue, String profileName) {
		super();
		this.profileName = profileName;
		this.profileValue = profileValue;
	}

}
