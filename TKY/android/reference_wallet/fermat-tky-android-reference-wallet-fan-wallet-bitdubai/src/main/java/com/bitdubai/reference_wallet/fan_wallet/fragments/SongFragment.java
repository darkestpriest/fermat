package com.bitdubai.reference_wallet.fan_wallet.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.FermatBundle;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.BroadcasterNotificationType;
import com.bitdubai.fermat_tky_api.all_definitions.enums.HTTPErrorResponse;
import com.bitdubai.fermat_tky_api.all_definitions.enums.SongStatus;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.Song;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDeleteSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDownloadSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantGetSongListException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongDevicePathException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongStatusException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.WalletSong;
import com.bitdubai.fermat_tky_api.layer.wallet_module.FanWalletPreferenceSettings;
import com.bitdubai.fermat_tky_api.layer.wallet_module.interfaces.FanWalletModule;
import com.bitdubai.reference_wallet.fan_wallet.R;
import com.bitdubai.reference_wallet.fan_wallet.common.adapters.SongAdapter;
import com.bitdubai.reference_wallet.fan_wallet.common.models.SongItems;
import com.bitdubai.reference_wallet.fan_wallet.session.FanWalletSession;
import com.bitdubai.reference_wallet.fan_wallet.util.ManageRecyclerviewClick;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Miguel Payarez on 16/03/16.
 */
public class SongFragment extends AbstractFermatFragment {
    //FermatManager
    private FanWalletSession fanwalletSession;
    private FanWalletPreferenceSettings fanWalletSettings;
    private ErrorManager errorManager;
    private FanWalletModule fanWalletModule;


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    String TAG="SONGFRAGMENT";
    View view;
    private Paint p = new Paint();
    DownloadThreadClass downloadThread;
    SyncThreadClass syncThread;
    final Handler myHandler = new Handler();
    final Handler myCancelHandler = new Handler();
   // MonitorThreadClass monitorThreadClass;
    private SongAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    String code;
    FermatBundle bundle;
    PresentationDialog presentationDialog;
    /**
     * This flag represents if this fragment can access to the Fan identity.
     */
    private boolean isFanIdentity = false;


    List<SongItems> items=new ArrayList();
    public static SongFragment newInstance(){

        return new SongFragment();
    }

    public SongFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            fanwalletSession = ((FanWalletSession) appSession);
            errorManager = appSession.getErrorManager();
            System.out.println("HERE START SONG");

