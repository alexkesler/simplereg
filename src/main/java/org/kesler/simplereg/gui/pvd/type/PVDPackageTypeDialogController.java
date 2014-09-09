package org.kesler.simplereg.gui.pvd.type;

import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.PackageType;
import org.kesler.simplereg.pvdimport.support.PackageTypesReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
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
    }

    List<CheckablePackageType> getCheckablePackageTypes() {return checkablePackageTypes;}

    public String showDialog(JDialog parentDialog, String typeIDsString) {
        this.typeIDsString = typeIDsString;
        checkablePackageTypes.clear();
        dialog = new PVDPackageTypeDialog(parentDialog, this);

        packageTypesReader.readInSeparateThread();

        dialog.setVisible(true);
        dialog.dispose();
        if (dialog.getResult() == AbstractDialog.OK)
            loadTypeIDsString();

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


        for(PackageType packageType:newTypes)
            checkablePackageTypes.add(new CheckablePackageType(packageType));

        clearCheckedInOtherServices();
        updateChecks();

        dialog.update();
    }

    private void updateChecks() {
        for (CheckablePackageType checkablePackageType:checkablePackageTypes) {
            checkablePackageType.setChecked(false);
            if (checkablePackageType.fitString(typeIDsString)) checkablePackageType.setChecked(true);
        }
    }

    private void clearCheckedInOtherServices() {
        List<Service> services = ServicesModel.getInstance().getActiveServces();
        StringBuilder allTypeIDsStringBuilder = new StringBuilder();
        for(Service service:services) {
            String typeIDs = service.getPkpvdTypeIDs();
            if(!typeIDs.isEmpty()) {
                if(allTypeIDsStringBuilder.length()!=0)
                    allTypeIDsStringBuilder.append(",");
                allTypeIDsStringBuilder.append(typeIDs);
            }
        }
        String allTypesIDs = allTypeIDsStringBuilder.toString();
        Iterator<CheckablePackageType> iterator = checkablePackageTypes.iterator();
        while (iterator.hasNext()) {
            CheckablePackageType type = iterator.next();
            if (type.fitString(allTypesIDs) && !type.fitString(typeIDsString)) iterator.remove();
        }
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

    boolean fitString(String typeIDsString) {

        String[] typeIDs = typeIDsString.split(",");
        for(String typeID:typeIDs) {
            if(typeID.equals(packageType.getId())) {
                return true;
            }
        }
        return false;

    }


    public PackageType getPackageType() { return packageType; }

    public String getId() { return packageType.getId(); }

    public String getGroupType() { return packageType.getGroupType(); }

    public String getType() { return packageType.getType(); }

    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }

}
