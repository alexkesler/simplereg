package org.kesler.simplereg.gui.realty;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.fias.FIASModel;
import org.kesler.simplereg.fias.FIASModelListener;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyType;
import org.kesler.simplereg.logic.realty.RealtyTypesModel;

import org.kesler.simplereg.util.ResourcesUtil;

public class RealtyObjectDialog extends AbstractDialog implements FIASModelListener{


	private final RealtyObject realtyObject;


//    private FIASModel fiasModel;


    private JComboBox realtyTypeComboBox;
//	private JComboBox addressComboBox;
    private JTextField addressTextField;

    private String currentAddress;

    private int waiterCount = 0;

	public RealtyObjectDialog(JDialog parentDialog) {
		super(parentDialog, "Объект недвижимости", true);
//        fiasModel = new FIASModel(this);

		realtyObject = new RealtyObject();

		createGUI();
        setLocationRelativeTo(parentDialog);
		loadGUIDataFromRealtyObject();
	}

	public RealtyObjectDialog(JDialog parentDialog, RealtyObject realtyObject) {
		super(parentDialog, "Изменить объект недвижимости", true);
//        fiasModel = new FIASModel(this);

        this.realtyObject = realtyObject;

		createGUI();
        setLocationRelativeTo(parentDialog);
		loadGUIDataFromRealtyObject();

	}

	public RealtyObject getRealtyObject() {
		return realtyObject;
	}

	private void createGUI() {

		// основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fillx"));


		realtyTypeComboBox = new JComboBox();

		List<RealtyType> realtyTypes = RealtyTypesModel.getInstance().getAllRealtyTypes();
		for (RealtyType realtyType: realtyTypes) {
			realtyTypeComboBox.addItem(realtyType);
		}


        addressTextField = new JTextField();

//		addressComboBox = new JComboBox();
//        addressComboBox.setEditable(true);
//        addressComboBox.setSize(200,addressComboBox.getHeight());
//        addressComboBox.setMaximumRowCount(10);
//
//        final JTextField addressComboBoxEditor = (JTextField) addressComboBox.getEditor().getEditorComponent();
//
//        addressComboBoxEditor.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if(e.getLength()==1) {
//                    currentAddress = addressComboBoxEditor.getText();
//                    computeAddressesByString(currentAddress);
//                }
//
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                if (e.getLength()==1) {
//                    currentAddress = addressComboBoxEditor.getText();
//                    computeAddressesByString(currentAddress);
//                }
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//
//            }
//        });


        //собираем панель данных
		dataPanel.add(new JLabel("Тип объекта"), "right");
		dataPanel.add(realtyTypeComboBox, "wrap");
		dataPanel.add(new JLabel("Адрес объекта"), "right");
        dataPanel.add(addressTextField, "pushx, growx, wrap");
//		dataPanel.add(addressComboBox, "pushx, growx, wrap");


		// панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (readRealtyObjectFromGUIData()) {
					result = OK;
					setVisible(false);
				}
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.setSize(600,150);
	}

	private void loadGUIDataFromRealtyObject() {

		RealtyType type = realtyObject.getType();
		if (type != null) {
			realtyTypeComboBox.setSelectedItem(type);
		} else {
			realtyTypeComboBox.setSelectedIndex(-1);
		}

        addressTextField.setText(realtyObject.getAddress());

//		addressComboBox.setSelectedItem(realtyObject.getAddress());

	}

	private boolean readRealtyObjectFromGUIData() {

		if (realtyTypeComboBox.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "Необходимо выбрать тип объекта", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;			
		}

//        String address = (String) addressComboBox.getSelectedItem();
        String address  = addressTextField.getText();

        if (address == null || address.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Поле адрес не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		RealtyType type = (RealtyType) (realtyTypeComboBox.getSelectedItem());

		realtyObject.setType(type);
		realtyObject.setAddress(address);

		return true;
	}


//    private void computeAddressesByString(final String searchString) {
//
//        // реализуем ожидание 1 сек пока не введем все
//        Thread waiter = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                waiterCount++;
//                try {
//                    Thread.sleep(700);
//                } catch (Exception e) {}
//                // если по окончании ожидания больше клавиш не нажато - запускаемся
//                System.out.println("WaiterCount = " + waiterCount + " Search: " + searchString);
//                if (waiterCount < 2) fiasModel.computeAddressesInSeparateThread(searchString);
//                waiterCount--;
//            }
//        });
//
//        waiter.start();
//
//    }

//    private void selectAddress() {
//        addressTextField.setText((String)addressesList.getSelectedValue());
//        addressTextField.requestFocus();
//    }


    @Override
    public void addresesFiltered(List<String> addresses) {

//        addressComboBox.removeAllItems();
//        for(String address: addresses) {
//            addressComboBox.addItem(address);
//        }
////        addressComboBox.setSelectedIndex(-1);
//        addressComboBox.setSelectedItem(currentAddress);
//        JTextComponent editor = (JTextComponent) addressComboBox.getEditor().getEditorComponent();
//        editor.setCaretPosition(currentAddress.length());
//
//        if (addresses.size() > 0 /*&& !addressComboBox.isPopupVisible()*/) {
//            addressComboBox.showPopup();
//        } else {
//            addressComboBox.hidePopup();
//        }

    }


}