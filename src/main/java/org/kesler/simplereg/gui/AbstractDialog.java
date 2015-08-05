package org.kesler.simplereg.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class AbstractDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	protected JDialog currentDialog;

	protected int result = NONE;

	public int getResult() {
		return result;
	}

	protected AbstractDialog(JDialog parentDialog, boolean modal) {
		super(parentDialog, modal);
		currentDialog = this;
	}

	protected AbstractDialog(JFrame parentFrame, boolean modal) {
		super(parentFrame, modal);
		currentDialog = this;
	}

	protected AbstractDialog(JDialog parentDialog, String name, boolean modal) {
		super(parentDialog, name, modal);
		currentDialog = this;
	}

	protected AbstractDialog(JFrame parentFrame, String name, boolean modal) {
		super(parentFrame, name, modal);
		currentDialog = this;
	}


}