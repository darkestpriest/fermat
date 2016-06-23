package org.fermat.fermat_job_api.layer.identity.employer.interfaces;

import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.all_definition.interfaces.JobIdentity;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface Employer extends JobIdentity {

    /**
     * This method returns the Industry.
     * @return
     */
    Industry getIndustry();

}
