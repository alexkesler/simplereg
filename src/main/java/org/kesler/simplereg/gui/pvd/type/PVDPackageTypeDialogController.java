package org.kesler.simplereg.gui.pvd.type;

import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.PackageType;
import org.kesler.simplereg.pvdimport.support.PackageTypesReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PVDPackageTypeDialogController implements ReaderListener{

    private static PVDPackageTypeDialogController instance = new PVDPackageTypeDialogController();

    private PVDPackageTypeDialog dialog;
    private final List<PackageType> types;

    public static synchronized PVDPackageTypeDialogController getInstance() {
        return instance;
    }

    private PVDPackageTypeDialogController() {
        types = new ArrayList<PackageType>();
        PackageTypesReader packageTypesReader = new PackageTypesReader(this);
        packageTypesReader.readInSeparateThread();
    }

    public List<PackageType> getTypes() {return types;}

    public void showDialog(JDialog parentDialog) {
        dialog = new PVDPackageTypeDialog(parentDialog, this);
        dialog.setVisible(true);
    }

    @Override
    public void readComplete() {
        dialog.update();
    }
}
