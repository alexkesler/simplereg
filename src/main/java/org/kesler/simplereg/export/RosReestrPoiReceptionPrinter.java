package org.kesler.simplereg.export;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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
        for(XWPFParagraph paragraph:document.getParagraphs()) {
            replace(paragraph,findText,replaceText);
        }
    }

    public void replace(XWPFParagraph paragraph, String findText, String replaceText) {
        if (paragraph.getText().contains(findText)) {
            log.debug("Find paragraph with text: " + findText + ", try to split runs and replace by " + replaceText);
            splitRunsForTextAndReplace(paragraph, findText, replaceText);
        }
    }

    private void splitRunsForTextAndReplace(XWPFParagraph paragraph, String findText, String replaceText) {

        String paragraphText = paragraph.getText();
        int begIndex = paragraphText.indexOf(findText);
        if (begIndex<0) return;
        int endIndex = begIndex+findText.length();
        List<XWPFRun> runs = paragraph.getRuns();

        // Заполняем позиции ранов
        List<RunPos> runPoses = new ArrayList<RunPos>();
        int position=0;
        for (XWPFRun run:runs) {
            RunPos runPos = new RunPos(run,position);
            position = runPos.getEndIndex()+1;
            runPoses.add(runPos);
        }

        // Составляем список ранов, содержащих указанный текст
        List<XWPFRun> textRuns = new ArrayList<XWPFRun>();
        for (RunPos runPos:runPoses) {
            if(begIndex>=runPos.getBegIndex() && begIndex<=runPos.getEndIndex())
                textRuns.add(runPos.getRun());
            if(endIndex>=runPos.getBegIndex() && endIndex<=runPos.getEndIndex())
                textRuns.add(runPos.getRun());
            if(begIndex<=runPos.getBegIndex() && endIndex>=runPos.getEndIndex())
                textRuns.add(runPos.getRun());
        }

        // заменяем полученный список ранов одним раном - первым из списка
        log.debug("Find " + textRuns.size() + " with text");
        String runText = "";
        for(XWPFRun run:textRuns) {
            runText += run.getText(0);
        }

        log.debug("Splitted text: " + runText);
        String replacedRunText = runText.replace(findText,replaceText);
        log.debug("Replaced text: " + replacedRunText);

        XWPFRun mainRun = textRuns.get(0);
        log.debug("Set replaced text to first run");
        mainRun.setText(replacedRunText,0);

        log.debug("New Run text: " + mainRun.getText(0));

        if (textRuns.size()>1) {
        log.debug("Remove unnesessary " + (textRuns.size()-1) + " runs");
            for (int i=1;i<textRuns.size();i++)
                paragraph.removeRun(runs.indexOf(textRuns.get(i)));

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
            endIndex = begIndex + runText.length();
        }

        public XWPFRun getRun() { return run; }

        public int getBegIndex() { return begIndex; }

        public int getEndIndex() { return endIndex; }
    }

}
