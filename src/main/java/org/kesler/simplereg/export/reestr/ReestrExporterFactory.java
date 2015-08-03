package org.kesler.simplereg.export.reestr;

/**
 * Created by alex on 13.06.14.
 */
public class ReestrExporterFactory {

    public static ReestrExporter createReestrExporter(ReestrExportEnum exportEnum) {
        switch (exportEnum) {
            case FOR_ARCHIVE:
                return new ArchiveReestrExporter();
            case SELECTED_COLUMNS:
                return new SelectedColumnsReestrExporter();
            case FOR_RETURN:
                return new ReturnReestrExporter();
            default:
                return null;
        }
    }
}
