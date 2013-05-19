package com.nutz.framework.action;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.nutz.framework.base.exception.BusinessException;
import com.nutz.framework.model.UserInfo;
import com.nutz.framework.services.UserServices;

@IocBean
public class UserAction {
	@Inject
	private UserServices userServices;
	
	
	/**
	 * 登录action
	 * @author Ajian
	 * @version 2012-7-16 下午2:54:53
	 * @throws BusinessException 
	 */
	@At("/login")//访问路径
	@Ok("redirect:/pages/index.jsp")//成功，重定向视图
	@Fail("forward:/login.jsp")//失败，请求转发到登录页面
	public void userLogin(HttpServletRequest request,@Param("::user.")UserInfo user) throws BusinessException{
		if(user==null || Strings.isEmpty(user.getUserName()) || Strings.isEmpty(user.getUserPass())){
			throw new BusinessException("输入信息不完整！");
		}
		//调用方法登录
		user = userServices.login(user);
		
		//保存
		request.getSession().setAttribute("loginUser", user);
	}
}
