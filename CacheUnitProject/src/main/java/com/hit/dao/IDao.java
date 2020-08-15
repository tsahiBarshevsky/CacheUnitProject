package com.hit.dao;

import java.io.Serializable;

public interface IDao<ID extends Serializable,T> {
	void save(T entity);
	void delete(T entity);
	T find(ID id);

}
