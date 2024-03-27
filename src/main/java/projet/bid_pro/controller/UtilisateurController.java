package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;


import java.security.Principal;
import java.util.List;

@Controller
//Injection de la liste des attributs en session
@SessionAttributes({"UtilisateurEnSession"})
public class UtilisateurController {


    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // Cette méthode met par défaut un nouveau utilisateur en session
    @ModelAttribute("UtilisateurEnSession")
    public Utilisateur membreEnSession(Principal principal) {
        return utilisateurService.charger(principal.getName());
    }

    @GetMapping("/loginSuccessHandler")
    public String loginSuccess() {
        return "redirect:/";
    }

    @GetMapping("/profil")
    public String afficherProfil(Principal principal, Model model) {
        var test = utilisateurService.charger(principal.getName());
        model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
        return "profil";
    }


    @GetMapping("/profilEdit")
    public String afficherProfilEdit(Principal principal, Model model) {

        model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
        return "profilEdit";
    }

    @PostMapping("/profilEdit")
    public String profilEdit(@Valid @ModelAttribute("userEdit") Utilisateur userEdit,
                             BindingResult result,
                             @RequestParam("motDePasseActuel") String motDePasseActuel,
                             @RequestParam("motDePasseNouveau") String motDePasseNouveau,
                             @RequestParam("motDePasseNouveauConfirm") String motDePasseNouveauConfirm,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userEdit", userEdit);
            return "profilEdit";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(motDePasseActuel, userEdit.getMotDePasse());
        if(matches){
            if(motDePasseNouveau.equals(motDePasseNouveauConfirm)){

                userEdit.setMotDePasse(motDePasseNouveau);
            }else {
                ObjectError error = new ObjectError("globalError", "Les nouveaux mots de passe ne correspondent pas");
                result.addError(error);
                return "/profilEdit";
            }

        }else{
            model.addAttribute("userEdit", userEdit);
            ObjectError error = new ObjectError("globalError", "Le mot de passe actuel est incorrect");
            result.addError(error);

            return "/profilEdit";
        }


        utilisateurService.edit(userEdit);
        return "profil";
    }

    @GetMapping("/deleteUtilisateur")
    public String afficherProfilEnchere(@RequestParam(name = "idUtilisateur", required = true) int idUtilisateur, Model model, HttpSession session) {
        utilisateurService.delete(idUtilisateur);
        session.invalidate();
        return "index";
    }

    @GetMapping("/gestionUtilisateur")
    public String gestionUtilisateur(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.consulterUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "admin/gestionUtilisateur";
    }

    @GetMapping("/utilisateurEtat")
    public String afficherProfilEnchere( @RequestParam(name = "idUtilisateur", required = true) int idUtilisateur,Model model) {
        utilisateurService.changeEtat(idUtilisateur);
        return "redirect:/gestionUtilisateur";
    }

    @GetMapping("/utilisateurSupprimer")
    public String utilisateurSupprimer( @RequestParam(name = "idUtilisateur", required = true) int idUtilisateur,Model model) {
        utilisateurService.delete(idUtilisateur);
        return "redirect:/gestionUtilisateur";
    }

    @GetMapping("/acheterCredits")
    public String acheterCredits(Principal principal, Model model) {

        model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
        return "acheterCredits";
    }

    @PostMapping("/acheterCredits")
    public String acheterCredits(@ModelAttribute("credit") int credit,Principal principal) {
        Utilisateur utilisateur = utilisateurService.charger(principal.getName());
        utilisateur.setCredit(utilisateur.getCredit()+credit);
        utilisateurService.ajouterCredit(utilisateur);
        return "redirect:/profil";
    }
}
