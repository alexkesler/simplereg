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
    private final List<CheckablePackageType> checkablePackageTypes;
    private String typeIDsString;
    private PackageTypesReader packageTypesReader;

    public static synchronized PVDPackageTypeDialogController getInstance() {
        return instance;
    }

    private PVDPackageTypeDialogController() {
        checkablePackageTypes = new ArrayList<CheckablePackageType>();
        packageTypesReader = new PackageTypesReader(this);
        packageTypesReader.readInSeparateThread();
    }

    List<CheckablePackageType> getCheckablePackageTypes() {return checkablePackageTypes;}

    public String showDialog(JDialog parentDialog, String typeIDsString) {
        this.typeIDsString = typeIDsString;
        System.out.println("Open PVDDialog: " + this.typeIDsString);
        dialog = new PVDPackageTypeDialog(parentDialog, this);
        updateChecks();
        dialog.setVisible(true);
        dialog.dispose();
        loadTypeIDsString();
        System.out.println("Close PVDDialog: " + this.typeIDsString);
        return this.typeIDsString;
    }

    void loadTypeIDsString() {
        StringBuilder typeIDsStringBuilder = new StringBuilder();
        for (CheckablePackageType checkablePackageType: checkablePackageTypes) {

            if (checkablePackageType.isChecked())
            {
                if(typeIDsStringBuilder.length() > 0)
                    typeIDsStringBuilder.append(",");
                typeIDsStringBuilder.append(checkablePackageType.getId());
            }

        }

        typeIDsString = typeIDsStringBuilder.toString();

    }

    @Override
    public void readComplete() {
        List<PackageType> newTypes = packageTypesReader.getTypes();
        checkablePackageTypes.clear();

        for(PackageType packageType:newTypes)
            checkablePackageTypes.add(new CheckablePackageType(packageType));

        updateChecks();

        dialog.update();
    }

    private void updateChecks() {
        for (CheckablePackageType checkablePackageType:checkablePackageTypes)
            checkablePackageType.checkString(typeIDsString);
    }

}

class CheckablePackageType {
    private PackageType packageType;
    private boolean checked;

    CheckablePackageType(PackageType packageType, boolean checked) {
        this.packageType = packageType;
        this.checked = checked;
    }

    CheckablePackageType(PackageType packageType) {
        this.packageType = packageType;
        this.checked = false;
    }

    void checkString(String typeIDsString) {

        this.checked = false;
        String[] typeIDs = typeIDsString.split(",");
        for(String typeID:typeIDs) {
            if(typeID.equals(packageType.getId())) {
                checked = true;
                break;
            }
        }

    }

    public PackageType getPackageType() { return packageType; }

    public String getId() { return packageType.getId(); }

    public String getGroupType() { return packageType.getGroupType(); }

    public String getType() { return packageType.getType(); }

    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }

}
