package projet.bid_pro.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;

import java.security.Principal;

@Controller
public class LoginController {

    private UtilisateurService utilisateurService;

    public LoginController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "loginError", required = false,defaultValue = "false") boolean error,
            @RequestParam(name = "logoutSuccess", required = false,defaultValue = "false") boolean logout,
            Model model) {
        return "login";
    }
    //LoginSuccessHandler


    @GetMapping("/register")
    public String register(Model model){
        // create model object to store form data
        Utilisateur user = new Utilisateur();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") Utilisateur userDto,
                               BindingResult result,
                               Model model){

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        utilisateurService.register(userDto);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
