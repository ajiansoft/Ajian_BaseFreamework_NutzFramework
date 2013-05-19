package com.nutz.framework.services;

import java.util.List;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.nutz.framework.base.exception.BusinessException;
import com.nutz.framework.dao.UserDao;
import com.nutz.framework.model.UserInfo;

@IocBean
@InjectName
public class UserServices {
	@Inject
	private UserDao userDao;
	
	/**
	 * 用户登录处理方法
	 * @param user
	 * @return
	 * @author Ajian
	 * @version 2012-7-16 下午3:01:31
	 * @throws Exception 
	 */
	public UserInfo login(UserInfo user) throws BusinessException{
		List<UserInfo> ul = userDao.queryUserByUserName(user.getUserName());
		if(ul==null || ul.size()==0){
			throw new BusinessException("此账号不存在，请查证！");
		}
		for (UserInfo u : ul) {
			if(u.getUserPass().equalsIgnoreCase(user.getUserPass())){
				return u;
			}
		}
		throw new BusinessException("密码错误！");
	}
}
