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
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li class="active"><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<form action="/viewGridJob" method="post">
<input type="text" value="${startDate }" id="inputDateStart" name="startDate" class="lt date-input">
<span class="lt lh30">&nbsp;~&nbsp;</span>
<input type="text" value="${endDate }" id="inputDateEnd" name="endDate" class="lt date-input">
	<table class="table table-bordered table-hover">
		<tr>
			<th>
			选择
			</th>
			<th>
			员工
			</th>
		</tr>
		<s:iterator var="employee" value="employeeDTOs">
			<tr>
				<td>
					<input type="checkbox" name="employeeId" value="${employee.id }">
				</td>
				<td>
					${employee.name }
				</td>
			</tr>
		</s:iterator>
	</table>
	<button type="submit">查看</button>
</form>
<script src="js/jquery.min.js"></script>
<script src="js/datepicker.js"></script>
<script type="text/javascript">
$('#inputDateStart').DatePicker({
	date: $('#inputDateStart').val(),
	current: $('#inputDateStart').val(),
	starts: 1,
	position: 'bottom',
	onBeforeShow: function(){
		$('#inputDateStart').DatePickerSetDate($('#inputDateStart').val(), true);
	},
	onChange: function(formated, dates){
		$('#inputDateStart').val(formated);
		//$('#inputDateStart').DatePickerHide();
	}
});

$('#inputDateEnd').DatePicker({
	date: $('#inputDateEnd').val(),
	current: $('#inputDateEnd').val(),
	starts: 1,
	position: 'bottom',
	onBeforeShow: function(){
		$('#inputDateEnd').DatePickerSetDate($('#inputDateEnd').val(), true);
	},
	onChange: function(formated, dates){
		$('#inputDateEnd').val(formated);
		$('#inputDateEnd').DatePickerHide();
	}
});
</script>
</body>
</html>