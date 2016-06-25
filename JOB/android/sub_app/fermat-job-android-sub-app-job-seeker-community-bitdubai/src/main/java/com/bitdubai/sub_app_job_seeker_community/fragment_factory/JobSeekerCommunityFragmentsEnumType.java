package com.bitdubai.sub_app_job_seeker_community.fragment_factory;

import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public enum JobSeekerCommunityFragmentsEnumType implements
        FermatFragmentsEnumType<
                        JobSeekerCommunityFragmentsEnumType> {


    JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_WORLD("JOBJSCCW"),
    JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTIONS("JOBJSCC"),
    JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_RESUME("JOBJSCCR"),
    JOB_SUB_APP_JOB_SEEKER_COMMUNITY_CONNECTION_NOTIFICATIONS("JOBJSCCN"),
    JOB_SUB_APP_JOB_SEEKER_COMMUNITY_LOCAL_IDENTITIES_LIST("JOBJSLIL"),
    ;

    private String key;

    JobSeekerCommunityFragmentsEnumType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }


    @Override
    public String toString() {
        return key;
    }

    public static JobSeekerCommunityFragmentsEnumType getValue(String name) {
        for (JobSeekerCommunityFragmentsEnumType fragments : JobSeekerCommunityFragmentsEnumType.values()) {
            if (fragments.key.equals(name)) {
                return fragments;
            }
        }
        return null;
    }
}
