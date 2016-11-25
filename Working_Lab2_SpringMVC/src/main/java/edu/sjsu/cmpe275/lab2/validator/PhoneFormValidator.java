/**
 * 
 */
package edu.sjsu.cmpe275.lab2.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.service.PhoneService;

/**
 * @author SkandaBhargav
 *
 */
public class PhoneFormValidator implements Validator{

	@Autowired
	PhoneService phoneService;

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return PhoneEntity.class.equals(arg0);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		PhoneEntity pEntity = (PhoneEntity) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.phoneForm.number");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty.phoneForm.description");

		if(pEntity.getNumber()==null || pEntity.getNumber().length()!=10){
			errors.rejectValue("number", "Invalid.phoneForm.number");
		}
	}
}
