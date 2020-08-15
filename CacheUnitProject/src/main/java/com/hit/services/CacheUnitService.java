package com.hit.services;

import java.util.ArrayList;
import java.util.List;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImp;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.server.Request;

public class CacheUnitService<T> extends Object {

	CacheUnit<T> cache;
	IAlgoCache<Long,DataModel<T>> algo;
	IDao<Long,DataModel<T>> dao;
	
	Request<T> request;
	
	public CacheUnitService() {
		algo = new LRUAlgoCacheImp<>(3);
		dao = new DaoFileImpl<>("src/main/resource/datasource.txt");
		cache = new CacheUnit<>(algo, dao);
	}
	
	public boolean update(DataModel<T>[] dataModels) {
		boolean ret = false;
		DataModel<T> tmp = null;
		List<Long> ids = new ArrayList<>();
		int size;
		size = dataModels.length;
		for(int i=0;i<size;i++) {
			ids.add(dataModels[i].getDataModelId());
		}
		
			for(int i=0;i<size;i++) {
				tmp = algo.getElement(ids.get(i));
				if(tmp != null) {
					tmp.setContent(dataModels[i].getContent());
					algo.putElement(ids.get(i), tmp);
					ret = true;
				}
				else {
					tmp = dao.find(ids.get(i));
					if(tmp != null) {
						dao.delete(tmp);
						tmp.setContent(dataModels[i].getContent());
						dao.save(tmp);
						ret = true;
					}
					else {
						tmp = new DataModel<T>(ids.get(i), dataModels[i].getContent());
						dao.save(tmp);
						ret = true;
					}
				}
			}
		return ret;
	}
	
	public boolean delete(DataModel<T>[] dataModels) {
		boolean ret = false;
		DataModel<T> tmp = null;
		List<Long> ids = new ArrayList<>();
		int size;
		size = dataModels.length;
		for(int i=0;i<size;i++) {
			ids.add(dataModels[i].getDataModelId());
		}
			for(int i=0;i<size;i++) {
				tmp = algo.getElement(ids.get(i));
				if(tmp != null) {
					algo.removeElement(ids.get(i));
					ret = true;
				}
				else {
					tmp = dao.find(ids.get(i));
					if(tmp != null) {
						dao.delete(tmp);
						ret = true;
					}
				}
			}
		
		return ret;
	}
	
	public DataModel<T>[] get(DataModel<T>[] dataModels){
		DataModel<T>[] ret = null;
		List<Long> ids = new ArrayList<>();
		int size;
		size = dataModels.length;
		for(int i=0;i<size;i++) {
			ids.add(dataModels[i].getDataModelId());
		}
		
		try {
			ret = cache.getDataModels(ids.toArray((Long[]) new Long[size]));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}
