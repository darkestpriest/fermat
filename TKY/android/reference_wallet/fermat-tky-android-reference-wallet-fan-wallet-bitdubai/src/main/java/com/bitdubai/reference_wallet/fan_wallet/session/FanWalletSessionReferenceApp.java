package com.bitdubai.reference_wallet.fan_wallet.session;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledWallet;
import com.bitdubai.fermat_tky_api.layer.wallet_module.interfaces.FanWalletModule;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_wallet.fan_wallet.common.adapters.SongAdapter;
import com.bitdubai.reference_wallet.fan_wallet.common.models.SongItems;
import com.bitdubai.reference_wallet.fan_wallet.fragments.SongFragment;

import java.util.List;

/**
 * Created by Miguel Payarez on 14/03/16.
 */
public class FanWalletSessionReferenceApp extends AbstractReferenceAppFermatSession<InstalledWallet,FanWalletModule,WalletResourcesProviderManager> {

    public static final String DOWNLOAD_THREAD="DOWNLOAD_THREAD";
    public static final String SYNC_THREAD="SYNC_THREAD";
    public static final String FAN_WALLET_VIEW="FAN_WALLET_VIEW";

    public static final String DOWNLOADING="DOWNLOADING";

    public static final String ITEMS="ITEMS";


}
