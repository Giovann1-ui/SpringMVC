package app.service;

import app.model.AdresseModel;
import app.repository.AdresseRepository;
import org.springframework.stereotype.Service;
import annotation.Injection;

@Service
public class AdresseService {
    @Injection
    private AdresseRepository AdresseRepository;

    public AdresseService(AdresseRepository AdresseRepository) {
        this.AdresseRepository = AdresseRepository;
    }

    public AdresseModel getAdresseByStreetAndCityAndPostalCode(String street, String city, int postal_code) {
        return AdresseRepository.getByStreetAndCityAndPostalCode(street, city, postal_code);
    }

    public boolean adresseExists(String street, String city, int postal_code) {
        return AdresseRepository.existsByStreetAndCityAndPostalCode(street, city, postal_code);
    }

    public AdresseModel createAdresse(String street, String city, int postal_code) {
        AdresseModel Adresse = new AdresseModel();
        Adresse.setStreet(street);
        Adresse.setCity(city);
        Adresse.setPostalCode(postal_code);
        return AdresseRepository.save(Adresse);
    }

    public AdresseModel getAdresseById(Long id) {
        return AdresseRepository.findById(id).orElse(null);
    }

    public void deleteAdresse(Long id) {
        AdresseRepository.deleteById(id);
    }
}
