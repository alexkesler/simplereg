package org.kesler.simplereg.pvdimport.support;

import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicantsReader extends DBReader {
    private Cause cause;

    public ApplicantsReader(Cause cause) {
        this.cause = cause;
    }

    @Override
    public String getQuerySQL() {
//        String causeIDs = "";
//        for (int i=0; i< cause.size(); i++) {
//            Cause cause = this.cause.get(i);
//            causeIDs += (cause.getId());
//            if (i!= this.cause.size()-1) causeIDs+=",";
//        }

        return "SELECT ID, ID_CAUSE, ID_SUBJECT, ID_AGENT FROM DPS$APPLICANT " +
                "WHERE ID_CAUSE='" + cause.getId() +"'";
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
//        Map<String,Cause> causeMap = new HashMap<String, Cause>();
//        for (Cause cause: this.cause) causeMap.put(cause.getId(),cause);
        while (rs.next()) {
            Applicant applicant = new Applicant();
            applicant.setId(rs.getString("ID"));
            applicant.setIdSubject(rs.getString("ID_SUBJECT"));
            applicant.setIdAgent(rs.getString("ID_AGENT"));

//            Cause cause = causeMap.get(rs.getString("ID_CAUSE"));
            cause.getApplicants().add(applicant);
        }
    }
}
