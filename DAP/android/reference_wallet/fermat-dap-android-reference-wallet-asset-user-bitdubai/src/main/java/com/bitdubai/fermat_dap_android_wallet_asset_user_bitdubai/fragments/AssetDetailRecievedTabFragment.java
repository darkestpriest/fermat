package com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.SizeUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.ConfirmDialog;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.R;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.adapters.AssetDetailRecievedAdapter;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.models.Data;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.models.DigitalAsset;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.models.Transaction;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.sessions.AssetUserSession;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.sessions.SessionConstantsAssetUser;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.util.CommonLogger;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_user.AssetUserSettings;
import com.bitdubai.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * Created by frank on 12/15/15.
 */
public class AssetDetailRecievedTabFragment extends FermatWalletListFragment<Transaction>
        implements FermatListItemListeners<Transaction> {

    private AssetUserSession assetUserSession;
    private AssetUserWalletSubAppModuleManager moduleManager;

    private View rootView;
    private Toolbar toolbar;
    private Resources res;
//    private View assetDetailRedeemLayout;
//    private View assetDetailAppropriateLayout;
//    private View assetDetailTransferLayout;

    private ImageView assetImageDetail;
    private FermatTextView assetDetailNameText;
    private FermatTextView assetDetailExpDateText;
    private FermatTextView availableText;
    private FermatTextView pendingText;
    private FermatTextView assetDetailBtcText;
//    private FermatTextView assetDetailRedeemText;

    private DigitalAsset digitalAsset;
    private ErrorManager errorManager;

    SettingsManager<AssetUserSettings> settingsManager;

    private View noTransactionsView;
    private List<Transaction> transactions;

    public AssetDetailRecievedTabFragment() {

    }

    public static AssetDetailRecievedTabFragment newInstance() {
        return new AssetDetailRecievedTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        assetUserSession = (AssetUserSession) appSession;
        moduleManager = assetUserSession.getModuleManager();
        errorManager = appSession.getErrorManager();
        moduleManager.clearDeliverList();
        settingsManager = appSession.getModuleManager().getSettingsManager();

        transactions = (List) getMoreDataAsync(FermatRefreshTypes.NEW, 0);
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        Activity activity = getActivity();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        rootView = configureActivityHeader(layoutInflater);

        res = rootView.getResources();

        setupUI();
        setupUIData();

        configureToolbar();

        noTransactionsView = layout.findViewById(R.id.dap_wallet_asset_issuer_no_transactions_recieved);

        showOrHideNoTransactionsView(transactions.isEmpty());
    }

    private View configureActivityHeader(LayoutInflater layoutInflater) {
        RelativeLayout header = getToolbarHeader();
        try {
            header.removeAllViews();
        } catch (Exception exception) {
            CommonLogger.exception(TAG, "Error removing all views from header ", exception);
            errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, exception);
        }
        header.setVisibility(View.VISIBLE);
        View container = layoutInflater.inflate(R.layout.dap_wallet_asset_user_asset_detail, header, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            container.getLayoutParams().height = SizeUtils.convertDpToPixels(428, getActivity());
        }
        return container;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showOrHideNoTransactionsView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            noTransactionsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noTransactionsView.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_wallet_asset_user_asset_detail_transaction_recieved;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_wallet_asset_issuer_user_asset_detail_transaction_recieved_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    private void setUpHelpAssetDetail(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_user_wallet)
                    .setIconRes(R.drawable.asset_user_wallet)
                    .setVIewColor(R.color.dap_user_view_color)
                    .setTitleTextColor(R.color.dap_user_view_color)
                    .setSubTitle(R.string.dap_user_wallet_detail_subTitle)
                    .setBody(R.string.dap_user_wallet_detail_body)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dap_wallet_asset_user_detail_menu, menu);
        menu.add(0, SessionConstantsAssetUser.IC_ACTION_USER_HELP_DETAIL, 0, "help").setIcon(R.drawable.dap_asset_user_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetUser.IC_ACTION_USER_HELP_DETAIL) {
                setUpHelpAssetDetail(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            } else if (id == R.id.action_wallet_user_redeem) {
                changeActivity(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM, appSession.getAppPublicKey());
                return true;
            } else if (id == R.id.action_wallet_user_appropriate) {
                new ConfirmDialog.Builder(getActivity(), appSession)
                        .setTitle(getResources().getString(R.string.dap_user_wallet_confirm_title))
                        .setMessage(getResources().getString(R.string.dap_user_wallet_confirm_sure))
                        .setColorStyle(getResources().getColor(R.color.dap_user_wallet_principal))
                        .setYesBtnListener(new ConfirmDialog.OnClickAcceptListener() {
                            @Override
                            public void onClick() {
                                doAppropriate(digitalAsset.getAssetPublicKey());
                            }
                        }).build().show();
                return true;
            } else if (id == R.id.action_wallet_user_transfer) {
                new ConfirmDialog.Builder(getActivity(), appSession)
                        .setTitle(getResources().getString(R.string.dap_user_wallet_confirm_title))
                        .setMessage(getResources().getString(R.string.dap_user_wallet_confirm_sure))
                        .setColorStyle(getResources().getColor(R.color.dap_user_wallet_principal))
                        .setYesBtnListener(new ConfirmDialog.OnClickAcceptListener() {
                            @Override
                            public void onClick() {
                                transferAsset(digitalAsset.getAssetPublicKey());
                            }
                        }).build().show();
                return true;
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), getResources().getString(R.string.dap_user_wallet_system_error),
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        setupBackgroundBitmap();

        assetImageDetail = (ImageView) rootView.findViewById(R.id.asset_image_detail);
        assetDetailNameText = (FermatTextView) rootView.findViewById(R.id.assetDetailNameText);
        assetDetailExpDateText = (FermatTextView) rootView.findViewById(R.id.assetDetailExpDateText);
        availableText = (FermatTextView) rootView.findViewById(R.id.assetAvailable1);
        pendingText = (FermatTextView) rootView.findViewById(R.id.assetAvailable2);
        assetDetailBtcText = (FermatTextView) rootView.findViewById(R.id.assetDetailBtcText);
    }

    private void setupBackgroundBitmap() {
        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {

            WeakReference<ViewGroup> view;

            @Override
            protected void onPreExecute() {
                view = new WeakReference(rootView);
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap drawable = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = true;
                    options.inSampleSize = 5;
                    drawable = BitmapFactory.decodeResource(
                            getResources(), R.drawable.bg_app_image_user, options);
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                }
                return drawable;
            }

            @Override
            protected void onPostExecute(Bitmap drawable) {
                if (drawable != null) {
                    view.get().setBackground(new BitmapDrawable(getResources(), drawable));
                }
            }
        };
        asyncTask.execute();
    }

    private void setupUIData() {
        String digitalAssetPublicKey = ((DigitalAsset) appSession.getData("asset_data")).getAssetPublicKey();
        try {
            digitalAsset = Data.getDigitalAsset(moduleManager, digitalAssetPublicKey);
        } catch (CantLoadWalletException e) {
            e.printStackTrace();
        }

        byte[] img = (digitalAsset.getImage() == null) ? new byte[0] : digitalAsset.getImage();
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(assetImageDetail, res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img); //todo comment to compile, please review.

//        assetDetailRedeemLayout.setVisibility((digitalAsset.getAvailableBalanceQuantity() > 0) ? View.VISIBLE : View.GONE);
//        assetDetailAppropriateLayout.setVisibility((digitalAsset.getAvailableBalanceQuantity() > 0) ? View.VISIBLE : View.GONE);
        //TODO set visibility of menu options

        assetDetailNameText.setText(digitalAsset.getName());
        assetDetailExpDateText.setText(digitalAsset.getFormattedExpDate());

        long available = digitalAsset.getAvailableBalanceQuantity();
        long book = digitalAsset.getBookBalanceQuantity();
        availableText.setText(availableText(available));
        if (available == book) {
            pendingText.setVisibility(View.INVISIBLE);
        } else {
            long pendingValue = Math.abs(available - book);
            pendingText.setText(pendingText(pendingValue));
            pendingText.setVisibility(View.VISIBLE);
        }

        assetDetailBtcText.setText(digitalAsset.getFormattedAvailableBalanceBitcoin() + " BTC");
    }

    private String pendingText(long pendingValue) {
        return "(" + pendingValue + " pending confirmation)";
    }

    private String availableText(long available) {
        return available + ((available == 1) ? " Asset" : " Assets");
    }

    private void configureToolbar() {
        toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.dap_user_wallet_principal));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBottom(Color.WHITE);
            toolbar.setTitle(digitalAsset.getName());
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.dap_user_wallet_principal));
            }
        }
    }

    private void doAppropriate(final String assetPublicKey) {
        final Activity activity = getActivity();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(getResources().getString(R.string.dap_user_wallet_wait));
        dialog.setCancelable(false);
        dialog.show();
        FermatWorker task = new FermatWorker() {
            @Override
            protected Object doInBackground() throws Exception {
//                    manager.distributionAssets(
//                            asset.getAssetPublicKey(),
//                            asset.getWalletPublicKey(),
//                            asset.getActorAssetRedeemPoint()
//                    );
                //TODO: only for Appropriate test
                moduleManager.appropriateAsset(assetPublicKey, null);
                return true;
            }
        };
        task.setContext(activity);
        task.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                dialog.dismiss();
                if (activity != null) {
                    Toast.makeText(activity, getResources().getString(R.string.dap_user_wallet_appropriation_ok), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                dialog.dismiss();
                if (activity != null)
                    Toast.makeText(activity, getResources().getString(R.string.dap_user_wallet_exception_retry),
                            Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    private void transferAsset(final String assetPublicKey) {
        final Activity activity = getActivity();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(getResources().getString(R.string.dap_user_wallet_wait));
        dialog.setCancelable(false);
        dialog.show();
        FermatWorker task = new FermatWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                moduleManager.transferAssets(assetPublicKey, null, 1);
                return true;
            }
        };
        task.setContext(activity);
        task.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                dialog.dismiss();
                if (activity != null) {
                    Toast.makeText(activity, getResources().getString(R.string.dap_user_wallet_appropriation_ok), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                dialog.dismiss();
                if (activity != null)
                    Toast.makeText(activity, getResources().getString(R.string.dap_user_wallet_exception_retry),
                            Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    @Override
    public void onItemClickListener(Transaction data, int position) {

    }

    @Override
    public void onLongItemClickListener(Transaction data, int position) {

    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                transactions = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(transactions);

                showOrHideNoTransactionsView(transactions.isEmpty());
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new AssetDetailRecievedAdapter(getActivity(), transactions, moduleManager);
            adapter.setFermatListEventListener(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    public List<Transaction> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<Transaction> transactions = new ArrayList<>();
        if (moduleManager != null) {
            try {
                transactions = Data.getTransactionsRecieved(moduleManager, digitalAsset);

                appSession.setData("transactions_recieved", transactions);

            } catch (Exception ex) {
                CommonLogger.exception(TAG, ex.getMessage(), ex);
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(
                            Wallets.DAP_ASSET_USER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                            ex);
            }
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.dap_user_wallet_system_error),
                    Toast.LENGTH_SHORT).
                    show();
        }
        return transactions;
    }
}