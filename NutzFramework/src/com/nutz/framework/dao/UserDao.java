package com.nutz.framework.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;

import com.nutz.framework.base.dao.BasicDao;
import com.nutz.framework.model.UserInfo;

@IocBean
@InjectName
public class UserDao extends BasicDao {
	
	/**
	 * 根据用户名查询用户集合返回
	 * @param userName
	 * @return
	 * @author Ajian
	 * @version 2012-7-16 下午3:04:49
	 */
	public List<UserInfo> queryUserByUserName(String userName){
		return dao.query(UserInfo.class, Cnd.where("username", "=", userName), null);
	}
}
