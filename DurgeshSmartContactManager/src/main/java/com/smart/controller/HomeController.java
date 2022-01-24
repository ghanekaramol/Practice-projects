package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About - Smart contact Manager");
		System.out.println("about handler worked");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register - Smart contact Manager");
		m.addAttribute("user", new User());
		System.out.println("Signup handler worked");
		return "signup";	
	}
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,@RequestParam(value="agreement",
									defaultValue ="false") boolean agreement, Model m, HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("You have to agree the terms and conditions");
				throw new Exception("You have to agree the terms and conditions");
			}
			
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassoword(passwordEncoder.encode(user.getPassoword()));
			
			System.out.println("Agreement " +agreement);
			System.out.println("USER" + user);
			
			User result = this.userRepository.save(user);
			System.out.println(result);
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfuly Registered!!", "alert-success"));
			return "signup";
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something wenr wrong!!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
		
	}
	
}
