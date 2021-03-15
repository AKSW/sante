package org.aksw.sante.smile.view;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
@Named
@FacesConverter(value = "suggestionConverter", managed = true)
public class SuggestionConverter implements Converter<AbstractSuggestionView>, Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1019896021432984596L;
	/**
	 * 
	 */
	@Inject
    private SuggestionList suggestionList;
	
	public SuggestionConverter() {
	}
     
    @Override
    public AbstractSuggestionView getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
        	if(StringUtils.isNumericSpace(value)) {
	            try {
	                return suggestionList.getSuggestion(value);
	            } catch(NumberFormatException e) {
	                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
	            }
        	} else {
        		String query = value.toLowerCase();
        		QueryPatternSuggestionView suggestionItem = new QueryPatternSuggestionView(-1,
        				query,
        				null,
        				query);
        		return suggestionItem;
        	}
        }
        return null;
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, AbstractSuggestionView object) {
        if(object != null) {
            return String.valueOf(object.getResultId());
        }
        else {
            return null;
        }
    }
    
    public void setSuggestionList(SuggestionList suggestionList) {
    	this.suggestionList = suggestionList;
    }
}