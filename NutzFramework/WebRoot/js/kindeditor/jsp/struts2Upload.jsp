<%@ page language="java" pageEncoding="UTF-8"%>   
<%@page import="com.ssh.framework.Userinfo,java.io.*, org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper,java.util.concurrent.locks.*"%>
<%@page import="com.hw.ssmell.comp.constant.CompanyFinal"%>
<%   
	//Struts2  请求 包装过滤器   
	MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;   
	// 获得上传的文件名    
	String fileName = wrapper.getFileNames("imgFile")[0];   
	//获得未见过滤器    
	File file = wrapper.getFiles("imgFile")[0];   
	//----------- 重新构建上传文件名----------------------   
	final Lock lock = new ReentrantLock();   
		String newName = null;   
		lock.lock();   
	try {   
		//加锁为防止文件名重复    
		newName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."),fileName.length());   
	}finally {   
		lock.unlock();   
	}  
	//------------ 锁结束 -------------
	session.setAttribute("userName","tempDir");
	String userName=session.getAttribute("userName").toString();
	Object obj=session.getAttribute("loginUserBean");
	if(obj!=null){
		Userinfo user=(Userinfo)obj;
		userName=user.getRegLogName();
	}
	//目标路径
	String destPath= request.getSession().getServletContext().getRealPath("/")+ CompanyFinal.COMPANY_DATA_PATH+userName+"/kindeditor/";
	//检查文件目录是否存在，如果不存在,创建目录
	String [] pathList=destPath.split("/");
	String tempDir=pathList[0];
	for(int i=1;i<pathList.length;i++){
		tempDir+="/"+pathList[i];
		java.io.File myPath=new java.io.File(tempDir);
		if(!myPath.exists()){
			myPath.mkdir();
		}
	}
	//获取文件输出流    
	FileOutputStream fos = new FileOutputStream(destPath+"\\" + newName);  
	//设置 KE 中的图片文件地址    
	String newFileName = request.getScheme() + "://"  
		+ request.getServerName() + ":" + request.getServerPort()   
		+ request.getContextPath() +"/"+ CompanyFinal.COMPANY_DATA_PATH+userName+"/kindeditor/"+ newName;   
	byte[] buffer = new byte[1024];   
	//获取内存中当前文件输入流    
	InputStream in = new FileInputStream(file);   
	try {   
		int num = 0;   
		while ((num = in.read(buffer)) > 0) {   
			fos.write(buffer, 0, num);   
		}   
	} catch (Exception e) {   
		e.printStackTrace(System.err);   
	} finally {   
		in.close();   
		fos.close();   
	}   
	//发送给KE    
	out.println("<html><head><title>Insert Image</title><meta http-equiv='content-type' content='text/html; charset=gbk'/></head><body>");   
	out.println("<script type='text/javascript'>");   
	out.println("parent.parent.KE.plugin['image'].insert('"  
		+ wrapper.getParameter("id") + "','" + newFileName + "','"  
		+ wrapper.getParameter("imgTitle") + "','"  
		+ wrapper.getParameter("imgWidth") + "','"  
		+ wrapper.getParameter("imgHeight") + "','"  
		+ wrapper.getParameter("imgBorder") + "','"  
		+ wrapper.getParameter("align") + "');</script>");   
	out.println("</body></html>");   
%>  
