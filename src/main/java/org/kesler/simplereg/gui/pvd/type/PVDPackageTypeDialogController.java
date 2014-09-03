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
    private PackageTypesReader packageTypesReader;

    public static synchronized PVDPackageTypeDialogController getInstance() {
        return instance;
    }

    private PVDPackageTypeDialogController() {
        types = new ArrayList<PackageType>();
        packageTypesReader = new PackageTypesReader(this);
        packageTypesReader.readInSeparateThread();
    }

    List<PackageType> getTypes() {return types;}

    public void showDialog(JDialog parentDialog) {
        dialog = new PVDPackageTypeDialog(parentDialog, this);
        dialog.setVisible(true);
    }

    @Override
    public void readComplete() {
        types.clear();
        types.addAll(packageTypesReader.getTypes());
        dialog.update();
    }
}
