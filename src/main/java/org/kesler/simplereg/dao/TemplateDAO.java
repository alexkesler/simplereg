package org.kesler.simplereg.dao;

import org.kesler.simplereg.logic.template.Template;

import java.util.List;

/**
 * Created by alex on 17.12.14.
 */
public interface TemplateDAO {
    public void addTemplate(Template template);
    public void updateTemplate(Template template);
    public void removeTemplate(Template template);
    public List<Template> getAllTemplates();
    public List<Template> getDefaultTemplates();
    public Template getTemplateByUUID(String uuid);
}
