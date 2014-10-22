package org.kesler.simplereg.export;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.kesler.simplereg.logic.Reception;

import java.util.ArrayList;
import java.util.List;

public class RosReestrPoiReceptionPrinter extends ReceptionPrinter {

    public RosReestrPoiReceptionPrinter(Reception reception) {
        super(reception);
    }

    @Override
    public void printReception() throws Exception{

        XWPFDocument doc = new XWPFDocument(OPCPackage.open("template/request.docx"));

        String docText = doc.toString();

    }

    public void replace(XWPFDocument document, String findText, String replaceText) {
        for(XWPFParagraph paragraph:document.getParagraphs()) {
            replace(paragraph,findText,replaceText);
        }
    }

    public void replace(XWPFParagraph paragraph, String findText, String replaceText) {
        if (paragraph.getText().contains(findText)) {
            splitRunsForTextAndReplace(paragraph,findText,replaceText);
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
        if (textRuns.size()>1) {
            String runText = "";
            for(XWPFRun run:textRuns) {
                runText += run.getText(0);
            }

            String replacedRunText = runText.replace(findText,replaceText);

            XWPFRun mainRun = textRuns.get(0);
            mainRun.setText(replacedRunText);
            for (int i=1;i>textRuns.size();i++)
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
