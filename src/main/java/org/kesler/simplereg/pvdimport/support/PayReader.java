package org.kesler.simplereg.pvdimport.support;

import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Pay;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PayReader extends DBReader {
    private Cause cause;

    public PayReader(Cause cause) {
        this.cause = cause;
    }

    @Override
    public String getQuerySQL() {
        return "select ID, CHARGE, PAYCODE " +
                "from DPS$PAYCODE " +
                "where ID_CAUSE='" + cause.getId() + "'";
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Pay pay = new Pay();
            pay.setId(rs.getString("ID"));
            pay.setCauseId(cause.getId());
            pay.setCharge(rs.getDouble("CHARGE"));
            pay.setPaycode(rs.getString("PAYCODE"));

            cause.getPays().add(pay);
        }
    }
}
