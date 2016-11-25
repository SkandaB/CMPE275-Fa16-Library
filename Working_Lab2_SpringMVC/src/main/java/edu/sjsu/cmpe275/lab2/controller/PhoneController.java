package edu.sjsu.cmpe275.lab2.controller;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import edu.sjsu.cmpe275.lab2.entity.AddressEntity;
import edu.sjsu.cmpe275.lab2.entity.PhoneEntity;
import edu.sjsu.cmpe275.lab2.entity.UserEntity;
import edu.sjsu.cmpe275.lab2.service.PhoneService;
import edu.sjsu.cmpe275.lab2.service.UserSerivce;
import edu.sjsu.cmpe275.lab2.validator.PhoneFormValidator;


/**
 * @author SkandaBhargav
 * Controller class to handle the various inbounds requests
 */
@Controller
public class PhoneController {
	@Autowired
	private	HttpServletResponse httpResponse;

	@Autowired
	private PhoneService pService;

	@Autowired
	private UserSerivce uService;

	@RequestMapping(value = "/phone", method = RequestMethod.GET)
	public ModelAndView showPhoneCreationForm(){
		ModelAndView modelAndView = new ModelAndView("phones/addPhone");
		return modelAndView;
	}
	@RequestMapping(value = "/phoneCreate", method = RequestMethod.GET)
	public ModelAndView showPhoneCreation(){
		System.out.println("in showPhoneCreation");
		ModelAndView modelAndView = new ModelAndView("phones/addPhone");
		return modelAndView;
	}
	@RequestMapping(value="/phone", method=RequestMethod.POST)
	public Object phoneCreation(
			@RequestParam String number,
			@RequestParam String description,
			@RequestParam String city,
			@RequestParam String state,
			@RequestParam String street,
			@RequestParam String zip_code,
			ModelMap model,
			HttpServletRequest  request,
			HttpServletResponse  response) {
		ModelAndView mv = new ModelAndView("phones/addPhone");
		if(number.isEmpty() || description.isEmpty() || city.isEmpty() ||
				state.isEmpty() || street.isEmpty() || zip_code.isEmpty()){
			mv.addObject("errorMessage", "* Please enter all the missing values");
			return mv;
		}
		if(number.length()!=10 ){
			mv.addObject("errorMessage", "* Phone Number invalid");
			return mv;
		}
		if(zip_code.length()!=5){
			mv.addObject("errorMessage", "* Zip Code invalid");
			return mv;
		}
		PhoneEntity pEntity = pService.createUser(number,description,city,state,street,zip_code);
		String redirect = "http://8.35.192.11//lab2-1.0/phone/"+pEntity.getId().toString();
		try{
			response.sendRedirect(redirect);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@RequestMapping(value = "/phone/{pid}", method = RequestMethod.GET)
	public Object showPhone(@PathVariable("pid") Integer id,
			@RequestParam(value="json",required = false, defaultValue="false") String json) {
		PhoneEntity pEntity = pService.findById(id);
		List<UserEntity> users = uService.findAll();
		List<UserEntity> ret = pService.retrieveUsers(id);

		if(null!= pEntity)
		{
			if(json.equals("true")) {
				System.out.println("Data in JSON ****************");
				ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
				httpResponse.setStatus(HttpStatus.OK.value());
				mv.addObject(pEntity);
				mv.addObject(users);
				mv.addObject(ret);
				return mv;
			} 
			System.out.println(" **************** Returning the normal model view ****************");
			ModelAndView mv = new ModelAndView("phones/phoneInfo");
			httpResponse.setStatus(HttpStatus.OK.value());
			mv.addObject("phone", pEntity);
			mv.addObject("users",users);
			mv.addObject("ret",ret);
			return mv;
		}
		else{
			ModelAndView modelAndView = new ModelAndView("error");
			String noUser = "Phone " +id + " Not found";
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
			modelAndView.addObject("errorMessage", noUser);
			modelAndView.addObject("responseCode", HttpStatus.NOT_FOUND.value());

			return modelAndView;
		}

	}

	@RequestMapping(value = "/phone/{pid}", method = RequestMethod.DELETE)
	public ModelAndView deleteUser(@PathVariable("pid") Integer id){
		boolean status = pService.deleteById(id);

		System.out.println("Came back after deleting the phone");
		if(status){
			System.out.println("redirecting-------------");
			ModelAndView modelAndView = new ModelAndView("phones/addPhone");
			return modelAndView;	
		}else{	
			System.out.println("redirecting 233-------------");
			ModelAndView modelAndView = new ModelAndView("phones/addPhone");
			return modelAndView;
		}
	}

	@RequestMapping(value="/phone/{pid}", method = RequestMethod.POST)
	public String updatePhone(@PathVariable("pid") Integer id,
			@RequestParam String number,
			@RequestParam String description,
			@RequestParam String city,
			@RequestParam String state,
			@RequestParam String street,
			@RequestParam String zip_code,
			@RequestParam String uid,
			ModelMap model,
			HttpServletRequest  request,
			HttpServletResponse  response ) {
		System.out.println("Check the contents of returned user IDS "+uid);
		PhoneEntity pEntity = pService.updatePhone((Integer)id,number,description,city,state,street,zip_code,uid);

		List<UserEntity> checkd = pService.retrieveUsers(id);
		/*ModelAndView modelAndView = new ModelAndView("users/userInfo");
		modelAndView.addObject("user", uEntity);
		return modelAndView;*/
		return "http://8.35.192.11//lab2-1.0/phone/"+pEntity.getId().toString();

	}

}

/*if (phoneService == null) {
	model.addAttribute("css", "danger");
	model.addAttribute("msg", "Phone not found");
}*/