package app.repository;

import org.springframework.stereotype.Repository;
import app.model.AdresseModel;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AdresseRepository extends JpaRepository<AdresseModel, Long>{

    boolean existsByStreetAndCityAndPostalCode(String street, String city, int postal_code);

    AdresseModel getByStreetAndCityAndPostalCode(String street, String city, int postal_code);
}
