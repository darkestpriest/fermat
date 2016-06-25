package com.bitdubai.sub_app_job_seeker_community.fragment_factory;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerCommunitySubAppSessionReferenceApp;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class JobSeekerCommunityFragmentFactory extends
        FermatFragmentFactory<
                JobSeekerCommunitySubAppSessionReferenceApp,
                SubAppResourcesProviderManager,
                JobSeekerCommunityFragmentsEnumType> {


    @Override
    public AbstractFermatFragment getFermatFragment(JobSeekerCommunityFragmentsEnumType fragments) throws
            FragmentNotFoundException {
        AbstractFermatFragment currentFragment = null;

        switch (fragments) {
            /*case ART_WALLET_STORE_ALL_FRAGMENT:
                currentFragment = ConnectionsWorldFragment.newInstance();
                break;*/
            case JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_WORLD:
           //     currentFragment = ConnectionsFragment.newInstance();
                break;
            case JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTIONS:
          //      currentFragment = ConnectionNotificationsFragment.newInstance();
                break;
            case JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_RESUME:
          //      currentFragment = ConnectionOtherProfileFragment.newInstance();
                break;
            case JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_NOTIFICATIONS:
         //       currentFragment = ConnectionsWorldFragment.newInstance();
                break;
            case JOB_SUB_APP_JOB_SEEKER_COMMUNITY_LOCAL_IDENTITIES_LIST:
          //      currentFragment = ConnectionsListFragment.newInstance();
                break;
            default:
                throw new FragmentNotFoundException(
                        fragments.toString(),
                        "Switch failed"
                );
        }
        return currentFragment;
    }

    @Override
    public JobSeekerCommunityFragmentsEnumType getFermatFragmentEnumType(String key) {
        return JobSeekerCommunityFragmentsEnumType.getValue(key);
    }

    private FragmentNotFoundException createFragmentNotFoundException(FermatFragmentsEnumType fragments) {
        String possibleReason, context;

        if (fragments == null) {
            possibleReason = "The parameter 'fragments' is NULL";
            context = "Null Value";
        } else {
            possibleReason = "Not found in switch block";
            context = fragments.toString();
        }
        return new FragmentNotFoundException(
                "Fragment not found",
                new Exception(),
                context,
                possibleReason);
    }
}
