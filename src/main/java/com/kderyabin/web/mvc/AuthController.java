package com.kderyabin.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.core.storage.repository.UserRepository;
import com.kderyabin.web.dto.SignupDTO;
import com.kderyabin.web.validator.SignupValidator;

@Controller
public class AuthController {
	final private Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private StorageManager storageManager;
	private UserRepository userRepository;

//	@Autowired
//	public void setStorageManager(StorageManager storageManager) {
//		this.storageManager = storageManager;
//	}

	/**
	 * Display sign in form.
	 * 
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");

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
	@PostMapping("/")
	public String authenticate(@ModelAttribute UserModel model, Model viewModel, HttpServletRequest request) {

		viewModel.addAttribute("login", model.getLogin());
		if (model.getLogin() == null || model.getPwd() == null) {
			viewModel.addAttribute("isFailedAuth", true);
			return "login";
		}
		UserModel foundUser = storageManager.findUserByLoginPassword(model.getLogin(), model.getPwd());

		if (foundUser == null) {
			viewModel.addAttribute("isFailedAuth", true);
			return "login";
		}

		HttpSession session = request.getSession();
		session.setAttribute("currentUser", foundUser);

		return "redirect:/sharings";
	}

	/**
	 * Displays subscribe form
	 * 
	 * @return
	 */
	@GetMapping("{lang}/subscribe")
	public ModelAndView subscribe(@PathVariable String lang, HttpServletRequest request) {
		request.setAttribute("lang", lang);

		ModelAndView modelAndView = new ModelAndView("subscribe");

		return modelAndView;
	}

	/**
	 * Validates sign up form data, creates user and sends validation email.
	 * 
	 * @return "subscribe" view with highlighted errors or "confirmation" view
	 */
	@PostMapping("{lang}/subscribe")
	public ModelAndView validateSubscription(@PathVariable String lang, @ModelAttribute SignupDTO dto,
			HttpServletRequest request) {
//		// Validate form fields
		SignupValidator validator = new SignupValidator();
		validator.validate(dto);

		if (validator.isValid()) {
			// Check if login is in use
			
		}

		request.setAttribute("lang", lang);

		ModelAndView modelAndView = new ModelAndView("subscribe");
		modelAndView.addObject("login", dto.getLogin());
		modelAndView.addObject("pwd", dto.getPwd());
		modelAndView.addObject("confirmPwd", dto.getConfirmPwd());
		if (!validator.isValid()) {
			modelAndView.addObject("errors", validator.getMessages());
		}

		return modelAndView;
	}

}
