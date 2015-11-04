package com.guangrui.manager.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guangrui.hibernate.baseDao.BaseDao;
import com.guangrui.manager.Manager;
import com.guangrui.model.project.Project;
import com.guangrui.query.ProjectQuery;
import com.guangrui.util.StringUtil;

@Component
public class ProjectManager implements Manager {

	@Autowired
	private BaseDao<Project> projectDao;
	
	@Override
	public void save(Object object) {
		projectDao.save(object);
		projectDao.flush();
	}
	
	public Project findUniqueById(int id){
		return this.projectDao.get(id);
	}

	public Project findUniqueByName(String name){
		if(!StringUtil.isNotBlank(name)){
			return null;
		}	
		return this.projectDao.findUniqueBy("name", name.trim());
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> queryProjects(ProjectQuery query){
		Criteria criteria = this.projectDao.createCriteria(Project.class);
		if(query.getName()!=null){
			criteria.add(Restrictions.eq("name", query.getName()));
		}
		criteria.addOrder(Order.desc("id"));
		if(query.getPageSize()>0){
			criteria.setFirstResult(query.getStartItem()).setMaxResults(query.getPageSize());
		}
		int count = countProjects(query);
		query.setTotal(count);
		return criteria.list();
	}
	
	public int countProjects(ProjectQuery query){
		Criteria criteria = this.projectDao.createCriteria(Project.class);
		criteria.setProjection(Projections.rowCount());
		if(query.getName()!=null){
			criteria.add(Restrictions.eq("name", query.getName()));
		}
		return Integer.parseInt(criteria.uniqueResult().toString());
	}
}
