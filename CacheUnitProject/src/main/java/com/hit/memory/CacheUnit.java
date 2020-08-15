package com.hit.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;



public class CacheUnit<T> extends Object {
	
	IAlgoCache<Long,DataModel<T>> algo;
	IDao<Long,DataModel<T>> dao;

	public CacheUnit(IAlgoCache<Long,DataModel<T>> algo, IDao<Long,DataModel<T>> dao) {
		this.algo = algo;
		this.dao = dao;
		
	}
	
	@SuppressWarnings("unchecked")
	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {
		int size = ids.length;
		DataModel<T> tmp;
		DataModel<T> dump;
		
		List<DataModel<T>> ret = new ArrayList<>();
		
		for(int i=0;i<size;i++) {
			tmp = algo.getElement(ids[i]);
			
			if(tmp != null){ 
				ret.add(tmp);
			}
			else {
				tmp = dao.find(ids[i]);
				
				if(tmp!= null) {
					dump = algo.putElement(ids[i], tmp);
					if(dump != null) {
						dao.delete(dump);
						dao.save(dump);
					}
					ret.add(tmp);
				}
				else {
					tmp = (DataModel<T>) new DataModel<String>(ids[i], "null");
					dao.save(tmp);
					ret.add(tmp);
				}
			}
		}
		
		return ret.toArray((DataModel<T>[]) new DataModel[size]);
	}
}
