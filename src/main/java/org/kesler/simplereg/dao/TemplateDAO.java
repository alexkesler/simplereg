package org.kesler.simplereg.dao;

import org.kesler.simplereg.logic.template.Template;

import java.util.List;

/**
 * Created by alex on 17.12.14.
 */
public interface TemplateDAO {
    void addTemplate(Template template);
    void updateTemplate(Template template);
    void removeTemplate(Template template);
    List<Template> getAllTemplates();
    List<Template> getDefaultTemplates();
    Template getTemplateByUUID(String uuid);
}
