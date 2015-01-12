package org.kesler.simplereg.logic.reception;

import org.kesler.simplereg.logic.ServiceState;

public interface ReceptionsModelStateListener {
	public void receptionsModelStateChanged(ServiceState state);
}