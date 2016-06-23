package com.bitdubai.sub_app_job_seeker_community.sessions;

import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.dmp_module.sub_app_manager.InstalledSubApp;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/23/16.
 */
public class JobSeekerSubAppSessionReferenceApp extends AbstractReferenceAppFermatSession<
        InstalledSubApp,
        JobSeekerCommunitySubAppModuleManager,
        SubAppResourcesProviderManager> {

    public JobSeekerSubAppSessionReferenceApp(){}


}
