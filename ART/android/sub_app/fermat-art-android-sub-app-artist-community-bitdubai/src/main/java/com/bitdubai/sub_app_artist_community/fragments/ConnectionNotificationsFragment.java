package com.bitdubai.sub_app_artist_community.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.artist.exceptions.CantListArtistsException;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.artist.interfaces.ArtistCommunityInformation;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.artist.interfaces.ArtistCommunitySubAppModuleManager;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.exceptions.CantGetActiveLoginIdentityException;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.sub_app.artist_community.R;
import com.bitdubai.sub_app_artist_community.adapters.AppNotificationAdapter;
import com.bitdubai.sub_app_artist_community.commons.popups.AcceptDialog;
import com.bitdubai.sub_app_artist_community.sessions.ArtistSubAppSession;
import com.bitdubai.sub_app_artist_community.util.CommonLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Araujo (gabe_512@hotmail.com) on 08/04/16.
 */
public class ConnectionNotificationsFragment extends AbstractFermatFragment<ArtistSubAppSession, SubAppResourcesProviderManager>
        implements SwipeRefreshLayout.OnRefreshListener, FermatListItemListeners<ArtistCommunityInformation>, AcceptDialog.OnDismissListener {

    public static final String ACTOR_SELECTED = "actor_selected";

    private static final int MAX = 20;

    protected final String TAG = "ConnectionNotificationsFragment";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefresh;
    private boolean isRefreshing = false;
    private View rootView;
    private AppNotificationAdapter adapter;
    private LinearLayout emptyView;
    private ArtistCommunitySubAppModuleManager moduleManager;
    private ErrorManager errorManager;
    private int offset = 0;
    private ArtistCommunityInformation artistCommunityInformation;
    private List<ArtistCommunityInformation> artistCommunityInformations;

    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */
    public static ConnectionNotificationsFragment newInstance() {
        return new ConnectionNotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting up  module
        artistCommunityInformation = (ArtistCommunityInformation) appSession.getData(ACTOR_SELECTED);
        moduleManager = appSession.getModuleManager();
        errorManager = appSession.getErrorManager();
        artistCommunityInformations = new ArrayList<>();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.aac_fragment_connections_notificactions, container, false);
            setUpScreen(inflater);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.aac_recycler_view);
            layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new AppNotificationAdapter(getActivity(), artistCommunityInformations);
            adapter.setFermatListEventListener(this);
            recyclerView.setAdapter(adapter);

            swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.afc_swipeRefresh);
            swipeRefresh.setOnRefreshListener(this);
            swipeRefresh.setColorSchemeColors(Color.BLUE, Color.BLUE);

            rootView.setBackgroundColor(Color.parseColor("#000b12"));
            emptyView = (LinearLayout) rootView.findViewById(R.id.aac_empty_view);

            onRefresh();

        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();

        }

        return rootView;
    }

    private synchronized ArrayList<ArtistCommunityInformation> getMoreData() {

        ArrayList<ArtistCommunityInformation> dataSet = new ArrayList<>();

        try {

            dataSet.addAll(moduleManager.listArtistsPendingLocalAction(moduleManager.getSelectedActorIdentity(), MAX, offset));

        } catch (CantGetSelectedActorIdentityException | ActorIdentityNotSelectedException | CantListArtistsException | ArrayIndexOutOfBoundsException e) {
            String possibleReason = null;
            if(e instanceof CantGetSelectedActorIdentityException) possibleReason = "Can't Get Selected Actor Identity.";
            if(e instanceof ActorIdentityNotSelectedException) possibleReason = "Actor Identity Not Selected.";
            if(e instanceof CantListArtistsException) possibleReason = "Can't List Artists.";
            if(e instanceof ArrayIndexOutOfBoundsException) possibleReason = "Array Index Out Of Bounds.";

            FermatException ex = new CantListArtistsException(
                    "Exception not catched correctly. getMoreData. - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "getMoreData. Problem to get the information. "+possibleReason);

        } catch (Exception e){
            FermatException ex = new CantListArtistsException(
                    "Exception not catched correctly. getMoreData - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "getMoreData. Unknown Failure");
            errorManager.reportUnexpectedPluginException(
                    Plugins.ARTIST_COMMUNITY_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    FermatException.wrapException(ex));
        } finally{
            return dataSet;
        }

    }

    private void setUpScreen(LayoutInflater layoutInflater) throws CantGetActiveLoginIdentityException {
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            final ProgressDialog notificationsProgressDialog = new ProgressDialog(getActivity());
            notificationsProgressDialog.setMessage("Loading Notifications");
            notificationsProgressDialog.setCancelable(false);
            notificationsProgressDialog.show();
            FermatWorker worker = new FermatWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    return getMoreData();
                }
            };
            worker.setContext(getActivity());
            worker.setCallBack(new FermatWorkerCallBack() {
                @SuppressWarnings("unchecked")
                @Override
                public void onPostExecute(Object... result) {
                    notificationsProgressDialog.dismiss();
                    isRefreshing = false;
                    if (swipeRefresh != null)
                        swipeRefresh.setRefreshing(false);
                    if (result != null &&
                            result.length > 0) {
                        if (getActivity() != null && adapter != null) {
                            artistCommunityInformations = (ArrayList<ArtistCommunityInformation>) result[0];
                            adapter.changeDataSet(artistCommunityInformations);
                            if (artistCommunityInformations.isEmpty()) {
                                showEmpty(true, emptyView);
                            } else {
                                showEmpty(false, emptyView);
                            }
                        }
                    } else
                        showEmpty(adapter.getSize() < 0, emptyView);
                }

                @Override
                public void onErrorOccurred(Exception ex) {
                    notificationsProgressDialog.dismiss();
                    try {

                        isRefreshing = false;
                        if (swipeRefresh != null)
                            swipeRefresh.setRefreshing(false);
                        if (getActivity() != null)
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();

                    } catch (Exception e){
                        FermatException exception = new CantListArtistsException(
                                "Exception not catched correctly. onErrorOccurred - Message: " + e.getMessage(),
                                FermatException.wrapException(e),
                                e.getCause().toString(),
                                "onErrorOccurred. onRefresh error"
                                );
                        errorManager.reportUnexpectedPluginException(
                                Plugins.ARTIST_COMMUNITY_SUB_APP_MODULE,
                                UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                                FermatException.wrapException(exception));
                    }
                }
            });
            worker.execute();
        }
    }


    @Override
    public void onItemClickListener(ArtistCommunityInformation data, int position) {
        try {

            Toast.makeText(getActivity(), "TODO ACCEPT ->", Toast.LENGTH_LONG).show();
            //moduleManager.acceptCryptoBroker(moduleManager.getSelectedActorIdentity(), data.getName(), data.getPublicKey(), data.getProfileImage());
            AcceptDialog notificationAcceptDialog = new AcceptDialog(getActivity(), appSession, appResourcesProviderManager, data, moduleManager.getSelectedActorIdentity());
            notificationAcceptDialog.setOnDismissListener(this);
            notificationAcceptDialog.show();

        } catch (CantGetSelectedActorIdentityException | ActorIdentityNotSelectedException | InstantiationException e) {
            String possibleReason = null;
            if(e instanceof CantGetSelectedActorIdentityException) possibleReason = "Can't Get Selected Actor Identity.";
            if(e instanceof ActorIdentityNotSelectedException) possibleReason = "Actor Identity Not Selected.";
            if(e instanceof InstantiationException) possibleReason = "Instantiation Problem.";

            FermatException exception = new CantListArtistsException(
                    "Error. onItemClickListener - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "onItemClickListener. "+possibleReason
            );
            errorManager.reportUnexpectedPluginException(
                    Plugins.ARTIST_COMMUNITY_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    FermatException.wrapException(e));

            Toast.makeText(getActivity(), "TODO ACCEPT but.. ERROR! ->", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLongItemClickListener(ArtistCommunityInformation data, int position) {

    }

    /**
     * @param show
     */
    public void showEmpty(boolean show, View emptyView) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(),
                show ? android.R.anim.fade_in : android.R.anim.fade_out);
        if (show &&
                (emptyView.getVisibility() == View.GONE || emptyView.getVisibility() == View.INVISIBLE)) {
            emptyView.setAnimation(anim);
            emptyView.setVisibility(View.VISIBLE);
            if (adapter != null)
                adapter.changeDataSet(null);
        } else if (!show && emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setAnimation(anim);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onRefresh();
    }
}
