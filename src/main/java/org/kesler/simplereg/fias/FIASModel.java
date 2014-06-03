package org.kesler.simplereg.fias;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель для получения адресных объектов
 */
public class FIASModel {

    Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private List<FIASRecord> allRecords;

    private List<String> lastSearchAddresses;

    private FIASModelListener listener;

    private int threadCount = 0; // количество процессов поиска - для прерывания текущего поиска

    public FIASModel(FIASModelListener listener) {
        this.listener = listener;
        allRecords = DAOFactory.getInstance().getFiasRecordDAO().getAllRecords();
    }

    /**
     * Получаем список адресов, удовлетворяющих строке поиска
     * поиск осуществляется в отдельном потоке
     * результаты поиска отправляются FIASModelListener
     * @param searchString строка поиска
     */
    public void computeAddressesInSeparateThread(final String searchString) {
        // Ограничиваем поиск
//        if(searchString.length() < 3) {
//            listener.addresesFiltered(new ArrayList<String>());
//            return;
//        }

//        if(searchString.length() > 10) {
//            listener.addresesFiltered(lastSearchAddresses);
//            return;
//        }

        Thread computeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadCount++;   // Запускаем еще процесс
                lastSearchAddresses = computeAddress(searchString);
                listener.addresesFiltered(lastSearchAddresses);
                threadCount--;    // Закончили
            }
        });

        computeThread.start();


    }

    private List<String> computeAddress(String searchString) {
        List<String> addreses = new ArrayList<String>();
        List<FIASRecord> filteredRecords = searchRecords(searchString);

        for (FIASRecord record: filteredRecords) {
            addreses.add(createAddress(record));
        }


        log.info("Computed " + addreses.size() + " addresses.");
        return addreses;
    }

    // Создаем полную адресную строку на основе записи ФИАС
    private String createAddress(FIASRecord record) {
        StringBuilder address = new StringBuilder();

        address.append(makeFormalName(record)).append(", ");

        String parentGUID = record.getParentGUID();

        while (parentGUID != null) {
            FIASRecord parentRecord = searchRecordByGUID(parentGUID);

            // если родительский элемент = элемент первого уровня - заканчиваем
            if(parentRecord.getAoLevel().equalsIgnoreCase("1")) break;

            address.insert(0, makeFormalName(parentRecord) + ", ");
            parentGUID = parentRecord.getParentGUID();
        }


        return address.toString();
    }

    // получаем наименование из объекта ФИАС
    private String makeFormalName(FIASRecord record) {
        String name = null;
        if(record.getAoLevel().equalsIgnoreCase("1")) {
            name = record.getFormalName() + " " + record.getShortName() + ".";
        } else {
            name = record.getShortName() + ". " + record.getFormalName();
        }


        return name;
    }

    // Ищем объект ФИАС по его GUID
    private FIASRecord searchRecordByGUID(String guid) {
        FIASRecord findRecord = null;
        for(FIASRecord record: allRecords)  {
            if(record.getAoGUID().equalsIgnoreCase(guid)) {
                findRecord = record;
                break;
            }
        }
        return findRecord;
    }

    // ищм записи, удовлетворяющие условиям поиска
    private List<FIASRecord> searchRecords(String searchString) {
        List<FIASRecord> filteredRecords = new ArrayList<FIASRecord>();

        if (allRecords == null)
            allRecords = DAOFactory.getInstance().getFiasRecordDAO().getAllRecords();

        searchString = searchString.toLowerCase();
        for(FIASRecord record: allRecords)  {
           if(threadCount>1) break;   // если кто-то еще запустился - заканчиваем с работой
            if(record.getFormalName().toLowerCase().indexOf(searchString) == 0)
                filteredRecords.add(record);

            if(filteredRecords.size() > 30) break;
        }

        log.info("Find " + filteredRecords.size() + " by search: " + searchString);
        return filteredRecords;

    }


}
