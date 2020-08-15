package com.hit.dm;

import java.io.Serializable;

public class DataModel<T> extends Object implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Long dataModelId;
	T content;

	public DataModel(Long dataModelId, T content) {
		this.dataModelId = dataModelId;
		this.content = content;
	} 
	
	@Override
	public int hashCode() {
		
		return dataModelId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(dataModelId.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		
		return "dataModelId:" + dataModelId.toString() + ",content:" + content.toString();
	}
	
	public Long getDataModelId() {
		
		return dataModelId;
	}
	
	public void setDataModelId(Long dataModelId) {
		this.dataModelId = dataModelId;
	}
	
	public T getContent() {
		
		return content;
	}
	
	public void setContent(T content) {
		this.content = content;
		
	}
}
