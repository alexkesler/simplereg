package org.kesler.simplereg.export.reestr;

/**
 * Created by alex on 13.06.14.
 */
public class ReestrExporterFactory {

    public static ReestrExporter createReestrExporter(ReestrExporter.Type type) {
        switch (type) {
            case FOR_ARCHIVE:
                return new ArchiveReestrExporter();
            case SELECTED_COLUMNS:
                return new SelectedColumnsReestrExporter();
            case FOR_RETURN:
                return new ReturnReestrExporter();
            case FOR_TRANSMIT:
                return new TransmitReestrExporter();
            default:
                return null;
        }
    }
}
