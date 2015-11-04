<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看工作</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/datepicker.css" rel="stylesheet" media="screen">
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li  class="active"><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<div>
<form action="/listProject" method="get">
项目名称：
<input type="text" name="pn" value="${pn }">&nbsp;&nbsp;
<button class="btn" type="submit">搜索</button>
</form>
<div>
</div>
</div>
	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>
					项目名称
				</th>
				<th>
					正常工作时长（小时）
				</th>
				<th>
					加班时长（小时）
				</th>
				<th>
					周末时长（小时）
				</th>
				<th>
					操作
				</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="projectDTO" value="#projectDTOs">
				<tr>
					<td>
						${projectDTO.name}
					</td>
					<td>
						${projectDTO.time}
					</td>
					<td>
						${projectDTO.overTime}
					</td>
					<td>
						${projectDTO.weekendTime}
					</td>	
					<td>
						<a class="btn" href="viewProject?id=${projectDTO.id}">查看详情</a>
					</td>	
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<s:if test="#query.totalPage<=5">
		<s:iterator var="cp" begin="1" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listProject?pn=${pn}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:if>
	<s:elseif test="#query.p>=#query.totalPage-3">
		<a href="/listProject?pn=${pn}&p=1">1</a>&nbsp;...
		<s:iterator var="cp" begin="#query.totalPage-4" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listProject?pn=${pn}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:elseif>
	<s:elseif test="#query.p<=4">
		<s:iterator var="cp" begin="1" end="#query.p+2">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listProject?pn=${pn}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...
		<a href="/listProject?pn=${pn}&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:elseif>
	<s:else>
	<a href="/listProject?pn=${pn}&p=1">1</a>&nbsp;...
	<s:iterator var="cp" begin="#query.p-2" end="#query.p+2">
		<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listProject?pn=${pn}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...<a href="/listProject?pn=${pn}&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:else>
<script src="js/jquery.min.js"></script>
<script src="js/datepicker.js"></script>
<script type="text/javascript">

</script>
</body>
</html>