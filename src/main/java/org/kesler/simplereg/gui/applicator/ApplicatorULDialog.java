package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.logic.applicator.UL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;

public class ApplicatorULDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private int result;

	private JFrame frame;
	private ApplicatorUL applicatorUL;

	public ApplicatorULDialog (JFrame frame) {
		super(frame,"Заявитель - юридическое лицо", true);
		this.frame = frame;
		result = NONE;
		createGUI();

		this.applicatorUL = new ApplicatorUL();
	}

	public ApplicatorULDialog(JFrame frame, ApplicatorUL applicatorUL) {
		super(frame, "Заявитель - юридичееское лицо", true);
		this.frame = frame;
		result = NONE;

		this.applicatorUL = applicatorUL; 
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout());



		// Панель с кнопками
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = OK;
				setVisible(false);
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
		this.setLocationRelativeTo(frame);
	}

	public int getResult() {
		return result;
	}

	public ApplicatorUL getApplicatorUL() {
		return applicatorUL;
	}	
}