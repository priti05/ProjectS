package org.projectS.sl.registration;

import org.projectS.dbo.error.exception.ProjectSException;
import org.projectS.sl.registration.request.RegisterProfileRequestTO;
import org.projectS.sl.registration.response.RegisterProfileResponseTO;

public interface IRegistration {
	
	public RegisterProfileResponseTO register(RegisterProfileRequestTO registerProfileRequestTO) throws ProjectSException;

}
