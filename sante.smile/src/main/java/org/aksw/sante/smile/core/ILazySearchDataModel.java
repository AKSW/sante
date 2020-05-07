package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

public interface ILazySearchDataModel extends Serializable {
	public int getRowCount();
	
	public List<AbstractEntityWrapper> load(int first, int pageSize, String inputSearchText, String sortField,
    		SortOrder sortOrder, Map<String, Object> filters);
	
	public long getRuntime();
}
