package com.nutz.framework.base;

import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.nutz.framework.base.dao.BasicDao;

/**
 * 总模板类 <br>
 * 本类为整个应用的默认模块类。在这个类上，你可以：
 * <ul>
 * <li>通过 '@Modules' 注解声明整个应用有哪些模块
 * <li>通过 '@IocBy' 注解声明整个应用，应采用何种方式进行反转注入。如果没有声明，整个应用将不支持 Ioc
 * <li>通过 '@Localization' 注解声明整个应用的本地地化字符串的目录
 * <li>通过 '@SetupBy' 注解声明应用启动和关闭时，应该进行的处理。（通过 Setup 接口）
 * <li>通过 '@Ok' 注解声明整个应用默认的成功视图
 * <li>通过 '@Fail' 注解声明整个应用默认的失败视图
 * </ul>
 * @author Ajian
 * @version 2012-7-16 下午1:53:05
 */
@Fail("json")
@Modules(packages={"com.nutz.framework.action"})
// 复合加载器，ComboIocLoader 的构造函数是个字符串形变参数组，所有的参数，如果以星号 " *"开头，
//则被认为是加载器的类型，后面的参数都作为这个加载器构造函数的参数，直到遇到下一个星号 " *" 开头的参数。
@IocBy(type = ComboIocProvider.class, args = {
		"*org.nutz.ioc.loader.json.JsonLoader", "/conf/datasource.json",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader","com.nutz.framework" })
@SetupBy(BasicDao.class)
public class MainMdoule {

}
