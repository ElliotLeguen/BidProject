package projet.bid_pro.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public String registration(@Valid @ModelAttribute("user") Utilisateur newUser,
                               BindingResult result,
                               Model model){

        if(result.hasErrors()){
            model.addAttribute("user", newUser);
            return "/register";
        }
        newUser.setCredit(100);
        Utilisateur registeredUser = utilisateurService.register(newUser);
        List<GrantedAuthority> authorities = new ArrayList<>();

// Ajouter le rôle approprié en fonction de l'attribut isAdmin
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(registeredUser.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
