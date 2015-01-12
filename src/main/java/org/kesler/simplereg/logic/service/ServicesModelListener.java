package org.kesler.simplereg.logic.service;

import org.kesler.simplereg.logic.ServiceState;

public interface ServicesModelListener {
	public void modelStateChanged(ServiceState state);
}