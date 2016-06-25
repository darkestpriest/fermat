package com.bitdubai.sub_app_job_seeker_community.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.ArtCommunityInformation;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.holders.AppFriendsListHolder;

import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;

import java.util.List;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class AppFriendsListAdapter extends FermatAdapter<JobSeekerCommunityInformation, AppFriendsListHolder> {

    public AppFriendsListAdapter(Context context, List<JobSeekerCommunityInformation> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected AppFriendsListHolder createHolder(View itemView, int type) {
        return new AppFriendsListHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.jjsc_row_connection_list;
    }

    @Override
    protected void bindHolder(AppFriendsListHolder holder, JobSeekerCommunityInformation data, int position) {
        if (data.getPublicKey() != null) {
            holder.friendName.setText(data.getAlias());
            if (data.getImage() != null) {
                Bitmap bitmap;
                if (data.getImage().length > 0) {
                    bitmap = BitmapFactory.decodeByteArray(data.getImage(), 0, data.getImage().length);
                } else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jjsc_profile_image);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
                holder.friendAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
            }else{
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jjsc_profile_image);
                bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
                holder.friendAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
            }
            Actors actorType = data.getActorType();
            Bitmap bitmap;
            switch (actorType){
                case EMPLOYER:
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jjsc_star);
                    break;
                case JOB_SEEKER:
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jjsc_people);
                    break;
                default:
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jjsc_people);
                    break;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
            holder.actorIcon.setImageBitmap(bitmap);
        }
    }


    public int getSize() {
        if (dataSet != null)
            return dataSet.size();
        return 0;
    }
}



