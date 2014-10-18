package org.kesler.simplereg.pvdimport.support;


import org.kesler.simplereg.pvdimport.PVDReader;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.PackageType;
import org.kesler.simplereg.util.OracleUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageTypesReader extends PVDReader {
    private List<PackageType> types;
    private ReaderListener listener;

    public PackageTypesReader(ReaderListener listener) {
        this.listener = listener;
        types = new ArrayList<PackageType>();
    }

    public List<PackageType> getTypes() { return types; }

    @Override
    public String getQuerySQL() {
        return "SELECT ID, GROUPTYPE, TYPE " +
                " from DPS$PACKAGETYPE " +
                " where receptionstatus=5 ";
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        types.clear();
        while (rs.next()) {
            PackageType packageType = new PackageType();
            packageType.setId(rs.getString("ID"));
            packageType.setGroupType(rs.getString("GROUPTYPE"));
            packageType.setType(rs.getString("TYPE"));

            types.add(packageType);
        }
    }

    public void readInSeparateThread() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PackageTypesReader.this.read();
                OracleUtil.closeConnection();
                listener.readComplete();
            }
        });
        readerThread.start();
    }
}
