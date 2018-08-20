package cn.lifehub.config.controller;

import cn.lifehub.config.constants.RedisConstant;
import cn.lifehub.config.enums.RoleTypeEnum;
import cn.lifehub.config.enums.UserStateEnum;
import cn.lifehub.config.models.BasePageResult;
import cn.lifehub.config.models.BaseResult;
import cn.lifehub.config.models.json.AuthUserJson;
import cn.lifehub.config.models.tables.User;
import cn.lifehub.config.service.IUserService;
import cn.lifehub.config.utils.JacksonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult<AuthUserJson> login(@RequestBody LoginParam param) {
        log.info("login account={}", param.account);
        if (StringUtils.isBlank(param.account)){
            return BaseResult.parameterError("登录账号不能为空");
        }
        if (StringUtils.isBlank(param.password)){
            return BaseResult.parameterError("密码不能为空");
        }
        User user = userService.findByAccount(param.account);
        if (user == null){
            return BaseResult.parameterError("账号不存在");
        }
        if (user.getState() == UserStateEnum.FROZEN){
            return BaseResult.parameterError("该账号已被冻结");
        }
        if (!user.getPassword().equals(param.password)){
            return BaseResult.parameterError("账号或密码输入有误");
        }
        String token = RandomStringUtils.randomAlphanumeric(100) + user.getId();
        AuthUserJson authUserJson = new AuthUserJson(token, user.getId(), user.getAccount(), user.getMobile(),user.getName(),
                user.getEmail(), null, user.getState(), user.getRoleType(), user.getHeadImage(), user.getCreateTime(), user.getUpdateTime());
        //缓存登录用户信息
        redisTemplate.opsForValue().set(RedisConstant.CONFIG_AUTH_USER_KEY + token, authUserJson, 60, TimeUnit.MINUTES);

        return BaseResult.success(authUserJson);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BaseResult<String> logout() {
        String token = super.getToken();
        log.info("logout param:{}", token);
        redisTemplate.delete(RedisConstant.CONFIG_AUTH_USER_KEY + token);
        return BaseResult.success("注销成功");
    }

    @RequestMapping(value = "/listUser", method = RequestMethod.POST)
    public BasePageResult<List<AuthUserJson>> listUser(@RequestBody UserListParam param) {
        log.info("listUser param:{}", JacksonUtils.toJSON(param));
        AuthUserJson authUser = super.getAuthUser();
        if (authUser.getRoleType() != RoleTypeEnum.SUPER_ADMIN){
            return BasePageResult.parameterError("权限不足");
        }
        if (param.page == null || param.page < 0 || param.count == null || param.count < 1){
            return BasePageResult.parameterError("分页参数有误");
        }
        List<AuthUserJson> jsonList = new ArrayList<>();
        Page<User> userPage = userService.findUserListByPage(param.roleType, param.state, param.keyword, param.page, param.count);
        for (User user : userPage){
            jsonList.add(new AuthUserJson(null, user.getId(), user.getAccount(), user.getMobile(),user.getName(), user.getEmail(),
                    user.getPassword(), user.getState(), user.getRoleType(), user.getHeadImage(), user.getCreateTime(), user.getUpdateTime()));
        }
        return BasePageResult.success(jsonList, param.page, userPage.getTotalPages(), userPage.getTotalElements());
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public BaseResult<String> addUser(@RequestBody UserParam param) {
        log.info("addUser account:{}, mobile:{}, name:{}, email:{}, state:{}, roleType:{}, headImage:{}",
                param.account, param.mobile, param.name, param.email, param.state, param.roleType, param.headImage);
        AuthUserJson authUser = super.getAuthUser();
        if (authUser.getRoleType() != RoleTypeEnum.SUPER_ADMIN){
            return BaseResult.parameterError("权限不足");
        }
        if (StringUtils.isBlank(param.account) || StringUtils.isBlank(param.mobile) || StringUtils.isBlank(param.name)
                || StringUtils.isBlank(param.password) || param.state == null || param.roleType == null){
            return BaseResult.parameterError();
        }
        User oldUser = userService.findByAccount(param.account);
        if (oldUser != null){
            return BaseResult.parameterError("该账号已存在");
        }
        User user = new User();
        user.setAccount(param.account);
        user.setMobile(param.mobile);
        user.setName(param.name);
        user.setEmail(param.email);
        user.setPassword(param.password);
        user.setState(param.state);
        user.setRoleType(param.roleType);
        user.setHeadImage(param.headImage);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userService.save(user);
        return BaseResult.success("保存成功");
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public BaseResult<String> updateUser(@RequestBody UserParam param) {
        log.info("updateUser userId:{}, account:{}, mobile:{}, name:{}, email:{}, state:{}, roleType:{}, headImage:{}",
                param.userId, param.account, param.mobile, param.name, param.email, param.state, param.roleType, param.headImage);
        AuthUserJson authUser = super.getAuthUser();
        if (authUser.getRoleType() != RoleTypeEnum.SUPER_ADMIN){
            return BaseResult.parameterError("权限不足");
        }
        if (param.userId == null || StringUtils.isBlank(param.account) || StringUtils.isBlank(param.mobile) || StringUtils.isBlank(param.name)
                || StringUtils.isBlank(param.password) || param.state == null || param.roleType == null){
            return BaseResult.parameterError();
        }
        User user = userService.findById(param.userId);
        if (user == null){
            return BaseResult.parameterError("用户id不存在");
        }
        User oldUser = userService.findByAccount(param.account);
        if (oldUser != null && !user.getId().equals(oldUser.getId())){
            return BaseResult.parameterError("该账号已存在");
        }
        user.setAccount(param.account);
        user.setMobile(param.mobile);
        user.setName(param.name);
        user.setEmail(param.email);
        user.setState(param.state);
        user.setRoleType(param.roleType);
        user.setHeadImage(param.headImage);
        user.setUpdateTime(new Date());
        if (!param.password.equals(user.getPassword())) {
            user.setPassword(param.password);
        }
        userService.save(user);
        return BaseResult.success("保存成功");
    }

    @RequestMapping(value = "/enableUser", method = RequestMethod.POST)
    public BaseResult<String> enableUser(@RequestBody UserIdParam param) {
        log.info("enableUser param:{}", JacksonUtils.toJSON(param));
        AuthUserJson authUser = super.getAuthUser();
        if (authUser.getRoleType() != RoleTypeEnum.SUPER_ADMIN){
            return BaseResult.parameterError("权限不足");
        }
        if (param.userId == null){
            return BaseResult.parameterError("用户id不能为空");
        }
        User user = userService.findById(param.userId);
        if (user == null){
            return BaseResult.parameterError("用户id不存在");
        }
        user.setState(UserStateEnum.NORMAL);
        user.setUpdateTime(new Date());
        userService.save(user);
        return BaseResult.success("解冻成功");
    }

    @RequestMapping(value = "/frozenUser", method = RequestMethod.POST)
    public BaseResult<String> frozenUser(@RequestBody UserIdParam param) {
        log.info("frozenUser param:{}", JacksonUtils.toJSON(param));
        AuthUserJson authUser = super.getAuthUser();
        if (authUser.getRoleType() != RoleTypeEnum.SUPER_ADMIN){
            return BaseResult.parameterError("权限不足");
        }
        if (param.userId == null){
            return BaseResult.parameterError("用户id不能为空");
        }
        User user = userService.findById(param.userId);
        if (user == null){
            return BaseResult.parameterError("用户id不存在");
        }
        user.setState(UserStateEnum.FROZEN);
        user.setUpdateTime(new Date());
        userService.save(user);
        return BaseResult.success("冻结成功");
    }

    public static class UserIdParam {
        public Integer userId;
    }

    public static class LoginParam {
        public String account;
        public String password;
    }

    public static class UserParam {
        public Integer userId;
        public String account;
        public String mobile;
        public String name;
        public String email;
        public String password;
        public UserStateEnum state;
        public RoleTypeEnum roleType;
        public String headImage;
    }

    public static class UserListParam {
        public String keyword;
        public UserStateEnum state;
        public RoleTypeEnum roleType;
        public Integer page;
        public Integer count;
    }
}
