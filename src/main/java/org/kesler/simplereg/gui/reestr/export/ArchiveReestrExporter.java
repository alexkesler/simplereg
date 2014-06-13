package org.kesler.simplereg.gui.reestr.export;

/**
 * Created by alex on 13.06.14.
 */
public class ArchiveReestrExporter extends ReestrExporter {

    private ReestrExportEnum exportEnum = ReestrExportEnum.FOR_ARCHIVE;


    @Override
    public ReestrExportEnum getEnum() {
        return exportEnum;
    }

    @Override
    protected void prepare() {

    }
}
