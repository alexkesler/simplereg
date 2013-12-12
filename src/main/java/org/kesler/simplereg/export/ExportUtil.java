package org.kesler.simplereg.export;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.JAXBElement;
import org.docx4j.wml.ContentAccessor;

class ExportUtil {

	public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {

		List<Object> result = new ArrayList<Object>();

		if (obj instanceof JAXBElement) obj = ((JAXBElement<?>)obj).getValue();

		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child: children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			} 
		}

		return result;

	}

}