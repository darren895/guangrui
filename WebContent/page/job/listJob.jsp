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
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li class="active"><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<form action="/listJob" method="get">
员工名称：
<input type="text" name="e" value="${e }">&nbsp;&nbsp;
项目名称：
<input type="text" name="pn" value="${pn }">&nbsp;&nbsp;
<input type="text" value="${startDate }" id="inputDateStart" name="startDate" class="lt date-input">
<span class="lt lh30">&nbsp;~&nbsp;</span>
<input type="text" value="${endDate }" id="inputDateEnd" name="endDate" class="lt date-input">
<label style="display: inline;" for="all">是否分页</label><input type="checkbox" id="all" name="all" value="1" <s:if test="#all==1">checked="checked"</s:if> >
<button class="btn" type="submit">搜索</button>
</form>
<div>
<p>总共正常工作时间（小时）:${workTime }
</p>
<p>总共加班时间(小时)：${overTime }
</p>
</div>
</div>
	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>
					员工姓名
				</th>
				<th>
					项目名称
				</th>
				<th>
					工作日期
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
			<s:iterator var="jobDTO" value="#jobDTOs">
				<tr>
				<td>${jobDTO.employeeName}
				</td>
				<td>${jobDTO.projectName}
			</td>
			<td>${jobDTO.date}
			</td>
			<td>${jobDTO.workTime}
			</td>
			<td>${jobDTO.overTime}
			</td>
			<td>
				${jobDTO.weekendTime}
			</td>
			<td>
				<button class="btn btn-danger delbtn" data-id="${jobDTO.id }">删除</button>
			</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<s:if test="#query.totalPage<=5">
		<s:iterator var="cp" begin="1" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:if>
	<s:elseif test="#query.p>=#query.totalPage-3">
		<a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=1">1</a>&nbsp;...
		<s:iterator var="cp" begin="#query.totalPage-4" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:elseif>
	<s:elseif test="#query.p<=4">
		<s:iterator var="cp" begin="1" end="#query.p+2">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...
		<a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:elseif>
	<s:else>
	<a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=1">1</a>&nbsp;...
	<s:iterator var="cp" begin="#query.p-2" end="#query.p+2">
		<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...<a href="/listJob?e=${e }&pn=${pn}&startDate=${startDate}&endDate=${endDate}&all=${all}&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:else>
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
	$('.delbtn').click(function(){
		var id = $(this).attr('data-id');
		if(!confirm('是否是确认删除?')){
			return;
		}
		jQuery.ajax({
			type:'post',
			url:'job/removeJob',
			data:{'id':id},
			dataType:'json',
			success:function(data){
				if(data.status){
					alert("删除成功");
				}else{
					alert("删除失败");
					
				}
				location.reload(true);
			}
		});
	});
</script>
</body>
</html>