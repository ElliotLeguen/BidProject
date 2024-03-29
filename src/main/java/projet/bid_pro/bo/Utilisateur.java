package projet.bid_pro.bo;

import jakarta.validation.constraints.*;


public class Utilisateur {

    @NotNull
    private int noUtilisateur;
    @NotBlank
    @NotNull
    @Size(max = 30)
    private String pseudo;
    @NotBlank
    @NotNull
    @Size(max = 30)
    private String nom;
    @NotBlank
    @NotNull
    @Size(max = 30)
    private String prenom;
    @NotBlank
    @NotNull
    @Size(max = 30)
    @Email
    private String email;

    @NotBlank
    @NotNull
    @Size(max = 15)
    private String telephone;
    @NotBlank
    @NotNull
    @Size(max = 30)
    private String rue;
    @NotBlank
    @NotNull
    @Size(max = 10)
    private String codePostal;
    @NotBlank
    @NotNull
    @Size(max = 30)
    private String ville;
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String motDePasse;

    private String confirmationMotDePasse;
    @NotNull
    private int credit = 100;

    private String administrateur;

    private byte etat;

    private String reset_password_token;
    public Utilisateur() {

    }

    // Constructeur
    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email,
                       String telephone, String rue, String codePostal, String ville,
                       String motDePasse ,int credit,String confirmationMotDePasse,byte etat) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.motDePasse = motDePasse;
        this.credit = credit;
        this.confirmationMotDePasse = confirmationMotDePasse;
        this.etat=etat;
    }

    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email,
                       String telephone, String rue, String codePostal, String ville,byte etat) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.etat = etat;
    }
    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email,
                       String telephone, String rue, String codePostal, String ville,int credit,byte etat) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.credit = credit;
        this.etat = etat;
    }
    // Getters et Setters
    public int getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(int noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getConfirmationMotDePasse() {
        return confirmationMotDePasse;
    }
    public void setConfirmationMotDePasse(String confirmationMotDePasse) {
        this.confirmationMotDePasse = confirmationMotDePasse;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String isAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(String administrateur) {
        this.administrateur = administrateur;
    }

    public byte getEtat() {
        return etat;
    }

    public void setEtat(byte etat) {
        this.etat = etat;
    }

    public String getReset_password_token() {
        return reset_password_token;
    }

    public void setReset_password_token(String reset_password_token) {
        this.reset_password_token = reset_password_token;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "no_utilisateur=" + noUtilisateur +
                ", pseudo='" + pseudo + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", credit=" + credit +
                ", administrateur=" + administrateur +
                ", etat=" + etat +
                '}';
    }
    public boolean passwordsMatch() {
        // Vérifier si les deux champs de mot de passe correspondent
        return motDePasse != null && motDePasse.equals(confirmationMotDePasse);
    }

}