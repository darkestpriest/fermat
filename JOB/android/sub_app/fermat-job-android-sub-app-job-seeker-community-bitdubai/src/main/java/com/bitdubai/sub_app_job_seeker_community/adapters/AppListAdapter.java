package com.bitdubai.sub_app_job_seeker_community.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.sub_app.job_seeker_community.R;
import com.bitdubai.sub_app_job_seeker_community.holders.AppWorldHolder;
import com.squareup.picasso.Picasso;

import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;

import java.util.List;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class AppListAdapter extends FermatAdapter<JobSeekerCommunityInformation, AppWorldHolder> {


    public AppListAdapter(Context context) {
        super(context);
    }

    public AppListAdapter(Context context, List<JobSeekerCommunityInformation> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected AppWorldHolder createHolder(View itemView, int type) {
        return new AppWorldHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.jjsc_row_connections_world;
    }

    @Override
    protected void bindHolder(AppWorldHolder holder, JobSeekerCommunityInformation data, int position) {
        holder.name.setText(data.getAlias());

        if(data.getConnectionState() != null && data.getConnectionState() == ConnectionState.CONNECTED)
            holder.connectionState.setVisibility(View.VISIBLE);
        else
            holder.connectionState.setVisibility(View.GONE);

        byte[] profileImage = data.getImage();
        if (profileImage != null && profileImage.length>0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data.getImage(), 0, data.getImage().length);
            // Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
            holder.thumbnail.setImageBitmap(bitmap);
        }
        else
            Picasso.with(context).load(R.drawable.jjsc_profile_image).into(holder.thumbnail);
    }



    public int getSize() {
        if (dataSet != null)
            return dataSet.size();
        return 0;
    }

}
