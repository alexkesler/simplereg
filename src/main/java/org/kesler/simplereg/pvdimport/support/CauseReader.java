package org.kesler.simplereg.pvdimport.support;


import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Package;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CauseReader extends DBReader {

    private Package aPackage;

    public CauseReader(Package aPackage) {
        this.aPackage = aPackage;
    }

    @Override
    public String getQuerySQL() {
        return "SELECT C.ID, C.ID_PACKAGE, C.STATUSMD, C.STARTDATE, C.ESTIMATEDATE, C.ID_PROC, C.STATE, C.STATUSMD, C.PURPOSE " +
                ", RBI.REGNUM, RBI.REGDATE " +
                "from DPS$D_CAUSE C, DPS$RECBOOKITEM RBI " +
                "WHERE RBI.ID_CAUSE=C.ID AND " +
                " ID_PACKAGE='" + aPackage.getId() + "'";
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        if (rs==null) return;
        while (rs.next()) {
            Cause cause = new Cause();
            cause.setId(rs.getString("ID"));
            cause.setPackage(aPackage);
            cause.setRegnum(rs.getString("REGNUM"));
            cause.setProcId(rs.getString("ID_PROC"));
            cause.setStartDate(rs.getDate("STARTDATE"));
            cause.setEstimateDate(rs.getDate("ESTIMATEDATE"));
            cause.setState(rs.getInt("STATE"));
            cause.setStatusMD(rs.getString("STATUSMD"));
            cause.setPurpose(rs.getInt("PURPOSE"));

            aPackage.getCauses().add(cause);
        }

    }


}
