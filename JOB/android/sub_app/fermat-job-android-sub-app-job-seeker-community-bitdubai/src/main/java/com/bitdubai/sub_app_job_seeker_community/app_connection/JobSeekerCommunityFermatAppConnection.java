package com.bitdubai.sub_app_job_seeker_community.app_connection;

import android.content.Context;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.sub_app_job_seeker_community.fragment_factory.JobSeekerCommunityFragmentFactory;
import com.bitdubai.sub_app_job_seeker_community.navigation_drawer.JobSeekerCommunityNavigationViewPainter;
import com.bitdubai.sub_app_job_seeker_community.notifications.CommunityNotificationPainterBuilder;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerCommunitySubAppSessionReferenceApp;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class JobSeekerCommunityFermatAppConnection extends AppConnections<JobSeekerCommunitySubAppSessionReferenceApp> {

    public JobSeekerCommunityFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new JobSeekerCommunityFragmentFactory();
    }

    @Override
    public PluginVersionReference[] getPluginVersionReference() {
        return  new PluginVersionReference[]{ new PluginVersionReference(
                Platforms.JOB_PLATFORM,
                Layers.SUB_APP_MODULE,
                Plugins.JOB_SEEKER_COMMUNITY,
                Developers.BITDUBAI,
                new Version()
        )};
    }


    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        //TODO: el actorIdentityInformation lo podes obtener del module en un hilo en background y hacer un lindo loader mientras tanto
        return new JobSeekerCommunityNavigationViewPainter(
                getContext(),
                null,
                getFullyLoadedSession());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return null;
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(final String code) {
        return CommunityNotificationPainterBuilder.getNotification(
                code,
                getFullyLoadedSession()
        );
    }
}

