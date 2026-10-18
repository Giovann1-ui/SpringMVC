package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import app.model.FichierModel;

@Repository
public interface FichierRepository extends JpaRepository<FichierModel, Long> {

    @Query("select new app.model.FichierModel(f.id, f.nom, f.extension) from FichierModel f")
    List<FichierModel> listerSansContenu();
}