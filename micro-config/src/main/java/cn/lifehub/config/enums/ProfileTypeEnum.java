package cn.lifehub.config.enums;

/**
 * @author zhuct
 * @Title
 * @Description
 * @date 2018/6/22 15:44
 */
public enum ProfileTypeEnum {
    DEV("dev"),
    TEST("test"),
    BETA("beta"),
    EXP("exp"),
    PROD("prod");
    private String msg;
    private ProfileTypeEnum(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }

}
