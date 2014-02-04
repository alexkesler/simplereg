package org.kesler.simplereg.fias.jaxb;


import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by Алексей on 03.02.14.
 */
public class JAXBFIASSaver {

    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private JAXBFIASListener listener;

    public JAXBFIASSaver(JAXBFIASListener listener) {
        this.listener = listener;
    }

    public void saveFIAS(File file) {

        FIAS fias = new FIAS();
        log.info("Reading records from DB");
        listener.jaxbMessage("Читаем список объектов из БД");
        fias.setRecords(DAOFactory.getInstance().getFiasRecordDAO().getAllRecords());
        try {
            JAXBContext context = JAXBContext.newInstance(FIAS.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            log.info("Saving XML to file: " + file.getPath());
            listener.jaxbMessage("Сохраняем в файл");
            marshaller.marshal(fias, file);
            log.info("Saving complete.");
            listener.jaxbMessage("Готово");

        } catch (Exception e) {
            log.error("Error saving XML", e);
            listener.jaxbMessage("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
