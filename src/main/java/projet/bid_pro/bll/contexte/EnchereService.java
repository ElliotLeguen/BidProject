package projet.bid_pro.bll.contexte;

import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> consulterEncheres();
    Enchere consulterEnchereParId(long id);
    void creerEnchere(Enchere enchere);

    //List<Genre> consulterGenres();

    //List<Participant> consulterParticipants();

    //Genre consulterGenreParId(long id);

    //Participant consulterParticipantParId(long id);

    //String consulterTitreFilm(long id);

    //void publierAvis(Avis avis, long idFilm);

    //List<Avis> consulterAvis(long idFilm);
}
