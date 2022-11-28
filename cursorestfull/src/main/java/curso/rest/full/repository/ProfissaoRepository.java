package curso.rest.full.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import curso.rest.full.model.Profissao;

@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {

}
