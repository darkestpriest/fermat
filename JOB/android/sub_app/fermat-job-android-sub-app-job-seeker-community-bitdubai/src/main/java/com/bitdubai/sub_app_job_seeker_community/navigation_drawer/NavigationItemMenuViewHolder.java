package com.bitdubai.sub_app_job_seeker_community.navigation_drawer;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.sub_app.job_seeker_community.R;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/23/16.
 */
public class NavigationItemMenuViewHolder extends FermatViewHolder {
    private TextView label;
    private ImageView icon;
    private LinearLayout rowContainer;
    private LinearLayout fullRow;



    public NavigationItemMenuViewHolder(View itemView) {
        super(itemView);

        label = (TextView) itemView.findViewById(R.id.jjsc_textView_label);
        icon = (ImageView) itemView.findViewById(R.id.jjsc_imageView_icon);
        rowContainer = (LinearLayout) itemView.findViewById(R.id.jjsc_row_container);
        fullRow = (LinearLayout) itemView.findViewById(R.id.jjsc_full_row);

    }

    public TextView getLabel() {
        return label;
    }

    public ImageView getIcon() {
        return icon;
    }

    public LinearLayout getFullRow() {
        return fullRow;
    }

    public LinearLayout getRowContainer() {
        return rowContainer;
    }
}
