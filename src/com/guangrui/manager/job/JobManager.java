package com.guangrui.manager.job;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guangrui.hibernate.baseDao.BaseDao;
import com.guangrui.manager.Manager;
import com.guangrui.model.job.Job;
import com.guangrui.model.user.Employee;
import com.guangrui.query.JobQuery;

@Component
public class JobManager implements Manager {
	
	@Autowired
	private BaseDao<Job> jobDao;

	@Override
	public void save(Object object) {
		this.jobDao.save(object);
		jobDao.flush();
	}
	
	public Job findUniqueById(int id){
		return jobDao.get(id);
	}
	
	public void removeJob(Job job){
		this.jobDao.remove(job);
		this.jobDao.flush();
	}
	
	public void removeJobById(int id){
		this.jobDao.removeById(id);
		this.jobDao.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Job> queryJobByEmployeeWithDate(Employee employee,Date startDate,Date endDate){
		String hql = "from com.guangrui.model.job.Job job where job.employee = ? and job.workDate >= ? and job.workDate <= ?";
		return jobDao.createQuery(hql).setInteger(0, employee.getId()).setDate(1, startDate).setDate(2, endDate).list();
	}
	
	public double sumWorkTime(JobQuery query){
		Criteria criteria = jobDao.createCriteria(Job.class);
		criteria.setProjection(Projections.sum("workTime"));
		if(query.getEmployee()!=null){
			criteria.add(Restrictions.eq("employee", query.getEmployee()));
		}
		if(query.getProject()!=null){
			criteria.add(Restrictions.eq("project", query.getProject()));
		}
		if(query.getStartDate() !=null){
			criteria.add(Restrictions.ge("workDate", query.getStartDate()));
		}
		if(query.getEndDate() != null) {
			criteria.add(Restrictions.le("workDate", query.getEndDate()));
		}
		Double d = (Double) criteria.uniqueResult();
		if(d == null){
			d = 0d;
		}
		return d;
	}
	
	public double sumWeekendTime(JobQuery query){
		Criteria criteria = jobDao.createCriteria(Job.class);
		criteria.setProjection(Projections.sum("weekendTime"));
		if(query.getEmployee()!=null){
			criteria.add(Restrictions.eq("employee", query.getEmployee()));
		}
		if(query.getProject()!=null){
			criteria.add(Restrictions.eq("project", query.getProject()));
		}
		if(query.getStartDate() !=null){
			criteria.add(Restrictions.ge("workDate", query.getStartDate()));
		}
		if(query.getEndDate() != null) {
			criteria.add(Restrictions.le("workDate", query.getEndDate()));
		}
		Double d = (Double) criteria.uniqueResult();
		if(d == null){
			d = 0d;
		}
		return d;
	}
	
	public double sumOverTime(JobQuery query){
		Criteria criteria = jobDao.createCriteria(Job.class);
		criteria.setProjection(Projections.sum("overTime"));
		if(query.getEmployee()!=null){
			criteria.add(Restrictions.eq("employee", query.getEmployee()));
		}
		if(query.getProject()!=null){
			criteria.add(Restrictions.eq("project", query.getProject()));
		}
		if(query.getStartDate() !=null){
			criteria.add(Restrictions.ge("workDate", query.getStartDate()));
		}
		if(query.getEndDate() != null) {
			criteria.add(Restrictions.le("workDate", query.getEndDate()));
		}
		Double d = (Double) criteria.uniqueResult();
		if(d == null){
			d = 0d;
		}
		return d;
	}
	
	@SuppressWarnings("unchecked")
	public List<Job> queryJob(JobQuery query){
		String hql = "from com.guangrui.model.job.Job job where 1=1 ";
		if(query.getEmployee()!=null){
			hql += " and job.employee = :employee";
		}
		if(query.getProject()!=null){
			hql += " and job.project = :project";
		}
		if(query.getStartDate()!=null){
			hql += " and job.workDate >= :startDate";
		}
		if(query.getEndDate()!=null){
			hql += " and job.workDate <= :endDate";
		}
		if(query.getEmployeeIds()!=null &&query.getEmployeeIds().size()>0){
			hql += " and job.employee in (:employeeIds)";
		}
		String countHql = "select count(*) " + hql;
		hql +=" order by job.workDate desc";
		Query hqlQuery = jobDao.createQuery(hql);
		Query countQuery = jobDao.createQuery(countHql);
		if(query.getEmployee()!=null){
			hqlQuery.setInteger("employee", query.getEmployee().getId());
			countQuery.setInteger("employee", query.getEmployee().getId());
		}
		if(query.getProject()!=null){
			hqlQuery.setInteger("project", query.getProject().getId());
			countQuery.setInteger("project", query.getProject().getId());
		}
		if(query.getStartDate()!=null){
			hqlQuery.setDate("startDate", query.getStartDate());
			countQuery.setDate("startDate", query.getStartDate());
		}
		if(query.getEndDate()!=null){
			hqlQuery.setDate("endDate", query.getEndDate());
			countQuery.setDate("endDate", query.getEndDate());
		}
		if(query.getEmployeeIds()!=null &&query.getEmployeeIds().size()>0){
			hqlQuery.setParameterList("employeeIds", query.getEmployeeIds());
			countQuery.setParameterList("employeeIds", query.getEmployeeIds());
		}
		Long count = (Long) countQuery.uniqueResult();
		query.setTotal(count);
		if(query.getPageSize()!=0){
			hqlQuery.setMaxResults(query.getPageSize()).setFirstResult((int)query.getStartItem());
		}
		return hqlQuery.list();
	}

}
