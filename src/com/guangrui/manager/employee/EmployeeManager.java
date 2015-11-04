package com.guangrui.manager.employee;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guangrui.hibernate.baseDao.BaseDao;
import com.guangrui.manager.Manager;
import com.guangrui.model.user.Employee;
import com.guangrui.query.EmployeeQuery;
import com.guangrui.util.StringUtil;

@Component
public class EmployeeManager implements Manager {
	
	@Autowired
	private BaseDao<Employee> employeeDao;

	@Override
	public void save(Object object) {
		try {
			employeeDao.save(object);
			employeeDao.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Employee findUniqueByName(String name){
		if(!StringUtil.isNotBlank(name)){
			return null;
		}
		return employeeDao.findUniqueBy("name", name.trim());
	}
	
	public Employee findUniqueById(int id){
		return employeeDao.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployees(){
		String hql = "from com.guangrui.model.user.Employee e order by id desc";
		return employeeDao.createQuery(hql).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Employee> queryEmployees(EmployeeQuery query){
		Criteria creiteria = employeeDao.createCriteria(Employee.class);
		Criteria countCreiteria = employeeDao.createCriteria(Employee.class);
		if(query.getName()!=null){
			creiteria.add(Restrictions.eq("name", query.getName()));
			countCreiteria.add(Restrictions.eq("name", query.getName()));
		}
		if(query.getIds() !=null && query.getIds().size() >0){
			creiteria.add(Restrictions.in("id", query.getIds()));
		}
		countCreiteria.setProjection(Projections.rowCount());
		long count = (Long) countCreiteria.uniqueResult();
		query.setTotal(count);
		if(query.getPageSize()>0){
			creiteria.setFirstResult(query.getStartItem()).setMaxResults(query.getPageSize());
			creiteria.addOrder(Order.desc("id"));
		}
		return creiteria.list();
	}

}
