package com.guangrui.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * ��Ե���Entity����Ĳ�������?.�������ھ���ORMʵ�ַ���.
 * 
 * @author springside
 * 
 */
public interface IEntityDao<T> {

	T get(Serializable id);

	List<T> getAll();

	void save(Object o);

	void remove(Object o);

	void removeById(Serializable id);

	/**
	 * ��ȡEntity�����������?.
	 */
	@SuppressWarnings("rawtypes")
	String getIdName(Class clazz);
}