package org.kesler.simplereg.logic.template;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.TemplateDAO;
import org.kesler.simplereg.logic.support.DefaultTemplateExistTemplateServiceException;
import org.kesler.simplereg.logic.support.ServiceException;
import org.kesler.simplereg.util.OptionsUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TemplateService {
    private final Logger log = Logger.getLogger(this.getClass());
    private TemplateDAO templateDAO;

    public TemplateService() {
        templateDAO = DAOFactory.getInstance().getTemplateDAO();
    }


    public void addTemplate(Template template) throws ServiceException {
        if (template.getByDefault()!=null && template.getByDefault()) {
            if (getDefaultTemplate()!=null)
                throw new DefaultTemplateExistTemplateServiceException();
        }
        try {
            templateDAO.addTemplate(template);
        } catch (Exception e) {
            log.error("Error adding template",e);
            throw new ServiceException("Error adding template",e);
        }
    }

    public void updateTemplate(Template template) throws ServiceException {
        if (template.getByDefault()!=null && template.getByDefault()) {
            Template existDefaultTemplate = getDefaultTemplate();
            if (existDefaultTemplate!=null && !existDefaultTemplate.equals(template))
                throw new DefaultTemplateExistTemplateServiceException();
        }
        try {
            templateDAO.updateTemplate(template);
        } catch (Exception e) {
            log.error("Error update template",e);
            throw new ServiceException("Error update template",e);
        }
    }

    public void removeTemplate(Template template) throws ServiceException {
        try {
            templateDAO.removeTemplate(template);
        } catch (Exception e) {
            log.error("Error remove template",e);
            throw new ServiceException("Error remove template",e);
        }
    }

    public List<Template> getAllTemplates() throws ServiceException {
        List<Template> templates;
        try {
            templates = templateDAO.getAllTemplates();
        } catch (Exception e) {
            log.error("Error getting templates",e);
            throw new ServiceException("Error getting templates",e);
        }
        return templates;
    }

    public Template getDefaultTemplate() throws ServiceException {
        log.info("Getting default template");
        List<Template> defaultTemplates;
        try {
            defaultTemplates = templateDAO.getDefaultTemplates();
        } catch (Exception e) {
            log.error("Error getting default templates",e);
            throw new ServiceException("Error getting default templates",e);
        }
        if (defaultTemplates.size()>1) throw new ServiceException("More than one default template");


        return defaultTemplates.size()==0?loadDefaultTemplateFromFile():defaultTemplates.get(0);
    }


    public Template getTemplateByUUID(String uuid) throws ServiceException {
        Template template = null;

        try {
            template = templateDAO.getTemplateByUUID(uuid);
        } catch (Exception e) {
            log.error("Error getting template with UUID: " + uuid, e);
            throw new ServiceException("Error getting template with UUID: " + uuid, e);
        }

        return template;
    }

    public Template loadDefaultTemplateFromFile() throws ServiceException {
        log.info("Loading default template from file");

        String templatePath = OptionsUtil.getCurrentDir() + "templates" + OptionsUtil.getDirSeparator() + "request.docx";

        File file = new File(templatePath);

        byte[] data = null;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            int length = fileInputStream.available();
            data = new byte[length];
            fileInputStream.read(data);
        } catch (FileNotFoundException e) {
            log.error("Can't find template file for default template", e);
            throw new ServiceException("Can't find template file: " + templatePath);
        } catch (IOException e) {
            log.error("Cant open template", e);
            throw new ServiceException("Can't open template file: " + templatePath);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                log.error("Can't close InputStream", e);
            }
        }


        Template template = new Template();
        template.setName("Шаблон по умолчанию");
        template.setData(data);
        template.setByDefault(true);

        try {
            templateDAO.addTemplate(template);
        } catch (Exception e) {
            log.error("Error adding template",e);
            throw new ServiceException("Error adding template",e);
        }

        return template;
    }
}
