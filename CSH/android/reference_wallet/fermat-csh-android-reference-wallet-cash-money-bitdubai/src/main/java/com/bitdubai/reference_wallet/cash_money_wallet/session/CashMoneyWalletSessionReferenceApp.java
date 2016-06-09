package com.bitdubai.reference_wallet.cash_money_wallet.session;

import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledWallet;
import com.bitdubai.fermat_csh_api.layer.csh_wallet_module.interfaces.CashMoneyWalletModuleManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;

/**
 * Created by Alejandro Bicelis on 12/9/2015.
 */
public class CashMoneyWalletSessionReferenceApp extends AbstractReferenceAppFermatSession<InstalledWallet,CashMoneyWalletModuleManager, WalletResourcesProviderManager> {

    public CashMoneyWalletSessionReferenceApp() {}

}
