package org.kesler.simplereg.fias.jaxb;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.fias.FIASRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Алексей on 03.02.14.
 */
public class JAXBFIASLoader {
    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private JAXBFIASListener listener;

    public JAXBFIASLoader(JAXBFIASListener listener) {
        this.listener = listener;
    }

    public void loadFIAS(File file) {

        FIAS fias = null;
        log.info("Removing all records from DB");
        listener.jaxbMessage("Удаляем все записи из БД");
        DAOFactory.getInstance().getFiasRecordDAO().removeAllRecords();
        try {
            JAXBContext context = JAXBContext.newInstance(FIAS.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            log.info("Reading XML from file: " + file.getPath());
            listener.jaxbMessage("Читаем из файла");
            fias =  (FIAS) unmarshaller.unmarshal(file);
            log.info("Reading complete.");
            listener.jaxbMessage("Готово");

        } catch (Exception e) {
            log.error("Error saving XML", e);
            listener.jaxbMessage("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("Saving to DB");
        if(fias != null && fias.getRecords() != null && fias.getRecords().size()>0)
            DAOFactory.getInstance().getFiasRecordDAO().addRecords(fias.getRecords());


    }

}
