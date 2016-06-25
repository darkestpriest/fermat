package com.bitdubai.sub_app_job_seeker_community.notifications;

import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.sessions.JobSeekerCommunitySubAppSessionReferenceApp;

import org.fermat.fermat_job_api.layer.actor.JobSeeker.enums.JobSeekerActorConnectionNotificationType;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;



/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/23/16.
 */
public class CommunityNotificationPainterBuilder {
    public static NotificationPainter getNotification(
            final String code,
            AbstractReferenceAppFermatSession session) {
        NotificationPainter notification = null;
        int lastListIndex;
        if(session!=null){
            JobSeekerCommunitySubAppSessionReferenceApp jobSeekerCommunitySubAppSession = (JobSeekerCommunitySubAppSessionReferenceApp) session;
            JobSeekerCommunitySubAppModuleManager moduleManager = jobSeekerCommunitySubAppSession.getModuleManager();
            try{
                JobSeekerActorConnectionNotificationType notificationType = JobSeekerActorConnectionNotificationType.getByCode(code);
                if(moduleManager==null){
                    return getDefaultNotification(code);
                }
                switch (notificationType) {
                    case ACTOR_REVIEW_RESUME:
                        //TODO: Define in module listEmployerReviewResume or something like that
                      /*  List<JobSeekerCommunityInformation> listEmployerReviewResume= moduleManager.
                                listEmployerReviewResume(
                                        moduleManager.getSelectedActorIdentity(),
                                        20,
                                        0);
                        lastListIndex = listEmployerReviewResume.size()-1;
                        JobSeekerCommunityInformation jobSeekerCommunityInformation = listEmployerReviewResume.get(
                                lastListIndex);
                        notification = new CommunityNotificationPainter(
                                "Job Seeker Community",
                                "A Fermat Employer, "+jobSeekerCommunityInformation.employerName()+", " +
                                        "review your profile on"+jobSeekerCommunityInformation.dateReview(),
                                "",
                                "",
                                R.drawable.jjsc_ic_nav_connections);*/
                        break;

                }
            } catch (Exception e) {
                //TODO: improve this catch
                e.printStackTrace();
            }
        }else{
            try {
                getDefaultNotification(code);
            } catch (Exception e) {
                //TODO: improve this catch
                e.printStackTrace();
            }
        }
        return notification;
    }

    /**
     * This method returns an Artist community notification
     * @param code
     * @return
     * @throws InvalidParameterException
     */
    private static NotificationPainter getDefaultNotification(final String code)
            throws InvalidParameterException {
        JobSeekerActorConnectionNotificationType notificationType =
                JobSeekerActorConnectionNotificationType.getByCode(code);
        NotificationPainter notification = null;
        switch (notificationType) {
            case ACTOR_REVIEW_RESUME:
                notification = new CommunityNotificationPainter(
                        "Job Seeker Community",
                        "A Fermat Employer wants to connect with you.",
                        "",
                        "",
                        R.drawable.jjsc_ic_nav_connections);
                break;

        }
        return notification;
    }

}
