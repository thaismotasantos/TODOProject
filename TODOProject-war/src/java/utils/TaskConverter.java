/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.Task;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 *
 * @author thais
 */
@FacesConverter(value = "taskConverter")
public class TaskConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object ret = null;
        if (component instanceof PickList) {
            Object dualList = ((PickList) component).getValue();
            DualListModel dl = (DualListModel) dualList;
            for (Object o : dl.getSource()) {
                String id = "" + ((Task) o).getId();
                if (value.equals(id)) {
                    ret = o;
                    break;
                }
            }
            if (ret == null)
                for (Object o : dl.getTarget()) {
                    String id = "" + ((Task) o).getId();
                    if (value.equals(id)) {
                        ret = o;
                        break;
                    }
                }
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = "";
        if (value instanceof Task) {
            str = "" + ((Task) value).getId();
        }
        return str;
    }
    
}
