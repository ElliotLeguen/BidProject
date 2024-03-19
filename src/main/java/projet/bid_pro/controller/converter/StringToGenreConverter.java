//package projet.bid_pro.controller.converter;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import projet.bid_pro.bll.FilmService;
//
//@Component
//public class StringToGenreConverter implements Converter<String, Genre> {
//	// Injection des services
//	private FilmService service;
//
//	public StringToGenreConverter(FilmService service) {
//		this.service = service;
//	}
//	@Override
//	public Genre convert(String id) {
//		Long theId = Long.parseLong(id);
//		return service.consulterGenreParId(theId);
//	}
//}
