package org.aksw.sante.smile.core;
 
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
 
/**
 * Implementation of Entity LazyDataModel
 */
public class LazySearchDataModel extends LazyDataModel<AbstractEntityWrapper> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3598304890071784205L;
	String inputTextFilter = null;
	ILazySearchDataModel model;

	public LazySearchDataModel(ILazySearchDataModel model) {
		this.model = model;
	}	

    @Override
    public Object getRowKey(AbstractEntityWrapper entity) {
        return entity.getId();
    }

    @Override
    public int getRowCount() {
    	return model.getRowCount();
    }
    
    @Override
    public List<AbstractEntityWrapper> load(int first, int pageSize, String sortField, SortOrder sortOrder,
    		Map<String, FilterMeta> filters) {
    	try {
    		List<AbstractEntityWrapper> entities = model.load(first, pageSize, inputTextFilter, sortField, sortOrder, filters);
			return entities;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
//    @Override
//    public List<AbstractEntityWrapper> load(int first, int pageSize, String sortField,
//    		SortOrder sortOrder, Map<String, Object> filters) {
//    	try {
//    		List<AbstractEntityWrapper> entities = model.load(first, pageSize, inputTextFilter, sortField, sortOrder, filters);
//			return entities;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}    	
//    	return null;
//    }
    
    public String getInputSearchText() {
		return inputTextFilter;
	}
    
    public void setInputSearchText(String inputTextFilter) {
		this.inputTextFilter = inputTextFilter;
	}
}