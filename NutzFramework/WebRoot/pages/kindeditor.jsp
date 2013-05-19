<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>kindeditor</title>
    
    <script type="text/javascript" src="<%=basePath%>js/kindeditor/kindeditor.js"></script>
    <script type="text/javascript">
    	KE.show({
    		id:"editorArea"
    	})
    </script>
  </head>
  
  <body>
	<form action="#" method="post">
		<textarea rows="10" cols="100" id="editorArea"></textarea>
	</form>
  </body>
</html>
