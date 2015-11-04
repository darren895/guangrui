<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${employeeDTO.name}</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/datepicker.css" rel="stylesheet" media="screen">
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li class="active"><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
	<table>
		<tr >
			<td>姓名：
			</td>
			<td>${employeeDTO.name}
			</td>
		</tr>
		<tr>
			<td>工作时长：
			</td>
			<td>${employeeDTO.time}小时 &nbsp;
			
			</td>
		</tr>
		<tr>
			<td>加班时长：
			</td>
			<td>${employeeDTO.overTime}小时 &nbsp;
			
			</td>
		</tr>
		<tr>
			<td>周末时长：
			</td>
			<td>${employeeDTO.weekendTime}小时 &nbsp;
			
			</td>
		</tr>
	</table>
	<a href="javascript :;" onClick="javascript :history.back(-1);">返回</a><br>
	统计时间：<input type="text" value="${startDate }" id="inputDateStart" name="startDate" class="lt date-input">
<span class="lt lh30">&nbsp;&nbsp;~&nbsp;&nbsp;</span>
<input type="text" value="${endDate }" id="inputDateEnd" name="endDate" class="lt date-input">
<input type="hidden" id="id" value="${employeeDTO.id }">
<button class="btn" type="button" id="search">查询</button> &nbsp;&nbsp;
<a target="_blank" class="btn" href="/employeeDownlowd?id=${employeeDTO.id }&startDate=${startDate }&endDate=${endDate }">导出Excel</a>
	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>
					项目名称
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
			<s:iterator var="projectDTO" value="#employeeDTO.projectDTOs">
				<tr>
					<td>
						${projectDTO.name }
					</td>
					<td>
						${projectDTO.time }小时
					</td>
					<td>
						${projectDTO.overTime }小时
					</td>
					<td>
						${projectDTO.weekendTime }小时
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
		window.location.href = '/viewEmployee?id='+id+'&startDate='+startDate+'&endDate='+endDate;
		
	})	
});
</script>
</body>
</html>