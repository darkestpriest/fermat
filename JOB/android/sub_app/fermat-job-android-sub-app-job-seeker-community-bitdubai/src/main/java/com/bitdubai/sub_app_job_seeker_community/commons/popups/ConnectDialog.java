package com.bitdubai.sub_app_job_seeker_community.commons.popups;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerCommunitySubAppSessionReferenceApp;

import org.fermat.fermat_job_api.all_definition.exceptions.ActorConnectionAlreadyRequestedException;
import org.fermat.fermat_job_api.all_definition.exceptions.ActorTypeNotSupportedException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySelectableIdentity;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class ConnectDialog  extends FermatDialog<JobSeekerCommunitySubAppSessionReferenceApp, SubAppResourcesProviderManager>
        implements View.OnClickListener {

    /**
     * UI components
     */
    FermatButton positiveBtn;
    FermatButton negativeBtn;
    FermatTextView mDescription;
    FermatTextView mUsername;
    FermatTextView mSecondDescription;
    FermatTextView mTitle;
    CharSequence description;
    CharSequence secondDescription;
    CharSequence username;
    CharSequence title;

    private final JobSeekerCommunityInformation information;
    private final JobSeekerCommunitySelectableIdentity identity   ;


    public ConnectDialog(final Activity activity,
                         final JobSeekerCommunitySubAppSessionReferenceApp jobSeekerCommunitySubAppSession,
                         final SubAppResourcesProviderManager subAppResources,
                         final JobSeekerCommunityInformation information,
                         final JobSeekerCommunitySelectableIdentity identity) {

        super(activity, jobSeekerCommunitySubAppSession, subAppResources);

        this.information = information;
        this.identity = identity;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDescription = (FermatTextView) findViewById(R.id.jjsc_description);
        mUsername = (FermatTextView) findViewById(R.id.jjsc_user_name);
        mSecondDescription = (FermatTextView) findViewById(R.id.jjsc_second_description);
        mTitle = (FermatTextView) findViewById(R.id.jjsc_title);
        positiveBtn = (FermatButton) findViewById(R.id.jjsc_positive_button);
        negativeBtn = (FermatButton) findViewById(R.id.jjsc_negative_button);
        mSecondDescription.setVisibility(View.VISIBLE);
        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        mSecondDescription.setText(secondDescription != null ? secondDescription : "");
        mDescription.setText(description != null ? description : "");
        mUsername.setText(username != null ? username : "");
        mTitle.setText(title != null ? title : "");

    }

    public void setSecondDescription(CharSequence secondDescription) {
        this.secondDescription = secondDescription;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public void setUsername(CharSequence username) {
        this.username = username;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.jjsc_dialog_builder;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.jjsc_positive_button) {
            try {
                if (information != null && identity != null) {

                    //System.out.println("*********** i'm the selected identity: "+identity);
                    //System.out.println("*********** i'm the selected broker information: " + information);

                    getSession().getModuleManager().requestConnectionToJobActor(identity, information);
                    Toast.makeText(getActivity(), "Connection request sent", Toast.LENGTH_SHORT).show();

                    //set flag so that the preceding fragment reads it on dismiss()
                    getSession().setData("connectionresult", 2);

                } else {
                    Toast.makeText(getActivity(), "There has been an error, please try again", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            } catch (CantRequestConnectionException e) {
                getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                Toast.makeText(getActivity(), "Can not request connection", Toast.LENGTH_SHORT).show();
            } catch (ActorConnectionAlreadyRequestedException | ActorTypeNotSupportedException e) {
                getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                Toast.makeText(getActivity(), "The connection has already been requested", Toast.LENGTH_SHORT).show();

            }

            dismiss();
        } else if (i == R.id.jjsc_negative_button) {
            dismiss();
        }
    }
}
