package org.kesler.simplereg.pvdimport.support;


import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.RSProcessor;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.util.OracleUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CausesReader implements RSProcessor {

    private List<Cause> causes;
    private ReaderListener readerListener;

    public CausesReader(ReaderListener readerListener) {
        this.readerListener = readerListener;
        causes = new ArrayList<Cause>();
    }

    @Override
    public String getQuerySQL() {
        return "SELECT C.ID, C.STATUSMD, C.ESTIMATEDATE, P.ID_BOOK, P.ID_TYPE, P.REGNUM, PT.GROUPTYPE, PT.TYPE " +
                "from DPS$PACKAGE P, DPS$PACKAGETYPE PT, DPS$D_CAUSE C " +
                "WHERE C.ID_PACKAGE=P.ID AND PT.ID=P.ID_TYPE";
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        if (rs==null) return;
        while (rs.next()) {
            Cause cause = new Cause();
            cause.setId(rs.getString("ID"));
            cause.setRegnum(rs.getString("REGNUM"));
            cause.setEstimateDate(rs.getDate("ESTIMATEDATE"));
            cause.setGroupType(rs.getString("GROUPTYPE"));
            cause.setType(rs.getString("TYPE"));

            causes.add(cause);
        }
        System.out.println("Read " + causes.size() + " causes");
    }

    public List<Cause> getCauses() {return causes;}

    public void read() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DBReader.read(CausesReader.this);
                for (Cause cause: causes) {
                    ApplicantsReader applicantsReader = new ApplicantsReader(cause);
                    DBReader.read(applicantsReader);
                    for (Applicant applicant:cause.getApplicants()) {
                        SubjectReader subjectReader = new SubjectReader(applicant);
                        DBReader.read(subjectReader);
                    }
                }
                OracleUtil.closeConnection();
                readerListener.readComplete();
            }

        });
        readerThread.start();
    }
}
