package org.kesler.simplereg.gui.main;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import java.beans.*;

import java.util.Properties;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.OptionsUtil;


/**
* Диалог настроек приложения
*/
public class OptionsDialog extends JDialog {
	private Properties options;

	private String[] propNamesArray;
	private List<JTextField> textFieds;

	private String btn1String = "Сохранить";
	private String btn2String = "Отмена";

	/**
	* Создает модальный диалог настроек программы
	* @param frame ссылка на родительское окно
	*/
	public OptionsDialog(JFrame frame) {
		super(frame, "Параметры приложения", true);
		options = OptionsUtil.getOptions();

		this.setContentPane(createOptionPane());

		this.pack();
		this.setLocationRelativeTo(frame);


	}

	/**
	* Открывает модальный диалог (ожидает закрытия окна диалога)
	*/
	public void showDialog() {
		setVisible(true);
	}

	private JOptionPane createOptionPane() {
		
		MigLayout layout = new MigLayout(
										"wrap 2",
										"[right][left]",
										"");

		JPanel dataPanel = new JPanel(layout);
		
        Set<String> propNamesSet = options.stringPropertyNames();
        
        propNamesArray = new String[propNamesSet.size()];
        propNamesArray = propNamesSet.toArray(propNamesArray);

        textFieds = new ArrayList<JTextField>();

        for (int i = 0; i < propNamesArray.length; i++) {
        	dataPanel.add(new JLabel(propNamesArray[i]));
        	JTextField textField = new JTextField(20);
        	textField.setText(options.getProperty(propNamesArray[i]));
        	textFieds.add(textField);
        	dataPanel.add(textField);
        }

        JOptionPane optionPane = new JOptionPane(dataPanel,
        										JOptionPane.PLAIN_MESSAGE,
        										JOptionPane.OK_CANCEL_OPTION,
        										null,
        										new String[] {btn1String, btn2String},
        										btn1String);

        optionPane.addPropertyChangeListener(new OptionsPropertyChangeListener());

        return optionPane;

	}

	private class OptionsPropertyChangeListener implements PropertyChangeListener {
		
		public void propertyChange(PropertyChangeEvent ev) {
			String prop = ev.getPropertyName();
			JOptionPane optionPane = (JOptionPane)(ev.getSource());

			if (JOptionPane.VALUE_PROPERTY.equals(prop) ||
				JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)) {

				Object value = optionPane.getValue();

				if (value == JOptionPane.UNINITIALIZED_VALUE) {
					return;
				}

				optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

				if (value == btn1String) {
					for (int i = 0; i < propNamesArray.length; i++) {
						options.setProperty(propNamesArray[i], textFieds.get(i).getText());
					}
					OptionsUtil.saveOptions();

					setVisible(false);
				}

				if (value == btn2String) {
					setVisible(false);
				}


				
			}
		}
	}


}