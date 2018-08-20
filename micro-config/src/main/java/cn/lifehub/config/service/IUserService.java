package cn.lifehub.config.service;

import cn.lifehub.config.enums.RoleTypeEnum;
import cn.lifehub.config.enums.UserStateEnum;
import cn.lifehub.config.models.tables.User;
import org.springframework.data.domain.Page;

public interface IUserService {

    User save(User user);

    User findById(Integer userId);

    User findByAccount(String account);

    Page<User> findUserListByPage(RoleTypeEnum roleType, UserStateEnum state, String keyword, Integer page, Integer count);
}
