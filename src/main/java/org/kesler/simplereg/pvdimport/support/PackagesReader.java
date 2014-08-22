package org.kesler.simplereg.pvdimport.support;

import org.kesler.simplereg.pvdimport.DBReader;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Package;
import org.kesler.simplereg.util.OracleUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PackagesReader extends DBReader {

    private List<Package> packages;
    private ReaderListener readerListener;
    private Date beginDate;
    private Date endDate;
    private Long lastNum;

    public PackagesReader(ReaderListener readerListener) {
        this.readerListener = readerListener;
        packages = new ArrayList<Package>();
    }

    public PackagesReader(ReaderListener readerListener, Date beginDate, Date endDate) {
        this.readerListener = readerListener;
        this.beginDate = beginDate;
        this.endDate = endDate;
        packages = new ArrayList<Package>();
    }

    public PackagesReader(ReaderListener readerListener, Long lastNum) {
        this.readerListener = readerListener;
        this.lastNum = lastNum;
        packages = new ArrayList<Package>();
    }


    @Override
    public String getQuerySQL() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        if (beginDate!=null || endDate!=null || lastNum!=null) {
            return "select P.ID, P.REGNUM, P.ID_TYPE, P.BEGINDATE, PT.GROUPTYPE, PT.TYPE " +
                    "from DPS$PACKAGE P, DPS$PACKAGETYPE PT " +
                    "WHERE PT.ID=P.ID_TYPE " +
                    (beginDate==null?"":" AND TRUNC(BEGINDATE) >= '" + dateFormat.format(beginDate) + "' ") +
                    (endDate==null?"":" AND TRUNC(BEGINDATE) <= '" + dateFormat.format(endDate) + "' ") +
                    (lastNum==null?"":" AND NUM > " + lastNum.toString()) +
                    " ORDER BY NUM";
         } else {
            return "select P.ID, P.REGNUM, P.ID_TYPE, P.BEGINDATE, PT.GROUPTYPE, PT.TYPE " +
                    "from DPS$PACKAGE P, DPS$PACKAGETYPE PT " +
                    "WHERE PT.ID=P.ID_TYPE";
        }
    }

    @Override
    public void processRs(ResultSet rs) throws SQLException {
        if (rs==null) return;
        while (rs.next()) {
            Package aPackage = new Package();
            aPackage.setId(rs.getString("ID"));
            aPackage.setTypeId(rs.getString("ID_TYPE"));
            aPackage.setRegnum(rs.getString("REGNUM"));
            aPackage.setBeginDate(rs.getDate("BEGINDATE"));
            aPackage.setGroupType(rs.getString("GROUPTYPE"));
            aPackage.setType(rs.getString("TYPE"));
            packages.add(aPackage);
        }
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void readFullInSeparateThread() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PackagesReader.this.read();
                for (Package aPackage: packages) {
                    CauseReader causeReader = new CauseReader(aPackage);
                    causeReader.read();
                    for (Cause cause: aPackage.getCauses()) {
                        // читаем заявителей
                        ApplicantsReader applicantsReader = new ApplicantsReader(cause);
                        applicantsReader.read();
                        for (Applicant applicant:cause.getApplicants()) {
                            // читаем субъектов
                            SubjectReader subjectReader = new SubjectReader(applicant);
                            subjectReader.read();
                        }
                        // читаем объекты
                        ObjReader objReader = new ObjReader(cause);
                        objReader.read();
                        // читаем платежи
                        PayReader payReader = new PayReader(cause);
                        payReader.read();
                    }

                }
                OracleUtil.closeConnection();
                readerListener.readComplete();
            }

        });
        readerThread.start();
    }

    public void readChargeInSeparateThread() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PackagesReader.this.read();
                for (Package aPackage: packages) {
                    CauseReader causeReader = new CauseReader(aPackage);
                    causeReader.read();
                    for (Cause cause: aPackage.getCauses()) {
                        // читаем платежи
                        PayReader payReader = new PayReader(cause);
                        payReader.read();
                    }
                }
                OracleUtil.closeConnection();
                readerListener.readComplete();
            }

        });
        readerThread.start();
    }

}