            try {
                fanWalletSettings =  fanwalletSession.getModuleManager().getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey());
            } catch (Exception e) {
                fanWalletSettings = null;
            }

            if (fanWalletSettings == null) {
                fanWalletSettings = new FanWalletPreferenceSettings();
                fanWalletSettings.setIsPresentationHelpEnabled(true);
                try {
                    fanwalletSession.getModuleManager().getSettingsManager().persistSettings(appSession.getAppPublicKey(), fanWalletSettings);
                } catch (Exception e) {
                    errorManager.reportUnexpectedWalletException(Wallets.TKY_FAN_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
            }


        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.TKY_FAN_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }

    }

    void initValues(){
        fanWalletModule=fanwalletSession.getModuleManager();
        compareViewAndDatabase();
        syncTokenlyAndUpdateThreads(true);

    }

    @Override
    public void onUpdateViewOnUIThread(String code) {
        if (swipeContainer.isRefreshing()){
            swipeContainer.setRefreshing(false);
        }
        Toast.makeText(
                getActivity(),
                "Connection Problem With External Platform",
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onUpdateViewUIThred(FermatBundle bundle) {
            this.bundle = bundle;
        if (swipeContainer.isRefreshing()){
            swipeContainer.setRefreshing(false);
        }
        myHandler.post(myRunnableBundle);
    }

    final Runnable myRunnableBundle = new Runnable() {

        public void run() {
            updateViewForBroadcaster(bundle);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.tky_fan_wallet_song_fragment, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        lManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(lManager);
        adapter = new SongAdapter(items);
        recyclerView.setAdapter(adapter);
        initValues();

        swipeEffect();


        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.fanwallet_background_viewpager);

        recyclerView.addOnItemTouchListener(
                new ManageRecyclerviewClick(view.getContext(), new ManageRecyclerviewClick.OnItemClickListener() {
                    @Override

                    public void onItemClick(View view, int position) {

                        System.out.println("click position:" + position);
                        if (items.get(position).getStatus().equals(SongStatus.AVAILABLE.getFriendlyName()) ||items.get(position).getStatus().equals(SongStatus.DELETED.getFriendlyName())) {
                            askQuestion(position, null, 1);
                        } else if (items.get(position).getStatus().equals(SongStatus.DOWNLOADED.getFriendlyName())) {
                            playSong();
                        } else if (items.get(position).getStatus().equals(SongStatus.DOWNLOADING.getFriendlyName())) {
                            askQuestion(position, null, 2);
                        }
                    }
                })
        );

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //just for Demo
                /*(new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);

                    }
                }, 1500);*/
                syncTokenlyAndUpdateThreads(false);

                //
                // updatesonglist();

            }
        });

        if (fanWalletSettings.isHomeTutorialDialogEnabled() == true)
        {
            setUpHelpFanWallet(false);
        }



        return view;
    }

    private void setUpHelpFanWallet(boolean checkButton) {
        try {
            presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setBannerRes(R.drawable.bannerfanwallet)
                    .setIconRes(R.drawable.banner_tky)
                    .setSubTitle(R.string.fan_wallet_dialog_subtitle)
                    .setBody(R.string.fan_wallet_dialog_body)
                    .setTextFooter(R.string.fan_wallet_footer)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.show();
        } catch (Exception e) {
            errorManager.reportUnexpectedWalletException(
                    Wallets.TKY_FAN_WALLET,
                    UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                    e);
        }
    }

    void syncTokenlyAndUpdateThreads(boolean autosync){

        syncThread =new SyncThreadClass(autosync); // Firstthread
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            {
                syncThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            syncThread.execute();
            } else{
                // Below Api Level 13
                syncThread.execute();
            }

       /* monitorThreadClass=new MonitorThreadClass(); // Secondthread
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)// Above Api Level 13
        {
            monitorThreadClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else // Below Api Level 13
        {
            monitorThreadClass.execute();
        }*/

    }

    int searchInViewBySongId(UUID song_Id){

        for(int i=0;i<items.size();i++){
            if(items.get(i).getSong_id().equals(song_Id)){
                return i;
            }
        }
        return 0;
    }

    void searchInViewPosition(Song songOfBroadcast,UUID song_Id){
       int position;
       String songInfo;
       List<String> listComposerAndSongNameOnView=new ArrayList<>();
        for(SongItems songitems : items){
               if(!listComposerAndSongNameOnView.contains(songitems.getArtist_name()+"@#@#"+songitems.getSong_name())){
                   //   System.out.println("TKY_VIEW songs"+songitems.getUsername()+"@#@#"+songitems.getSong_name());
                   listComposerAndSongNameOnView.add(songitems.getArtist_name()+"@#@#"+songitems.getSong_name());
               }
        }
        songInfo=songOfBroadcast.getComposers()+"@#@#"+songOfBroadcast.getName();
        if(!listComposerAndSongNameOnView.contains(songInfo)){
            items.add(new SongItems(R.drawable.tky_tokenly_album, songOfBroadcast.getName(), songOfBroadcast.getComposers(), SongStatus.DOWNLOADING.getFriendlyName(),song_Id, 0, false));
            adapter.setFilter(items,false,0);

        }

    }

    void compareViewAndDatabase(){
        String databaseInfo;
        List<String> listComposerAndSongNameOnView=new ArrayList<>();
        List<WalletSong> songsInDatabase=new ArrayList<>();

        try {
            songsInDatabase=fanWalletModule.getAvailableSongs();
        } catch (CantGetSongListException e) {
            e.printStackTrace();
        }

        for(SongItems songitems : items){
            if(!listComposerAndSongNameOnView.contains(songitems.getArtist_name()+"@#@#"+songitems.getSong_name())){
             //   System.out.println("TKY_VIEW songs"+songitems.getUsername()+"@#@#"+songitems.getSong_name());
                listComposerAndSongNameOnView.add(songitems.getArtist_name()+"@#@#"+songitems.getSong_name());
            }
        }
        if(songsInDatabase.size()>listComposerAndSongNameOnView.size()){
            for (WalletSong walletitems :songsInDatabase){
                databaseInfo=walletitems.getComposers()+"@#@#"+walletitems.getName();
              //  System.out.println("TKY_WALLET songs"+walletitems.getComposers()+"@#@#"+walletitems.getName());
                if(!listComposerAndSongNameOnView.contains(databaseInfo)){
                    listComposerAndSongNameOnView.add("TKY_WALLET songs" + walletitems.getComposers() + "@#@#" + walletitems.getName());
                    //System.out.println("TKY_NOT in view" + walletitems.getComposers() + "@#@#" + walletitems.getName());
                    items.add(new SongItems(R.drawable.tky_tokenly_album, walletitems.getName(), walletitems.getComposers(), walletitems.getSongStatus().getFriendlyName(), walletitems.getSongId(), 0, false));
                    adapter.setFilter(items,false,0);
                }
            }

        }
    }

    void swipeEffect(){

   //     ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                return true;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<SongItems> original=new ArrayList<>();
                original.addAll(items);
                askQuestion(position, original, 0);

            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position=viewHolder.getAdapterPosition();
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.END;       // delete one in case you want just one direction
                if(position>=0) {
                    if (items.get(position).getStatus() == SongStatus.DOWNLOADING.getFriendlyName() || items.get(position).getStatus() == SongStatus.NOT_AVAILABLE.getFriendlyName()) {
                        return 0;
                    } else {
                        return makeMovementFlags(dragFlags, swipeFlags);
                    }
                }else{
                    return 0;
                }

             //   return  items.get(position).getStatus()=="Downloading"? 0:makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.tky_trash);
                        //Start draw left-top to right-bottom     RectF (left,top,right,bottom)
                        RectF icon_dest = new RectF((float) itemView.getLeft() + (width/4) ,(float) itemView.getTop() + 0.75f*width,(float) itemView.getLeft()+ 2f*width,(float)itemView.getBottom() -0.75f*width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.tky_chat);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 1.75f*width ,(float) itemView.getTop() + 0.75f*width,(float) itemView.getRight() - width/4,(float)itemView.getBottom() - 0.75f*width);
                        c.drawBitmap(icon, null, icon_dest, p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    void askQuestion(final int position, final List<SongItems> original, int whocallme){
        final UUID songid;
        if(whocallme==0) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(view.getContext());
                dialog1.setTitle("FanWallet");
                dialog1.setMessage("Do you really want to delete '" + items.get(position).getSong_name() + "' from your device?");
                dialog1.setCancelable(false);
                dialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {

                        deleteSong(position);

                    }
                });
                dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {
                        adapter.setFilter(original, false, 0);
                    }
                });
                dialog1.show();
        }else if(whocallme==1){
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(view.getContext());
            dialog1.setTitle("FanWallet");
            dialog1.setMessage("Do you really want to download '" + items.get(position).getSong_name() + "'?");
            dialog1.setCancelable(false);
            dialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int id) {

                    downloadSong(position);

                }
            });
            dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int id) {
                    //Nothing happen
                }
            });
            dialog1.show();
        }else if(whocallme==2){
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(view.getContext());
            dialog1.setTitle("FanWallet");
            dialog1.setMessage("Do you really want to Cancel the download of '" + items.get(position).getSong_name() + "'?");
            dialog1.setCancelable(false);
            dialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int id) {

                    cancelSong(position);


                }
            });
            dialog1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int id) {
                    //Nothing happen
                }
            });
            dialog1.show();
        }
    }

    void deleteSong(int position){

        try {
            fanWalletModule.deleteSong(items.get(position).getSong_id());
            items.get(position).setStatus(SongStatus.DELETED.getFriendlyName());
            items.get(position).setProgress(0);
            adapter.setFilter(items,false,0);
        } catch (CantDeleteSongException e) {
            errorManager.reportUnexpectedWalletException(Wallets.TKY_FAN_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        } catch (CantUpdateSongStatusException e) {
            errorManager.reportUnexpectedWalletException(Wallets.TKY_FAN_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }

    }

    void downloadSong(int position){
        downloadThread = new DownloadThreadClass(position);
        downloadThread.execute();

    }

    void cancelSong(int position){
        try {
            if(downloadThread !=null){
                downloadThread.cancel(true);
            }
            else if(syncThread !=null){
                myCancelHandler.post(myRunnableCancel);
            }



        } catch (Exception e) {
            Log.v(TAG, "EXCEPTION" + e);
        }

    }

    final Runnable myRunnableCancel = new Runnable() {

        public void run() {
            fanwalletSession.getModuleManager().cancelDownload();
        }
    };

    public void updateViewForBroadcaster(FermatBundle bundle) {

        int position=0;

            try {
                System.out.println("TKY_Broad_Arrive:");

                if (bundle.contains(BroadcasterNotificationType.SONG_INFO.getCode())) {
                    System.out.println("TKY_BROAD_SONGINFO:"+ ((Song)bundle.getSerializable(BroadcasterNotificationType.SONG_INFO.getCode())).getName());
                    searchInViewPosition((Song) bundle.getSerializable(BroadcasterNotificationType.SONG_INFO.getCode()),
                            (UUID) bundle.getSerializable(BroadcasterNotificationType.SONG_ID.getCode()));
                }

                if (bundle.contains(BroadcasterNotificationType.DOWNLOAD_PERCENTAGE.getCode())) {
                    System.out.println("TKY_BROAD_DOWNLOAD_PERCENTAGE:"+bundle.getString(BroadcasterNotificationType.DOWNLOAD_PERCENTAGE.getCode()));
                    position=searchInViewBySongId((UUID) bundle.getSerializable(BroadcasterNotificationType.SONG_ID.getCode()));
                    updateProgress(position, bundle.getString(BroadcasterNotificationType.DOWNLOAD_PERCENTAGE.getCode()).split("%")[0]);
                }

                if (bundle.contains(BroadcasterNotificationType.DOWNLOAD_EXCEPTION.getCode())) {
                    System.out.println("TKY_BROAD_DOWNLOAD_EXCEPTION:"+bundle.getString(BroadcasterNotificationType.DOWNLOAD_EXCEPTION.getCode()));
                    position=searchInViewBySongId((UUID) bundle.getSerializable(BroadcasterNotificationType.SONG_ID.getCode()));
                    downloadproblem(position);
                }

                if (bundle.contains(BroadcasterNotificationType.SONG_CANCEL.getCode())) {
                    System.out.println("TKY_BROAD_SONG_CANCEL:"+bundle.getString(BroadcasterNotificationType.SONG_CANCEL.getCode()));
                    position=searchInViewBySongId((UUID) bundle.getSerializable(BroadcasterNotificationType.SONG_ID.getCode()));
                    cancelNotification(position);
                }


            } catch (IllegalAccessException e) {
                //errorManager.reportUnexpectedWalletException(Wallets.TKY_FAN_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                System.out.println("TKY_Error On update bundle:" + e);
                e.printStackTrace();
            }
        }



    void updateProgress(int position, String progress){
        System.out.println("Vista(" + position + "):" + progress);
        items.get(position).setProgressbarvissible(true);
        items.get(position).setProgress(Integer.valueOf(progress));
        if(progress.equals("100")){
            items.get(position).setProgressbarvissible(false);
            items.get(position).setStatus(SongStatus.DOWNLOADED.getFriendlyName());
        }
        adapter.setFilter(items,true,position);
    }

    void cancelNotification(int position){
        items.get(position).setProgressbarvissible(false);
        items.get(position).setProgress(Integer.valueOf(0));
        items.get(position).setStatus(SongStatus.AVAILABLE.getFriendlyName());
        adapter.setFilter(items,false,0);
    }
    // TODO: 04/04/16 what happen here?
    void downloadproblem(int position){
        //TODO: to implement
    }

    void playSong(){
        //TODO: to implement
    }


    /* AsyncTask
    Variable type
    Void for the parameters
    Float for the onprogressupdate
    Boolen for the  onPostExecute   */
    public class DownloadThreadClass extends AsyncTask<Void, Float, Boolean> {
        int position;
        HTTPErrorResponse httpErrorResponse;
        UUID songId;
        Fan fanIdentity = getFanIdentity();
        /**
         * parameters position
         */
        public DownloadThreadClass(int position) {
            this.position=position;
        }

        /**
         * Before start thread
         */
        @Override
        protected void onPreExecute() {

            items.get(position).setStatus("Downloading");
            items.get(position).setProgressbarvissible(true);
            songId=items.get(position).getSong_id();
            Log.v(TAG, "Before start download");
            adapter.setFilter(items,true,position);

        }

        /**
         * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal con el método "publishProgress" que ejecuta el método "onProgressUpdate" en hilo Principal
         */
        @Override
        protected Boolean doInBackground(Void... variableNoUsada) {

            float progreso = 0.0f;

            while(items.get(position).getProgress()<100) {
                    if (isCancelled()) {
                       break;
                    }
                    try {
                        //We're going to check if fanIdentity is null
                        if(fanIdentity==null){
                            isFanIdentity=false;
                        } else{
                            isFanIdentity=true;
                            fanwalletSession.getModuleManager().
                                    downloadSong(
                                            songId,
                                            fanIdentity.getMusicUser());
                        }

                    } catch (CantDownloadSongException e) {
                        httpErrorResponse = e.getHttpErrorResponse();
                    } catch (CantUpdateSongStatusException e) {
                        e.printStackTrace();
                    } catch (CantUpdateSongDevicePathException e) {
                        e.printStackTrace();
                    }


            }

            return true;
        }



        /**
         * To update the view
         */
        @Override
        protected void onProgressUpdate(Float... percentProgress) {

        }


        /**
         * after finish receive the value of doInBackground
         */

        protected void onPostExecute(Boolean ready) {
            if(!isFanIdentity){
                //We don't get the identity, we're gonna notify to user.
                Toast.makeText(
                        view.getContext(),
                        "Cannot load a Fan identity",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            //If exists any error from Tokenly API, I'll notify to user.
            if(httpErrorResponse!=null){
                Toast.makeText(
                        view.getContext(),
                        httpErrorResponse.getErrorResponse(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        /**
         * when the method cancel is called
         */
        @Override
        protected void onCancelled() {
            fanwalletSession.getModuleManager().cancelDownload();
        }

    }

    /* AsyncTask
   Variable type
   Void for the parameters
   Float for the onprogressupdate
   Boolen for the  onPostExecute   */
    public class SyncThreadClass extends AsyncTask<Void, WalletSong, Boolean> {
        boolean autoSync;


        /**
         * parameters position
         */
        public SyncThreadClass(boolean autoSync) {
            this.autoSync = autoSync;
        }

        /**
         * Before start thread
         */
        @Override
        protected void onPreExecute() {

        }

        /**
         * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal con el método "publishProgress" que ejecuta el método "onProgressUpdate" en hilo Principal
         */
        @Override
        protected Boolean doInBackground(Void... notUsingObject) {

            try {
                Fan fanIdentity= getFanIdentity();
                //We gonna check if identity is null
                if(fanIdentity!=null){
                    //Not null we gonna proceed with the sync song process.
                    isFanIdentity=true;
                    if(autoSync) {
                        System.out.println("TKY_AutoSync ok");
                        fanwalletSession.getModuleManager().synchronizeSongs(fanIdentity);
                    }else{
                        System.out.println("TKY_ManualSync ok");
                        fanwalletSession.getModuleManager().synchronizeSongsByUser(fanIdentity);
                    }
                } else{
                    //Is null, we gonna notify to the user
                    isFanIdentity=false;
                }

            }catch (Exception e ){
                System.out.println("TKY_Error manual sync:" + e);
            }


            return true;
        }

        /**
         * To update the view
         */
        @Override
        protected void onProgressUpdate(WalletSong... walletitems) {


        }
        /**
         * After finish receive the value of doinbackground
         */

        protected void onPostExecute(Boolean ready) {

            Log.v(TAG, "Game Over SyncThreadClass");
            if(!isFanIdentity){
                //We don't get the identity, we're gonna notify to user.
                Toast.makeText(
                        view.getContext(),
                        "Cannot load a Fan identity",
                        Toast.LENGTH_SHORT)
                        .show();
                //TODO: Miguel, we need to study if we gonna launch the Fan Identity Fragment.
            }

        }
        /**
         * When the method cancel is called
         */
        @Override
        protected void onCancelled() {

        }
    }

    /**
     * This method returns a fan identity
     * @return
     */
    private Fan getFanIdentity(){
        /**
         * Process with hardcoded Fan Identity.
         */
        /*Fan fanIdentity = new TestFan();
        return fanIdentity;*/
        /**
         * Proper implementation.
         */
        try {
            //Get the fan list from the Module.
            List<Fan> fanList = fanWalletModule.listIdentitiesFromCurrentDeviceUser();
            //If the list is empty or null we will return a null
            if(fanList==null || fanList.isEmpty()){
                return null;
            }
            //In this version we will return the first Identity that we get from the list.
            return fanList.get(0);
        } catch (CantListFanIdentitiesException e) {
            //I need to report this, it can be a very grave error.
            errorManager.reportUnexpectedWalletException(
                    Wallets.TKY_FAN_WALLET,
                    UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                    e);
            //In this case, we will return null
            return null;
        }
    }


    /* AsyncTask
  Variable type
  Void for the parameters
  Float for the onprogressupdate
  Boolen for the  onPostExecute   */

   /* public class MonitorThreadClass extends AsyncTask<Void, WalletSong, Boolean> {

        boolean unfinish=true;

        List<WalletSong> songlistofthread=new ArrayList<>();
        List<String> listComposerAndSongNameOnView=new ArrayList<>();
        *//**
         * parameters position
         *//*
        public MonitorThreadClass() {

        }

        *//**
         * Before start thread
         *//*
        @Override
        protected void onPreExecute() {

        }

        *//**
         * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal con el método "publishProgress" que ejecuta el método "onProgressUpdate" en hilo Principal
         *//*
        @Override
        protected Boolean doInBackground(Void... variableNoUsada) {

            while(syncThread.unfinish){
                try {
                    songlistofthread=fanwalletmoduleManager.getFanWalletModule().getAvailableSongs();
                    compareViewAndDatabase(songlistofthread, items);
           //         System.out.println("TKY_Monitor ok");
                } catch (CantGetSongListException e) {
                    System.out.println("tky_monitorthread:"+e);;
                }
            }


            return true;
        }

        void compareViewAndDatabase(List<WalletSong> listAvailableSongs,List<SongItems> listSongInView ){
            String databaseInfo;

            for(SongItems songitems : listSongInView){
                if(!listComposerAndSongNameOnView.contains(songitems.getUsername()+"@#@#"+songitems.getSong_name())){
                    System.out.println("TKY_VIEW songs"+songitems.getUsername()+"@#@#"+songitems.getSong_name());
                    listComposerAndSongNameOnView.add(songitems.getUsername()+"@#@#"+songitems.getSong_name());
                }
            }
            if(listAvailableSongs.size()>listComposerAndSongNameOnView.size()){
                for (WalletSong walletitems :listAvailableSongs){
                    databaseInfo=walletitems.getComposers()+"@#@#"+walletitems.getName();
                    System.out.println("TKY_WALLET songs"+walletitems.getComposers()+"@#@#"+walletitems.getName());
                    if(!listComposerAndSongNameOnView.contains(databaseInfo)){
                        listComposerAndSongNameOnView.add("TKY_WALLET songs"+walletitems.getComposers()+"@#@#"+walletitems.getName());
                        System.out.println("TKY_NOT in view"+walletitems.getComposers()+"@#@#"+walletitems.getName());
                        publishProgress(walletitems);
                    }
                }

            }
        }

        *//**
         * To update the view
         *//*
        @Override
        protected void onProgressUpdate(WalletSong... walletitems) {
            System.out.println("TKY_PUBLISHPROGRESS"+Arrays.toString(walletitems));
            items.add(new SongItems(R.drawable.tky_tokenly_album, walletitems[0].getName(), walletitems[0].getComposers(),SongStatus.DED.getCode(),walletitems[0].getSongId(),0,false));
            adapter.setFilter(items);
        }


        *//**
         * afte finish receive the value of doinbackground
         *//*

        protected void onPostExecute(Boolean ready) {

            swipeContainer.setRefreshing(false);
            Log.v(TAG, "Game Over MonitoringThreadClass");

        }
        *//**
         * when the method cancel is called
         *//*
        @Override
        protected void onCancelled() {

        }





    }*/

}
