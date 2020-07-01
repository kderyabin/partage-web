package com.kderyabin.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kderyabin.core.model.UserModel;
import com.kderyabin.core.services.StorageManager;

@Controller
public class AuthController {
	
	
	private StorageManager storageManager;
	
//	@Autowired
//	public void setStorageManager(StorageManager storageManager) {
//		this.storageManager = storageManager;
//	}

	/**
	 * Display authentication form
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");

		return modelAndView;
	}
	/**
	 * Validate authentication form.
	 * @param model
	 * @param viewModel
	 * @param request
	 * @return
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
	 

}
