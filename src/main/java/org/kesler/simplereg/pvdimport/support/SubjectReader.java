package org.kesler.simplereg.pvdimport.support;


import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SubjectReader extends DBReader {
    private Applicant applicant;

    public SubjectReader(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public String getQuerySQL() {
        String subjectId = applicant.getIdSubject();
        String agentId = applicant.getIdAgent();
        return "SELECT ID, ID_CLSTYPE, SURNAME, FIRSTNAME, PATRONYMIC, SHORTNAME from DPS$SUBJECT" +
                " WHERE ID = '" + subjectId + "'" +
                (agentId==null?"":(" OR ID = '" + agentId + "'"));
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setId(applicant.getIdSubject());
        Subject agent = null;
        if (applicant.getIdAgent()!=null) agent = new Subject();
        while (rs.next()) {
            if(rs.getString("ID").equals(applicant.getIdSubject())) {
                // заполняем субъекта
                subject.setClsType(rs.getString("ID_CLSTYPE"));
                subject.setShortName(rs.getString("SHORTNAME"));
                subject.setFirstname(rs.getString("FIRSTNAME"));
                subject.setSurname(rs.getString("SURNAME"));
                subject.setPatronymic(rs.getString("PATRONYMIC"));
            } else if (agent!=null && rs.getString("ID").equals(applicant.getIdAgent())) {
                agent.setClsType(rs.getString("ID_CLSTYPE"));
                agent.setShortName(rs.getString("SHORTNAME"));
                agent.setFirstname(rs.getString("FIRSTNAME"));
                agent.setSurname(rs.getString("SURNAME"));
                agent.setPatronymic(rs.getString("PATRONYMIC"));
            }
        }
        applicant.setSubject(subject);
        applicant.setAgent(agent);
    }
}
