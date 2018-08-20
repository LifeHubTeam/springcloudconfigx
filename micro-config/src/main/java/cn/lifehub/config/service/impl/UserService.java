package cn.lifehub.config.service.impl;


import cn.lifehub.config.enums.RoleTypeEnum;
import cn.lifehub.config.enums.UserStateEnum;
import cn.lifehub.config.models.tables.QUser;
import cn.lifehub.config.models.tables.User;
import cn.lifehub.config.repository.UserRepository;
import cn.lifehub.config.service.BaseDslService;
import cn.lifehub.config.service.IUserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends BaseDslService implements IUserService{

    private static final String CREATE_TIME = "createTime";

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Integer userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public Page<User> findUserListByPage(RoleTypeEnum roleType, UserStateEnum state, String keyword, Integer page, Integer count) {
        QUser user = QUser.user;
        Pageable pageable = new PageRequest(page, count, Sort.Direction.ASC, CREATE_TIME);
        BooleanExpression bl = user.isNotNull();
        if (roleType != null){
            bl = bl.and(user.roleType.eq(roleType));
        }
        if (state != null){
            bl = bl.and(user.state.eq(state));
        }
        if (StringUtils.isNotBlank(keyword)){
            bl = bl.and(user.account.like("%" + keyword + "%").or(user.mobile.eq(keyword)));
        }
        return userRepository.findAll(bl, pageable);
    }
}
