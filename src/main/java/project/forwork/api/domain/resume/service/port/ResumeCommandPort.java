package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.model.Resume;

import java.util.List;


public interface ResumeCommandPort {
    Resume save(Resume resume);
    Resume approve(Resume resume, Long adminId);
    Resume deny(Resume resume, Long adminId);
    void delete(Resume resume, Long userId);
    void deleteAll(List<Resume> resumeList, Long userId);
}