package org.kesler.simplereg.util;

import javax.swing.ImageIcon;
import java.net.URL;

public class ResourcesUtil {

	public static ImageIcon getIcon(String name) {
		String path = "/images/" + name;
		URL imgURL = ResourcesUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("File not found: " + path);
			return null;
		}
	}

}