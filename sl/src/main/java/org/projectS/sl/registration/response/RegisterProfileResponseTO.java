package org.projectS.sl.registration.response;

import org.projectS.sl.Master.MasterResponse;

public final class RegisterProfileResponseTO extends MasterResponse {
	
	private boolean registered = false;
	
	public RegisterProfileResponseTO(String trckId){
		super(trckId);
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
}
