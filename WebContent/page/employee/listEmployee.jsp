<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看员工</title>
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
<div>
<ul class="nav nav-tabs">
<li><a href="addEmployee">添加员工</a></li>
<li><a href="addProject">添加项目</a></li>
<li><a href="addJob">添加工作</a></li>
<li class="active"><a href="listEmployee">查看员工</a></li>
<li><a href="listProject">查看项目</a></li>
<li><a href="listJob">查看工作</a></li>
<li><a href="gridJob">查看工作(日历版)</a></li>
</ul>
</div>
<div>
	<form action="listEmployee" method="get">
		<p>
			姓名：
		</p>
		<input type="text" name="name" value="${name }">
		<button type="submit" class="btn">搜索</button>
	</form>
</div>
	<table class="table table-bordered table-hover table-striped">
		<thead>
			<tr>
				<th>
					姓名
				</th>
				<td>
					正常工作时长（小时）
				</td>
				<td>
					加班时长（小时）
				</td>
				<td>
					周末时长（小时）
				</td>
				<td>
					操作
				</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator var="employee" value="#employeeDTOs">
			<tr>
				<td>${employee.name}
			</td>
			<td>${employee.time}小时			
			</td>
			<td>
				${employee.overTime}小时		
			</td>
			<td>
				${employee.weekendTime}小时		
			</td>
			<td>
				<a class="btn" href="viewEmployee?id=${employee.id }">查看详情</a>
				<button class="btn inbtn" data-id="${employee.id}" data-name="${employee.name}">批量导入</button>
			</td>
			</tr>
			</s:iterator>
		</tbody>
	</table>
	<s:if test="#query.totalPage<=5">
		<s:iterator var="cp" begin="1" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listEmployee?name=${name }&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:if>
	<s:elseif test="#query.p>=#query.totalPage-2">
		<a href="/listEmployee?name=${name }&p=1">1</a>&nbsp;...
		<s:iterator var="cp" begin="#query.totalPage-4" end="#query.totalPage">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listEmployee?name=${name }&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
	</s:elseif>
	<s:elseif test="#query.p<=3">
		<s:iterator var="cp" begin="1" end="#query.p+2">
			<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listEmployee?name=${name }&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...
		<a href="/listEmployee?name=${name }&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:elseif>
	<s:else>
	<a href="/listEmployee?name=${name }&p=1">1</a>&nbsp;...
	<s:iterator var="cp" begin="#query.p-2" end="#query.p+2">
		<s:if test="#query.p==#cp">${cp }</s:if><s:else><a href="/listEmployee?name=${name }&p=${cp}">${cp}</a></s:else>&nbsp;
		</s:iterator>
		...<a href="/listEmployee?name=${name }&p=${query.totalPage }">${query.totalPage }</a>&nbsp;
	</s:else>
	
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;width: initial;left: 100px;margin-left: auto;">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>批量导入</h3>
  </div>
  <div class="modal-body">
  <input type="hidden" id="inid" value="">
    <p id="modalName"><p>
    <table class="table table-bordered table-hover table-striped">
    	<thead id="addHead">
    		<tr>
    		<th>
    			<button id="dateMin" data-date="${timeTable.get(0)}">&lt; </button>
    		</th>
    		<s:iterator var="time" value="timeTable" >
    			<th>
    				${time}
    			</th>
    		</s:iterator>
    		<th>
    			<button id="dateAdd" data-date="${timeTable.get((timeTable.size()-1))}">&gt; </button>
    		</th>
    		</tr>
    	</thead>
    	<tbody id="addBody">
			<tr>
			<td>
			</td>
			<s:iterator var="time" value="timeTable" >
    			<td>
    				项目名称:<input type="text" style="width:70px;" data-date="${time}" class="pn"><br>
    				工作时间:<input type="text" style="width:70px;" data-date="${time}" class="time"><br>
    				加班时间:<input type="text" style="width:70px;" data-date="${time}" class="otime"><br>
    				周末时间:<input type="text" style="width:70px;" data-date="${time}" class="wtime">
    			</td>
    		</s:iterator>			
			<td>
			</td>
			</tr>    	
    	</tbody>
    </table>
    
  </div>
  <div class="modal-footer">
  <button class="btn add">新增</button>
    <button class="btn"  data-dismiss="modal" aria-hidden="true">关闭</button>
    <button  id="saveBtn" class="btn btn-primary">保存</button>
  </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#myModal').modal('hide');
		var trhtml = $('#addBody').html();
		$('.inbtn').click(function(){
			var id = $(this).attr('data-id');
			$('#inid').val(id);
			var name = $(this).attr('data-name');
			$('#modalName').html(name);
			$('#myModal').modal('show');
		});
		$('#dateMin').live('click',function(){
			var date = $(this).attr('data-date');
			jQuery.ajax({
				type:'post',
				url:'/employee/changeTime',
				data:{'time':date,'add':false},
				dataType:'json',
				success:function(data){
					if(data.status){
						var timeTable = data.timeTable;
						var head = '<tr><th><button id="dateMin" data-date="'+timeTable[0]+'">&lt; </button></th>';
						var body = '<tr><td></td>';
						for ( var time in timeTable) {
							head += '<th>'+timeTable[time]+'</th>';
							body += '<td>项目名称:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="pn"><br>工作时间:<input style="width:70px;" type="text" data-date="'+timeTable[time]+'" class="time"><br>加班时间:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="otime"><br>周末时间:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="wtime"></td>';
						}
						head += '<th><button id="dateAdd" data-date="'+timeTable[timeTable.length-1]+'">&gt; </button></th></tr>';
						body += '<td></td></tr>';
						$('#addHead').html($(head));
						$('#addBody').html($(body));
						trhtml = body;
					}else{
						alert("失败");
					}
				}
			});
		});
		$('#dateAdd').live('click',function(){
			var date = $(this).attr('data-date');
			jQuery.ajax({
				type:'post',
				url:'/employee/changeTime',
				data:{'time':date,'add':true},
				dataType:'json',
				success:function(data){
					if(data.status){
						var timeTable = data.timeTable;
						var head = '<tr><th><button id="dateMin" data-date="'+timeTable[0]+'">&lt; </button></th>';
						var body = '<tr><td></td>';
						for ( var time in timeTable) {
							head += '<th>'+timeTable[time]+'</th>';
							body += '<td>项目名称:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="pn"><br>工作时间:<input style="width:70px;" type="text" data-date="'+timeTable[time]+'" class="time"><br>加班时间:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="otime"><br>周末时间:<input type="text" style="width:70px;" data-date="'+timeTable[time]+'" class="wtime"></td>';
						}
						head += '<th><button id="dateAdd" data-date="'+timeTable[timeTable.length-1]+'">&gt; </button></th></tr>';
						body += '<td></td></tr>';
						$('#addHead').html($(head));
						$('#addBody').html($(body));
						trhtml = body;
					}else{
						alert("失败");
					}
				}
			});
		});
		$('.add').live('click',function(){
			$('#addBody').append(trhtml);
		});
		$('#saveBtn').click(function(){
			var id = $('#inid').val();
			var td = $('#addBody td');
			var i = 0;
			var str = '{';
			td.each(function(){
				
				var pn = $(this).find('.pn').val();
				var time = $(this).find('.time').val();
				var otime = $(this).find('.otime').val();
				var wtime = $(this).find('.wtime').val();
				if(pn!=''&&pn!=undefined){
					str += '"jobDTOs['+i+'].employeeId":'+id+',';
				if(time==''){
					time = 0;
				}
				if(otime==''){
					otime = 0;
				}
				if(wtime==''){
					wtime = 0;
				}
				str += '"jobDTOs['+i+'].projectName":"' +pn+'",';
				str += '"jobDTOs['+i+'].workTime":' + time+',';
				str += '"jobDTOs['+i+'].overTime":' + otime+',';
				str += '"jobDTOs['+i+'].weekendTime":' + wtime+',';
				str += '"jobDTOs['+i+'].date":"' + $(this).find('.pn').attr('data-date')+'",';
				i++;
				}
			});
			str += '"t":1}';
			var testJson = $.parseJSON(str); 
			jQuery.ajax({
				type:'post',
				url:'/job/addJobs',
				data:testJson,
				dataType:'json',
				success:function(data){
					if(data.status){
						alert("成功");
					}else{
						alert(data.message);
					}
					
					location.reload(true);
				}
			});
		});
	});	
</script>
</body>
</html>