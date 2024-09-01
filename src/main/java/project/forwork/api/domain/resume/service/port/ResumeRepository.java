package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository {

    Resume save(Resume resume);

    void delete(Resume resume);

    Resume getById(Long id);

    Optional<Resume> findById(Long id);

    List<Resume> findAll();

    List<Resume> findAllBySeller(User seller);
}
