package org.kesler.simplereg.export;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.kesler.simplereg.export.mapping.MappingFactory;
import org.kesler.simplereg.logic.Reception;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RosReestrPoiReceptionPrinter extends ReceptionPrinter {
    private Logger log = Logger.getLogger(this.getClass().getSimpleName());

    public RosReestrPoiReceptionPrinter(Reception reception) {
        super(reception);
    }

    @Override
    public void printReception() throws Exception{
        log.info("Handle printReception");

        String templatePath = getRequestTemplatePath();
        if (templatePath.isEmpty()) return;
        log.info("Open doc: " + templatePath);
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(templatePath));

        log.info("Replace mappings..");
        Map<String,String> valueMap = MappingFactory.getInstance().initMappings(reception).getValueMap();
        for (String key:valueMap.keySet()) {
            replace(doc,key,valueMap.get(key));
        }

        log.info("Save doc..");
        doc.write(new FileOutputStream(getRequestPath()));
        log.info("Saving complete. Opening..");
        openFile(getRequestPath());


    }

    public void replace(XWPFDocument document, String findText, String replaceText) {
        log.debug("Try to replace in document: " + findText + " -> " + replaceText);
        // Заменяем в параграфах
        for(XWPFParagraph paragraph:document.getParagraphs()) {
            replace(paragraph,findText,replaceText);
        }

        // Заменяем в таблицах
        for (XWPFTable table:document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replace(paragraph, findText, replaceText);
                    }
                }
            }
        }
    }

    public void replace(XWPFParagraph paragraph, String findText, String replaceText) {
        if (paragraph.getText().contains(findText)) {
            log.debug("Find paragraph with text: " + findText);
            splitRunsForTextAndReplace(paragraph, findText, replaceText);
        }
    }

    private void splitRunsForTextAndReplace(XWPFParagraph paragraph, String findText, String replaceText) {
        log.debug("Try to split runs and replace by " + replaceText);

        String paragraphText = paragraph.getText();
        log.debug("Paragraph text:" + paragraph.getText());
        log.debug("Paragraph size: " + paragraph.getText().length());

        int begIndex = paragraphText.indexOf(findText);
        if (begIndex<0) return;
        int endIndex = begIndex+findText.length()-1;
        log.debug("FindText indexes: ["+begIndex+";"+endIndex+"]");

        List<XWPFRun> runs = paragraph.getRuns();

        String runsString = "";
        for (int i=0;i<runs.size();i++) {
            XWPFRun run = runs.get(i);
            runsString += "["+i+"("+run.getText(0).length()+")"+"]-{"+run.getText(0)+"};";
        }
        log.debug("Runs: "+runsString);
        // Заполняем позиции ранов
        List<RunPos> runPoses = new ArrayList<RunPos>();
        int position=0;
        String runPosString = "";

        for (int i=0;i<runs.size();i++) {
            XWPFRun run = runs.get(i);
            RunPos runPos = new RunPos(run,position);
            position = runPos.getEndIndex()+1;
            runPoses.add(runPos);
            runPosString += "("+i+")-["+runPos.begIndex+";"+runPos.endIndex+"]";
        }
        log.debug("Run positions: " + runPosString);

        // Составляем список ранов, содержащих указанный текст
        List<XWPFRun> matchedRuns = new ArrayList<XWPFRun>();
        for (RunPos runPos:runPoses) {
            if (runPos.isInInterval(begIndex,endIndex)) matchedRuns.add(runPos.getRun());
        }

        // заменяем полученный список ранов одним раном - первым из списка
        log.debug("Find " + matchedRuns.size() + " runs with text");
        String matchedRunsString = "";
        String runText = "";
        for(int i=0;i<matchedRuns.size();i++) {
            XWPFRun run = matchedRuns.get(i);
            matchedRunsString += "["+i+"]-{"+matchedRuns.get(i).getText(0)+"};";
            runText += run.getText(0);
        }

        log.debug("Matched runs: " + matchedRunsString);
        log.debug("Splitted text: " + runText);
        String replacedRunText = runText.replace(findText,replaceText);
        log.debug("Replaced text: " + replacedRunText);

        XWPFRun mainRun = matchedRuns.get(0);
        log.debug("Set replaced text to first run");
        mainRun.setText(replacedRunText, 0);

        log.debug("New Run text: " + mainRun.getText(0));

        if (matchedRuns.size()>1) {
        log.debug("Remove unnesessary " + (matchedRuns.size()-1) + " runs");
            for (int i=1;i<matchedRuns.size();i++)
                paragraph.removeRun(runs.indexOf(matchedRuns.get(i)));

        }

    }


    private class RunPos {
        private XWPFRun run;
        private int begIndex;
        private int endIndex;

        RunPos(XWPFRun run, int begIndex) {
            this.run = run;
            this.begIndex = begIndex;
            String runText = run.getText(0);
            endIndex = begIndex + runText.length()-1;
        }

        public XWPFRun getRun() { return run; }

        public int getBegIndex() { return begIndex; }

        public int getEndIndex() { return endIndex; }

        public boolean isInclude(int pos) {
            return  (pos>=begIndex && pos<=endIndex);
        }

        public boolean isBefore(int pos) {
            return  endIndex < pos;
        }

        public boolean isAfter(int pos) {
            return  begIndex > pos;
        }

        public boolean isInInterval(int begPos, int endPos) {
            if (isInclude(begPos)) return true;
            if (isInclude(endPos)) return true;
            if (isAfter(begPos) && isBefore(endPos)) return true;
            return false;
        }

    }

}
