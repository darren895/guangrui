<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看工作</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li class="active"><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
	<table>
		<tr >
			<td>员工姓名：
			</td>
			<td>${jobDTO.employeeName}
			</td>
		</tr>
		<tr>
			<td>项目名称：
			</td>
			<td>${jobDTO.projectName}
			</td>
		</tr>
		<tr>
			<td>工作日期：
			</td>
			<td>${jobDTO.date}
			</td>
		</tr>
		<tr>
			<td>正常工作时长（小时）：
			</td>
			<td>${jobDTO.workTime}
			</td>
		</tr>
		<tr>
			<td>加班时长（小时）：
			</td>
			<td>${jobDTO.overTime}
			</td>
		</tr>
		<tr>
			<td>周末时长（小时）：
			</td>
			<td>${jobDTO.weekendTime}
			</td>
		</tr>
	</table>
</body>
</html>