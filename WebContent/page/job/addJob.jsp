<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加员工</title>
<link href="css/datepicker.css" rel="stylesheet" media="screen">
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
<form action="saveJob" method="post">
<p>
员工姓名
</p>
<input type="text" id="name">
<br>
<p>
项目名称
</p>
<input type="text" id="projectName">
<br>
<p>工作日期
</p>
<input type="text" value="" id="inputDateStart" class="lt date-input">
<p>
正常工作时间(小时):
</p>
<input type="text" id="time">
<br>
<p>
加班时间(小时)：
</p>
<input type="text" id="overTime">
<br>
<p>周末时间(小时)</p>
<input type="text" id="weekendTime">
<button type="button" id="subJob">提交</button>
<a href="/">返回</a>
</form>
</body>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
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
</script>
<script type="text/javascript">
jQuery(function(){
	$('#subJob').click(function(){
		var jobDTO = {};
		var employeeName = $('#name').val();
		var date = $('#inputDateStart').val();
		var projectName = $('#projectName').val();
		var workTime = $('#time').val();
		var overTime = $('#overTime').val();
		var weekendTime = $('#weekendTime').val();
		jQuery.ajax({
			type:'post',
			url:'job/saveJob',
			data:{'jobDTO.employeeName':employeeName,'jobDTO.date':date,'jobDTO.projectName':projectName,'jobDTO.workTime':workTime,'jobDTO.overTime':overTime,'jobDTO.weekendTime':weekendTime},
			dataType:'json',
			success:function(data){
				if(data.status){
					alert("保存成功");
					window.location.href = '/viewJob?id='+data.id;
				}else{
					alert(data.error);
					location.reload(true);
				}
			}
		});
	});
});
</script>
</html>