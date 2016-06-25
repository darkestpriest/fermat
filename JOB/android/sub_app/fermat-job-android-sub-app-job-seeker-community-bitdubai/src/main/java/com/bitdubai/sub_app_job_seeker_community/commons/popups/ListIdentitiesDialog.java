package com.bitdubai.sub_app_job_seeker_community.commons.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.adapters.AppSelectableIdentitiesListAdapter;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerCommunitySubAppSessionReferenceApp;

import org.fermat.fermat_job_api.all_definition.exceptions.CantListSelectableIdentitiesException;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySelectableIdentity;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class ListIdentitiesDialog extends
        FermatDialog<
                JobSeekerCommunitySubAppSessionReferenceApp,
                SubAppResourcesProviderManager>
        implements
        FermatListItemListeners<JobSeekerCommunitySelectableIdentity> {

    /**
     * UI components
     */
    private CharSequence title;
    private AppSelectableIdentitiesListAdapter adapter;

    /**
     * Managers
     */
    private JobSeekerCommunitySubAppModuleManager manager;

    public ListIdentitiesDialog(final Context activity,
                                final JobSeekerCommunitySubAppSessionReferenceApp subAppSession,
                                final SubAppResourcesProviderManager subAppResources) {
        super(
                activity,
                subAppSession,
                subAppResources
        );
        manager = subAppSession.getModuleManager();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<JobSeekerCommunitySelectableIdentity> jobSeekerCommunitySelectableIdentities = new ArrayList<>();
        try {
            jobSeekerCommunitySelectableIdentities = manager.listSelectableIdentities();
        }  catch (CantListSelectableIdentitiesException cantListIdentitiesToSelectException) {
            getSession().getErrorManager().reportUnexpectedUIException(
                    UISource.ADAPTER,
                    UnexpectedUIExceptionSeverity.UNSTABLE,
                    cantListIdentitiesToSelectException
            );
        }

        adapter = new AppSelectableIdentitiesListAdapter(getActivity(), jobSeekerCommunitySelectableIdentities);
        adapter.setFermatListEventListener(this);

        adapter.changeDataSet(jobSeekerCommunitySelectableIdentities);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.jjsc_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClickListener(JobSeekerCommunitySelectableIdentity data, int position) {
        manager.setSelectedActorIdentity(data);
        dismiss();
    }

    @Override
    public void onLongItemClickListener(JobSeekerCommunitySelectableIdentity data, int position) {
        manager.setSelectedActorIdentity(data);
        dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.jjsc_fragment_list_identities;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }
}
