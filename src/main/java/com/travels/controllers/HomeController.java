package com.travels.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travels.models.Booking;
import com.travels.models.Payment;
import com.travels.models.User;
import com.travels.services.BookingService;
import com.travels.services.UserService;

@Controller
public class HomeController {
	
	@Autowired UserService uservice;
	@Autowired BookingService bservice;
	@Autowired HttpSession session;

	@GetMapping("/")
	public String homepage() {
		return "index";
	}
	
	@GetMapping("/login")
	public String loginpage() {
		return "login";
	}
	
	@PostMapping("/login")
	public String validate(String userid,String pwd,RedirectAttributes ra) {
		User user=uservice.ValidateLogin(userid, pwd);
		if(user==null) {
			ra.addFlashAttribute("error", "Invalid username or password");
			return "redirect:/login";
		}else {
			session.setAttribute("userid", user.getUserid());
			session.setAttribute("uname", user.getUname());
			session.setAttribute("role", user.getRole());
			return "redirect:/packages";			
		}
	}
	
	@GetMapping("/register")
	public String registerpage() {
		return "register";
	}
	
	@PostMapping("/register")
	public String saveuser(User user,RedirectAttributes ra) {
		ra.addFlashAttribute("msg", "User registered successfully");
		uservice.saveUser(user);
		return "redirect:/login";
	}
	
	@GetMapping("/hotels")
	public String hotels() {
		return "hotels";
	}
	
	@GetMapping("/book")
	public String bookings(String tour,RedirectAttributes ra) {
		if(session.getAttribute("userid") == null) {
			ra.addFlashAttribute("error", "Please login first for booking");
			return "redirect:/login";
		}
		return "bookings";
	}
	
	@PostMapping("/book")
	public String savebooking(Booking book) {
		System.out.println(book);
		bservice.saveBooking(book);
		book.setBid(bservice.getLastId());
		return "redirect:/payment?bid="+book.getBid();
	}
	
	@GetMapping("/payment")
	public String payments(int bid) {
		return "payment";
	}
	
	@PostMapping("/payment")
	public String savePayment(Payment pmt) {
		bservice.savePayment(pmt);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/places")
	public String places() {
		return "places";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		String userid=session.getAttribute("userid").toString();
		model.addAttribute("list", bservice.getMyBookings(userid));
		return "dashboard";
	}
	
	@GetMapping("/bookings")
	public String bookings(Model model) {
		model.addAttribute("list", bservice.getMyBookings());
		return "allbookings";
	}
	
	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("list", uservice.getAllUsers());
		return "users";
	}
	
	@GetMapping("/packages")
	public String packages() {
		return "packages";
	}
	
	@GetMapping("/flights")
	public String flights() {
		return "flights";
	}
	
	@GetMapping("/logout")
	public String logout(){
		session.invalidate();
		return "redirect:/";
	}
}
