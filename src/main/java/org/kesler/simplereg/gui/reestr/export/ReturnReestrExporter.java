package org.kesler.simplereg.gui.reestr.export;

/**
 * Экспорт ведомости для возврата
 */
public class ReturnReestrExporter extends ReestrExporter {
    private ReestrExportEnum exportEnum = ReestrExportEnum.FOR_RETURN;

    @Override
    public ReestrExportEnum getEnum() {
        return exportEnum;
    }

    @Override
    protected void prepare() {

    }
}
