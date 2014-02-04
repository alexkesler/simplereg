package org.kesler.simplereg.gui.realty;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.event.DocumentEvent;


import com.alee.laf.list.WebList;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.popup.PopupStyle;
import com.alee.managers.popup.WebPopup;
import com.alee.utils.swing.DocumentChangeListener;
import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.fias.FIASModel;
import org.kesler.simplereg.fias.FIASModelListener;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyType;
import org.kesler.simplereg.logic.realty.RealtyTypesModel;

import org.kesler.simplereg.util.ResourcesUtil;

public class RealtyObjectDialog extends AbstractDialog implements FIASModelListener{


	private RealtyObject realtyObject;

	private JDialog parentDialog;

    private FIASModel fiasModel;


    private JComboBox realtyTypeComboBox;
	private WebTextField addressTextField;
    private WebList addressesList;
    private WebPopup addressPopup;

    private int waiterCount = 0;

	public RealtyObjectDialog(JDialog parentDialog) {
		super(parentDialog, "Объект недвижимости", true);
		this.parentDialog = parentDialog;
        fiasModel = new FIASModel(this);

		realtyObject = new RealtyObject();

		createGUI();
		loadGUIDataFromRealtyObject();
	}

	public RealtyObjectDialog(JDialog parentDialog, RealtyObject realtyObject) {
		super(parentDialog, "Изменить объект недвижимости", true);
		this.parentDialog = parentDialog;

		this.realtyObject = realtyObject;

		createGUI();
		loadGUIDataFromRealtyObject();

	}

	public RealtyObject getRealtyObject() {
		return realtyObject;
	}

	private void createGUI() {

		// основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fill"));


		realtyTypeComboBox = new JComboBox();

		List<RealtyType> realtyTypes = RealtyTypesModel.getInstance().getAllRealtyTypes();
		for (RealtyType realtyType: realtyTypes) {
			realtyTypeComboBox.addItem(realtyType);
		}


		addressTextField = new WebTextField(45);

        addressPopup = new WebPopup(PopupStyle.lightSmall);
        addressPopup.setRequestFocusOnShow(false);
        addressPopup.setSize(addressTextField.getWidth(), 50);

        addressTextField.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void documentChanged(DocumentEvent documentEvent) {
               computeAddressesByString(addressTextField.getText());
            }
        });

        addressTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAddress();
            }
        });

        addressTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_DOWN ||
                        e.getKeyCode()==KeyEvent.VK_UP) {
                    addressesList.requestFocusInWindow();
                    super.keyPressed(e);
                }

            }
        });

        addressesList = new WebList();
        addressesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addressesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) selectAddress();
            }
        });
        addressesList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) selectAddress();
            }
        });

        WebScrollPane addressesListScrollPane = new WebScrollPane(addressesList);

        addressPopup.add(addressesListScrollPane);
        addressPopup.setAnimated(true);

        //собираем панель данных
		dataPanel.add(new JLabel("Тип объекта"), "right");
		dataPanel.add(realtyTypeComboBox, "wrap");
		dataPanel.add(new JLabel("Адрес объекта"), "right");
		dataPanel.add(addressTextField, "wrap");


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
		this.pack();
		this.setLocationRelativeTo(parentDialog);
	}

	private void loadGUIDataFromRealtyObject() {

		RealtyType type = realtyObject.getType();
		if (type != null) {
			realtyTypeComboBox.setSelectedItem(type);
		} else {
			realtyTypeComboBox.setSelectedIndex(-1);
		}

		addressTextField.setText(realtyObject.getAddress());

	}

	private boolean readRealtyObjectFromGUIData() {

		if (realtyTypeComboBox.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "Необходимо выбрать тип объекта", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;			
		}

		if (addressTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Поле адрес не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		RealtyType type = (RealtyType) (realtyTypeComboBox.getSelectedItem());

		realtyObject.setType(type);
		realtyObject.setAddress(addressTextField.getText());

		return true;
	}


    private void computeAddressesByString(final String searchString) {

        // реализуем ожидание 1 сек пока не введем все
        Thread waiter = new Thread(new Runnable() {
            @Override
            public void run() {
                waiterCount++;
                try {
                    Thread.sleep(700);
                } catch (Exception e) {}
                // если по окончании ожидания больше клавиш не нажато - запускаемся
                System.out.println("WaiterCount = " + waiterCount + " Search: " + searchString);
                if (waiterCount < 2) fiasModel.computeAddressesInSeparateThread(searchString);
                waiterCount--;
            }
        });

        waiter.start();


//        List<String> addresses = FIASModel.getInstance().computeAddress(searchString);
//        mainView.setAddressVariants(addresses);
    }

    private void selectAddress() {
        addressTextField.setText((String)addressesList.getSelectedValue());
        addressTextField.requestFocus();
    }


    @Override
    public void addresesFiltered(List<String> addresses) {
        addressesList.setListData(addresses.toArray());

        if(addresses.size() > 0) {
            addressesList.setSelectedIndex(0);
            if(!addressPopup.isShowing()){
//                addressPopup.packPopup();
                addressPopup.showPopup(addressTextField,0,addressTextField.getHeight());
            }
        }
        else addressPopup.hidePopup();
    }


}