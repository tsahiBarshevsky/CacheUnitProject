package com.hit.memory;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImp;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

class CacheUnitTest {
	String test;

	CacheUnit<String> testRun;
	IAlgoCache<Long,DataModel<String>> algo;
	IDao<Long,DataModel<String>> dao;
	Long[] ids;
	DataModel<String>[] check;
	
	public CacheUnitTest() {
		algo = new LRUAlgoCacheImp<>(3);
		dao = new DaoFileImpl<String>("src/main/resource/datasource.txt");
		testRun = new CacheUnit<String>(algo, dao);
		ids = new Long[5];
		
		for(int i =0; i<5; i++) {
			ids[i] = new Long(i);
		}
	}
	
	@Test
	public void getDataModelsTest() {
		
		try {
			check = testRun.getDataModels(ids);
		} 
		catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException " + e);
		} 
		catch (IOException e) {
			System.out.println("IOException " + e);
		}
		
		Assert.assertEquals("[2, 3, 4] {2=dataModelId:2,content:bob, 3=dataModelId:3,content:ron, 4=dataModelId:4,content:null}", algo.toString());
		Assert.assertEquals("dataModelId:0,content:null", check[0].toString());
	}
}
