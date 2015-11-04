<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看工作</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/datepicker.css" rel="stylesheet" media="screen">
<style type="text/css">
	/* Special grid styles
-------------------------------------------------- */

.show-grid {
  margin-top: 10px;
  margin-bottom: 20px;
}
.show-grid [class*="span"] {
  background-color: #eee;
  text-align: center;
  -webkit-border-radius: 3px;
     -moz-border-radius: 3px;
          border-radius: 3px;
  min-height: 40px;
  line-height: 40px;
}
.show-grid [class*="span"]:hover {
  background-color: #ddd;
}
.show-grid .show-grid {
  margin-top: 0;
  margin-bottom: 0;
}
.show-grid .show-grid [class*="span"] {
  margin-top: 5px;
}
.show-grid [class*="span"] [class*="span"] {
  background-color: #ccc;
}
.show-grid [class*="span"] [class*="span"] [class*="span"] {
  background-color: #999;
}
</style>
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li class="active"><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<form action="">
<s:if test="#jobMap!=null">
	<table class="table table-bordered" style="width:;">
		<tr>
			<th>
				员工
			</th>
			<s:iterator var="time" value="#dates">
				<th style="width:100px">
					${time }
				</th>
			</s:iterator>
		</tr>
		<tbody>
			<s:iterator var="employee" value="#employeeDTOs">
				<tr>
					<td>
						${employee.name }
					</td>
					<s:iterator var="date" value="#dates">
						<td>
						<s:if test="#jobMap.get(#employee.id).get(#date)!=null">
							<s:iterator var="jobDTO" value="#jobMap.get(#employee.id).get(#date)">
									<div class="row-fluid show-grid">
									<div class="span12">项目：<span class="label">${jobDTO.projectName }</span>
									<br>
									正常工作：<span class="label">${jobDTO.workTime }</span>
									<br>
									加班时间：<span class="label">${jobDTO.overTime }</span>
									<br>
									周末时间：<span class="label">${jobDTO.weekendTime }</span>
									</div>
									</div>
							</s:iterator>
						</s:if>
						</td>
					</s:iterator>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
	<a class="btn" href="/gridJob">重新选择</a>
</form>
<script src="js/jquery.min.js"></script>
</body>
</html>