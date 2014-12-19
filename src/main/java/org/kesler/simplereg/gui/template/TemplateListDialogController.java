package org.kesler.simplereg.gui.template;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;
import org.kesler.simplereg.logic.template.Template;
import org.kesler.simplereg.logic.template.TemplateService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class TemplateListDialogController implements GenericListDialogController<Template> {
    private final Logger log = Logger.getLogger(this.getClass());
    private static TemplateListDialogController instance;

    private TemplateListDialogController() {
        templateService = new TemplateService();
    }

    private GenericListDialog<Template> dialog;

    private TemplateService templateService;


    public static synchronized TemplateListDialogController getInstance() {
        if (instance == null) {
            instance = new TemplateListDialogController();
        }
        return instance;
    }

    public Template showSelectDialog(JFrame parentFrame) {
        log.info("Open select dialog");
        Template template = null;
        dialog = new GenericListDialog<Template>(parentFrame, "Шаблоны", this);
        updateTemplates();

        dialog.setVisible(true);

        if (dialog.getResult() == GenericListDialog.OK && dialog.getSelectedIndex() != -1) {
            template = dialog.getSelectedItem();
        }

        // Освобождаем ресурсы
        dialog.dispose();
        dialog = null;

        return template;
    }

    public Template showSelectDialog(JDialog parentDialog) {
        log.info("Open select dialog");
        Template template = null;
        dialog = new GenericListDialog<Template>(parentDialog, "Шаблоны", this);
        updateTemplates();
        dialog.setVisible(true);

        if (dialog.getResult() == GenericListDialog.OK && dialog.getSelectedIndex() != -1) {
            template = dialog.getSelectedItem();
        }

        // Освобождаем ресурсы
        dialog.dispose();
        dialog = null;

        return template;
    }


    @Override
    public boolean openAddItemDialog() {
        log.info("Open add item dialog");
        Template template = new Template();
        if (TemplateDialog.showModal(dialog, template)) {
            AddTemplateWorker addTemplateWorker = new AddTemplateWorker(template);
            dialog.showProcess();
            addTemplateWorker.execute();
            return true;
        }
        return false;

    }

    @Override
    public boolean openEditItemDialog(Template template) {
        if (template == null) return false;
        log.info("Open edit item dialog");
        if (TemplateDialog.showModal(dialog, template)) {
            UpdateTemplateWorker updateTemplateWorker = new UpdateTemplateWorker(template);
            dialog.showProcess();
            updateTemplateWorker.execute();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeItem(Template template) {
        if (template == null) return false;
        log.info("Remove item");
        RemoveTemplateWorker updateTemplateWorker = new RemoveTemplateWorker(template);
        dialog.showProcess();
        updateTemplateWorker.execute();
        return true;
    }

    @Override
    public void filterItems(String filter) {

    }

    @Override
    public void updateItems() {
        updateTemplates();
    }




    private void updateTemplates() {
        UpdateTemplatesWorker updateTemplatesWorker = new UpdateTemplatesWorker();
        dialog.showProcess();
        updateTemplatesWorker.execute();
    }

    class UpdateTemplatesWorker extends SwingWorker<List<Template>, Void> {
        private final Logger log = Logger.getLogger(this.getClass());
        @Override
        protected List<Template> doInBackground() throws Exception {
            log.info("Update templates..");
            return templateService.getAllTemplates();
        }

        @Override
        protected void done() {
            List<Template> templates = new ArrayList<Template>();
            try {
                dialog.hideProcess();
                templates = get();
                log.info("Update templates complete");
            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error ",e);
                JOptionPane.showMessageDialog(dialog,
                        "Ошибка при чтении данных",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            dialog.setItems(templates);

        }
    }

    class AddTemplateWorker extends SwingWorker<Void, Void> {
        private final Logger log = Logger.getLogger(this.getClass());
        private Template template;

        public AddTemplateWorker(Template template) {
            this.template = template;
        }

        @Override
        protected Void doInBackground() throws Exception {
            log.info("Add template " + template.getName());
            templateService.addTemplate(template);
            return null;
        }

        @Override
        protected void done() {
            try {
                dialog.hideProcess();
                get();
                log.info("Adding complete");
                updateTemplates();
            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error adding template",e);
                JOptionPane.showMessageDialog(dialog,
                        "Ошибка при сохранении данных",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);

            }

        }
    }

   class UpdateTemplateWorker extends SwingWorker<Void, Void> {
        private final Logger log = Logger.getLogger(this.getClass());
        private Template template;

        public UpdateTemplateWorker(Template template) {
            this.template = template;
        }

        @Override
        protected Void doInBackground() throws Exception {
            log.info("Update template " + template.getName());
            templateService.updateTemplate(template);
            return null;
        }

        @Override
        protected void done() {
            try {
                dialog.hideProcess();
                get();
                log.info("Updating complete");
                updateTemplates();
            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error adding template",e);
                JOptionPane.showMessageDialog(dialog,
                        "Ошибка при сохранении данных",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);

            }

        }
    }

   class RemoveTemplateWorker extends SwingWorker<Void, Void> {
        private final Logger log = Logger.getLogger(this.getClass());
        private Template template;

        public RemoveTemplateWorker(Template template) {
            this.template = template;
        }

        @Override
        protected Void doInBackground() throws Exception {
            log.info("Remove template " + template.getName());
            templateService.removeTemplate(template);
            return null;
        }

        @Override
        protected void done() {
            try {
                dialog.hideProcess();
                get();
                log.info("Removing complete");
                updateTemplates();
            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error adding template",e);
                JOptionPane.showMessageDialog(dialog,
                        "Ошибка при сохранении данных",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);

            }

        }
    }


}
