package org.fermat.fermat_job_api.layer.middleware.resume.interfaces;

import org.fermat.fermat_job_api.all_definition.interfaces.Resume;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface ResumeManager {

    /**
     * This method returns a list of resume by a given actor public key.
     * @param actorPublicKey
     * @return
     */
    List<Resume> getResumeListByActorPublicKey(String actorPublicKey);

    /**
     * This method returns the resume by a given resume id.
     * @param resumeId
     * @return
     */
    Resume getResumeById(UUID resumeId);

    /**
     * This method create and returns a resume.
     * TODO: to define all the data to include in the resume.
     * @param actorPublicKey
     * @return
     */
    Resume createResume(String actorPublicKey);

    /**
     * This method creates a new resume
     * @param incomingResume
     */
    void createResume(Resume incomingResume);

    /**
     * This method updates and returns a resume.
     * TODO: to define all the data to include in the resume.
     * @param actorPublicKey
     * @return
     */
    Resume updateResume(String actorPublicKey);

    /**
     * This method resume an existing resume.
     * @param incomingResume
     */
    void updateResume(Resume incomingResume);

}
