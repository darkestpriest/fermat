package com.bitdubai.sub_app_job_seeker_community.holders;

import android.view.View;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.sub_app.job_seeker_community.R;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/24/16.
 */
public class AppWorldHolder extends FermatViewHolder {

    public ImageView thumbnail;
    public FermatTextView name;
    public ImageView connectionState;

    /**
     * Default Constructor
     * @param itemView cast elements in layout
     */
    public AppWorldHolder(View itemView) {
        super(itemView);
        connectionState = (ImageView) itemView.findViewById(R.id.jjsc_connection_state);
        thumbnail = (ImageView) itemView.findViewById(R.id.profile_image);
        name = (FermatTextView) itemView.findViewById(R.id.jjsc_community_name);
    }
}
