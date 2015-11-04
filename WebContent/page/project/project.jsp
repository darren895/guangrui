<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${projectDTO.name}</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/datepicker.css" rel="stylesheet" media="screen">
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li   class="active"><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<table>
	<tr>
		<td>
			名称
		</td>
		<td>
			${projectDTO.name }
		</td>
	</tr>
	<tr>
		<td>
			正常工作时间：
		</td>
		<td>
			${projectDTO.time }
		</td>
	</tr>
	<tr>
		<td>
			加班工作时间：
		</td>
		<td>
			${projectDTO.overTime }
		</td>
	</tr>
</table>
统计时间：<input type="text" value="${startDate }" id="inputDateStart" name="startDate" class="lt date-input">
<span class="lt lh30">&nbsp;&nbsp;~&nbsp;&nbsp;</span>
<input type="text" value="${endDate }" id="inputDateEnd" name="endDate" class="lt date-input">
<input type="hidden" id="id" value="${projectDTO.id }">
<button type="button" id="search">查询</button>
<br>
<a href="javascript :;" onClick="javascript :history.back(-1);">返回</a>

	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>
					员工名称
				</th>
				<th>
					正常工作时长
				</th>
				<th>
					加班时长
				</th>
				<th>
					周末时长
				</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="employeeDTO" value="#projectDTO.employeeDTOs">
				<tr>
					<td>
						${employeeDTO.name }
					</td>
					<td>
						${employeeDTO.time }小时
					</td>
					<td>
						${employeeDTO.overTime }小时
					</td>
					<td>
						${employeeDTO.weekendTime }小时
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
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
<script type="text/javascript">
jQuery(function(){
	$('#search').click(function(){
		var id = $('#id').val();
		var startDate = $('#inputDateStart').val();
		var endDate = $('#inputDateEnd').val();
		window.location.href = '/viewProject?id='+id+'&startDate='+startDate+'&endDate='+endDate;
		
	})	
});
</script>
</body>
</html>