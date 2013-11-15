package org.kesler.simplereg.gui.reestr.filter;

import org.kesler.simplereg.logic.reception.Reception;

public class ResultInMFCReceptionsFilter implements ReceptionsFilter {

	private Boolean resultInMFC;

	public ResultInMFCReceptionsFilter(Boolean resultInMFC) {
		this.resultInMFC = resultInMFC;
	}

	public Boolean getResultInMFC() {
		return resultInMFC;
	}

	public void setResultInMFC(Boolean resultInMFC) {
		this.resultInMFC = resultInMFC;
	}

	@Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException("Reception argument can not be null");
		}

		Boolean receptionResultInMFC = reception.isResultInMFC();

		boolean fit = false;

		if (receptionResultInMFC != null && receptionResultInMFC == resultInMFC) {
			fit = true;			
		}		

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "";
		if (resultInMFC) {
			filterString = "Результат получать в МФЦ";
		} else {
			filterString = "Результат получать не в МФЦ";
		}

		return filterString;
	}

}