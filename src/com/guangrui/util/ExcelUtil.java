package com.guangrui.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.guangrui.dto.project.ProjectDTO;
import com.guangrui.dto.user.EmployeeDTO;

public class ExcelUtil {
	
	public static ByteArrayOutputStream getEmployeeExcel(EmployeeDTO employeeDTO){
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet(employeeDTO.getName());
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("项目名称");
			cell = row.createCell(1);
			cell.setCellValue("正常工作时长");
			cell = row.createCell(2);
			cell.setCellValue("加班工作时长");
			cell = row.createCell(3);
			cell.setCellValue("周末工作时长");
			if(employeeDTO != null && employeeDTO.getProjectDTOs() != null && !employeeDTO.getProjectDTOs().isEmpty()){
				int i = 1;
				for (ProjectDTO projectDTO : employeeDTO.getProjectDTOs()) {
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(projectDTO.getName());
					cell = row.createCell(1);
					cell.setCellValue(projectDTO.getTime());
					cell = row.createCell(2);
					cell.setCellValue(projectDTO.getOverTime());
					cell = row.createCell(3);
					cell.setCellValue(projectDTO.getWeekendTime());
				}
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			workbook.write(os);
			return os;
		} catch (Exception e) {
			return null;
		} finally {
			if(workbook !=null){
				try {
					workbook.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
