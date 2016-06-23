package com.bitdubai.sub_app_job_seeker_community.navigation_drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerSubAppSessionReferenceApp;

import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;

import java.lang.ref.WeakReference;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/23/16.
 */
public class JobSeekerCommunityNavigationViewPainter implements NavigationViewPainter {

    private WeakReference<Context> activity;
    private ActiveActorIdentityInformation actorIdentity;
    private JobSeekerSubAppSessionReferenceApp subAppSession;
    private JobSeekerCommunitySubAppModuleManager moduleManager;


    public JobSeekerCommunityNavigationViewPainter(
            Context activity,
            ActiveActorIdentityInformation actorIdentity,
            JobSeekerSubAppSessionReferenceApp subAppSession) {
        this.activity = new WeakReference<Context>(activity);
        this.actorIdentity = actorIdentity;
        this.subAppSession = subAppSession;
        this.moduleManager = subAppSession.getModuleManager();
    }

    @Override
    public View addNavigationViewHeader() {
        View headerView = null;
        //TODO: el actorIdentityInformation lo podes obtener del module en un hilo en background y hacer un lindo loader mientras tanto
//        try {
//            //If the actor is not set yet, I'll try to find it.
//            if(actorIdentityInformation==null&&moduleManager!=null){
//                try{
//                    //I'll set the app public cas just in case.
//                    String subAppPublicKey = subAppSession.getAppPublicKey();
//                    if(subAppPublicKey!=null&&!subAppPublicKey.isEmpty()){
//                        moduleManager.setAppPublicKey(subAppPublicKey);
//                    }
//                    actorIdentityInformation = moduleManager.getSelectedActorIdentity();
//                } catch (Exception e) {
//                    //Cannot find the selected identity in any form... I'll let the identity in null
//                }
//            }
//            headerView = FragmentsCommons.setUpHeaderScreen((LayoutInflater) activity.get()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE), activity.get(), actorIdentityInformation);
//            headerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try{
//                        ListIdentitiesDialog listIdentitiesDialog = new ListIdentitiesDialog(activity.get(), subAppSession, null);
//                        listIdentitiesDialog.setTitle("Connection Request");
//                        listIdentitiesDialog.show();
//                        listIdentitiesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                            }
//                        });
//                        listIdentitiesDialog.show();
//                    }catch(Exception e){ }
//                }
//            });
//        } catch (CantGetActiveLoginIdentityException e) {
//            e.printStackTrace();
//        }
        return headerView;
    }

    @Override
    public FermatAdapter addNavigationViewAdapter() {
        try {
            return new JobSeekerCommunityNavigationViewAdapter(activity.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ViewGroup addNavigationViewBodyContainer(LayoutInflater layoutInflater, ViewGroup base) {
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
                R.layout.jjsc_navigation_view_bottom,
                base,
                true);
        return layout;
    }

    @Override
    public Bitmap addBodyBackground() {
        return null;
    }

    @Override
    public int addBodyBackgroundColor() {
        return 0;
    }

    @Override
    public RecyclerView.ItemDecoration addItemDecoration() {
        return null;
    }

    @Override
    public boolean hasBodyBackground() {
        return true;
    }

    @Override
    public boolean hasClickListener() {
        return false;
    }
}
