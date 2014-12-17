package org.kesler.simplereg.logic.template;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.TemplateDAO;

import java.util.List;

/**
 * Created by alex on 17.12.14.
 */
public class TemplateModel {
    private final Logger log = Logger.getLogger(this.getClass());
    private TemplateDAO templateDAO;

    public TemplateModel() {
        templateDAO = DAOFactory.getInstance().getTemplateDAO();
    }


    public void addTemplate(Template template) {
        templateDAO.addTemplate(template);
    }

    public void updateTemplate(Template template) {
        templateDAO.updateTemplate(template);
    }

    public void removeTemplate(Template template) {
        templateDAO.removeTemplate(template);
    }

    public List<Template> getAllTemplates() {
        return templateDAO.getAllTemplates();
    }

    public List<Template> getDefaultTemplates() {
        return templateDAO.getDefaultTemplates();
    }


    public Template getTemplateByUUID(String uuid) {
        return templateDAO.getTemplateByUUID(uuid);
    }
}
