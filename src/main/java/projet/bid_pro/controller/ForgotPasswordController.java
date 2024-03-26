package projet.bid_pro.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    private JavaMailSender mailSender;

    private UtilisateurService utilisateurService;

    public ForgotPasswordController(JavaMailSender mailSender, UtilisateurService utilisateurService) {
        this.mailSender = mailSender;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

            utilisateurService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "\"Nous avons envoyé un lien de réinitialisation de mot de passe à votre adresse e-mail. Veuillez vérifier.");

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("eni@support.fr", "ENI Support");
        helper.setTo(recipientEmail);

        String subject = "Voici le lien pour réinitialiser votre mot de passe";

        String content = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>"
                + "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe :</p>"
                + "<p><a href=\"" + link + "\">Changer mon mot de passe</a></p>"
                + "<br>"
                + "<p>Ignorez cet e-mail si vous vous souvenez de votre mot de passe, "
                + "ou si vous n'avez pas effectué cette demande.</p>";


        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        Utilisateur customer = utilisateurService.findByResetPasswordToken(token);
        System.out.println(customer);
        model.addAttribute("token", token);

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Utilisateur utilisateur = utilisateurService.findByResetPasswordToken(token);
        model.addAttribute("title", "Réinitialiser votre mot de passe");

        if (utilisateur == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            utilisateurService.updatePassword(utilisateur, password);

            model.addAttribute("message", "Vous avez changé votre mot de passe avec succès.");
        }

        return "login";
    }

    public class Utility {
        public static String getSiteURL(HttpServletRequest request) {
            String siteURL = request.getRequestURL().toString();
            return siteURL.replace(request.getServletPath(), "");
        }
    }
}
