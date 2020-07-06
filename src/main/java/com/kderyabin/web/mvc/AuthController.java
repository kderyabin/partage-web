package com.kderyabin.web.mvc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.core.storage.entity.UserEntity;
import com.kderyabin.core.storage.repository.UserRepository;
import com.kderyabin.web.dto.Signin;
import com.kderyabin.web.dto.SignupDTO;
import com.kderyabin.web.validator.SigninValidator;
import com.kderyabin.web.validator.SignupValidator;

@Controller
public class AuthController {
	final private Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private StorageManager storageManager;

	@Autowired
	public void setStorageManager(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	

	/**
	 * Display sign in form.
	 * 
	 * @return
	 */
	@GetMapping("{lang}/signin")
	public ModelAndView displaySignin() {
		ModelAndView modelAndView = new ModelAndView("signin");

		return modelAndView;
	}

	/**
	 * Validate authentication form.
	 * 
	 * @param model
	 * @param viewModel
	 * @param request
	 * @return View name or redirect command.
	 */
	@PostMapping("{lang}/signin")
	public String authenticate(@ModelAttribute Signin model, Model viewModel, HttpServletRequest request) {

		viewModel.addAttribute("login", model.getLogin());
		SigninValidator validator = new SigninValidator();
		validator.validate(model);
		if(validator.isValid()) {
			LOG.info(model.toString());
			UserModel foundUser = storageManager.findUserByLoginPassword(
					model.getLogin(), 
					getHashedPassword(model.getPwd().trim())
					);

			if (foundUser == null) {
				viewModel.addAttribute("isFailedAuth", true);
				return "signin";
			} else {
				LOG.info("User found");
				LOG.info(foundUser.toString());
			}

		}
//		if (model.getLogin() == null || model.getPwd() == null) {
//			viewModel.addAttribute("isFailedAuth", true);
//			return "login";
//		}
		
//		HttpSession session = request.getSession();
//		session.setAttribute("currentUser", foundUser);

//		return "redirect:/sharings";
		return "signin"; 
	}

	/**
	 * Displays subscribe form
	 * 
	 * @return
	 */
	@GetMapping("{lang}/signup")
	public ModelAndView displaySignup() {
		ModelAndView modelAndView = new ModelAndView("signup");

		return modelAndView;
	}

	/**
	 * Validates sign up form data, creates user and sends validation email.
	 * 
	 * @return "subscribe" view with highlighted errors or "confirmation" view
	 */
	@PostMapping("{lang}/signup")
	public ModelAndView register( @ModelAttribute SignupDTO dto ) {
		
		ModelAndView modelAndView = new ModelAndView("signup");
//		// Validate form fields
		SignupValidator validator = new SignupValidator();
		validator.validate(dto);

		if (validator.isValid()) {
			// Check if login is in use
			UserModel userModel = new UserModel(dto.getLogin(), null);
			boolean exists = storageManager.isUserExists( userModel );
			LOG.info("User found status: " + exists);
			if(!exists) {
				// Complete the model and save.
				String hashPwd = getHashedPassword( dto.getPwd().trim());
				userModel.setPwd(hashPwd);
				userModel.generateId();
				userModel = storageManager.save(userModel);
				LOG.debug(userModel.toString());
			} else {
				validator.addMessage("login", "error.email_in_use");
			}
			//return "redirect/";
		}

		modelAndView.addObject("login", dto.getLogin());
		modelAndView.addObject("pwd", dto.getPwd());
		modelAndView.addObject("confirmPwd", dto.getConfirmPwd());
		if (!validator.isValid()) {
			modelAndView.addObject("errors", validator.getMessages());
		}

		return modelAndView;
	}
	
	/**
	 *  Generates a a SHA-256 hash of the user password.
	 *  @return Hexadecimal value
	 */
	private String getHashedPassword(String pwd) {
		MessageDigest digest;
		String salt = "HSIjedufjf9é)@";
		StringBuffer hexString = new StringBuffer();
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(salt.getBytes(StandardCharsets.UTF_8));
			byte[] hash = digest.digest( pwd.getBytes(StandardCharsets.UTF_8));
			// Convert to hex value
			for(byte aByte: hash){
				hexString.append(String.format("%02x", aByte));
			}
//		    for (int i = 0; i < hash.length; i++) {
//			    String hex = Integer.toHexString(0xff & hash[i]);
//			    if(hex.length() == 1) { 
//			    	hexString.append('0');
//			    }
//			    hexString.append(hex);
//		    }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}

}
