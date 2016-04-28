package com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.fragments;

/**
 * Created by edicson on 23/03/16.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.transformation.CircleTransform;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.Validate;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.session.ArtistIdentitySubAppSession;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.session.SessionConstants;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.util.CommonLogger;
import com.bitdubai.fermat_art_api.all_definition.enums.ArtExternalPlatform;
import com.bitdubai.fermat_art_api.layer.identity.artist.interfaces.Artist;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.Fanatic;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.Artist.ArtistIdentityManagerModule;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.Artist.ArtistIdentitySettings;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import com.bitdubai.sub_app.artist_identity.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;


public class CreateArtistIndetityFragment extends AbstractFermatFragment<ArtistIdentitySubAppSession,SubAppResourcesProviderManager> {



    private static final String TAG = "CreateArtArtistIdentity";
    private static final int CREATE_IDENTITY_FAIL_MODULE_IS_NULL = 0;
    private static final int CREATE_IDENTITY_FAIL_NO_VALID_DATA = 1;
    private static final int CREATE_IDENTITY_FAIL_MODULE_EXCEPTION = 2;
    private static final int CREATE_IDENTITY_SUCCESS = 3;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;

    private static final int CONTEXT_MENU_CAMERA = 1;
    private static final int CONTEXT_MENU_GALLERY = 2;
    private ArtistIdentitySubAppSession artistIdentitySubAppSession;
    private byte[] artistImageByteArray;
    private ArtistIdentityManagerModule moduleManager;
    private ErrorManager errorManager;
    private Button createButton;
    private EditText mArtistUserName;
    private ImageView artistImage;
    private RelativeLayout relativeLayout;
    private Menu menuHelp;
    private Artist identitySelected;
    private boolean isUpdate = false;
    private EditText mArtistExternalPassword;
    private Spinner mArtistExternalPlatform;
    private Spinner mArtistExternalName;
    private SettingsManager<ArtistIdentitySettings> settingsManager;
    private ArtistIdentitySettings artArtistPreferenceSettings = null;
    private boolean updateProfileImage = false;
    private boolean contextMenuInUse = false;
    private boolean authenticationSuccessful = false;
    private UUID externalPlatformID;
    private Handler handler;
    boolean checked =false;


    public static CreateArtistIndetityFragment newInstance() {
        return new CreateArtistIndetityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            moduleManager = appSession.getModuleManager();
            errorManager = appSession.getErrorManager();
            setHasOptionsMenu(false);
            settingsManager = appSession.getModuleManager().
                    getSettingsManager();

            try {
                if (appSession.getAppPublicKey()!= null){
                    artArtistPreferenceSettings = settingsManager.loadAndGetSettings(
                            appSession.getAppPublicKey());
                }else{
                    artArtistPreferenceSettings = settingsManager.loadAndGetSettings("public_key_art_artist_identity");
                }

            } catch (Exception e) {
                artArtistPreferenceSettings = null;
            }

            if (artArtistPreferenceSettings == null) {
                artArtistPreferenceSettings = new ArtistIdentitySettings();
                artArtistPreferenceSettings.setIsPresentationHelpEnabled(false);
                if(settingsManager != null){
                    if (appSession.getAppPublicKey()!=null){
                        settingsManager.persistSettings(
                                appSession.getAppPublicKey(), artArtistPreferenceSettings);
                    }else{
                        settingsManager.persistSettings("public_key_art_artist_identity", artArtistPreferenceSettings);
                    }
                }
            }
        }catch (Exception e){
            errorManager.reportUnexpectedSubAppException(
                    SubApps.ART_ARTIST_IDENTITY,
                    UnexpectedSubAppExceptionSeverity.DISABLES_THIS_FRAGMENT,
                    e);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootLayout = inflater.inflate(R.layout.fragment_art_create_artist_identity, container, false);
       initViews(rootLayout);
        setUpIdentity();

        return rootLayout;
    }

    private void setUpIdentity() {
        try {
            identitySelected = (Artist) appSession.getData(
                    SessionConstants.IDENTITY_SELECTED);

            if (identitySelected != null) {
                loadIdentity();
            } else {
                List<Artist> lst = moduleManager.listIdentitiesFromCurrentDeviceUser();
                if(!lst.isEmpty()){
                    identitySelected = lst.get(0);
                }
                if (identitySelected != null) {
                    loadIdentity();
                    isUpdate = true;
                    createButton.setText("Save changes");
                }
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedSubAppException(
                    SubApps.ART_ARTIST_IDENTITY,
                    UnexpectedSubAppExceptionSeverity.DISABLES_THIS_FRAGMENT,
                    e);
        }
    }
    private void loadIdentity(){
        //System.out.println("Image is : " + identitySelected.getProfileImage());
        if (identitySelected.getProfileImage() != null) {
            Bitmap bitmap = null;
            if (identitySelected.getProfileImage().length > 0) {
                bitmap = BitmapFactory.decodeByteArray(
                        identitySelected.getProfileImage(),
                        0,
                        identitySelected.getProfileImage().length);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.afi_profile_male);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            artistImageByteArray = toByteArray(bitmap);
            artistImage.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));
        }
        mArtistUserName.setText(identitySelected.getAlias());
        List<String> arraySpinner = new ArrayList<>();
        arraySpinner.add("Select a Platform...");
        arraySpinner.addAll(ArtExternalPlatform.getArrayItems());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinner);
        mArtistExternalPlatform.setAdapter(adapter);
        ArtExternalPlatform[] externalPlatforms = ArtExternalPlatform.values();
//        for (int i=0; i<externalPlatforms.length;i++){
//            if(externalPlatforms[i].getCode().equals(
//                    identitySelected.getExternalPlatform().getCode())){
//                mArtistExternalPlatform.setSelection(i);
//                break;
//            }
//        }
    }
    private void initViews(View layout) {
        createButton = (Button) layout.findViewById(R.id.create_art_artist_identity);
        mArtistUserName = (EditText) layout.findViewById(R.id.aai_username);
        artistImage =  (ImageView) layout.findViewById(R.id.aai_artist_image);
        mArtistExternalPlatform = (Spinner) layout.findViewById(R.id.aai_external_platform);
        mArtistExternalName = (Spinner) layout.findViewById(R.id.aai_userIdentityName);
        relativeLayout = (RelativeLayout) layout.findViewById(R.id.aai_layout_user_image);
        createButton.setText((!isUpdate) ? "Create" : "Update");
        mArtistUserName.requestFocus();


        List<String> arraySpinner = new ArrayList<>();
        arraySpinner.add("Select a Platform...");
        arraySpinner.addAll(ArtExternalPlatform.getArrayItems());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinner);

        mArtistExternalPlatform.setAdapter(adapter);
        externalPlatformSpinnerListener();
        externalUserSpinnerListener();
        mArtistUserName.requestFocus();
        registerForContextMenu(artistImage);

        artistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonLogger.debug(TAG, "Entrando en ArtImage.setOnClickListener");
                getActivity().openContextMenu(artistImage);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonLogger.debug(TAG, "Entrando en createButton.setOnClickListener");


                int resultKey = 0;
                try {
                    resultKey = createNewIdentity();
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                }
                switch (resultKey) {
                    case CREATE_IDENTITY_SUCCESS:
//                        changeActivity(Activities.CCP_SUB_APP_INTRA_USER_IDENTITY.getCode(), appSession.getAppPublicKey());
                        if (!isUpdate) {
                            Toast.makeText(getActivity(), "Identity created", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_EXCEPTION:
                        Toast.makeText(getActivity(), "Error al crear la identidad", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_NO_VALID_DATA:
                        Toast.makeText(getActivity(), "La data no es valida", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_IS_NULL:
                        Toast.makeText(getActivity(), "No se pudo acceder al module manager, es null", Toast.LENGTH_LONG).show();
                        break;
                }

            }

        });
    }

    private int createNewIdentity() throws InvalidParameterException {
        String artistName = mArtistUserName.getText().toString();
        ArtExternalPlatform externalPlatform = ArtExternalPlatform.getDefaultExternalPlatform();
        if(mArtistExternalPlatform.isSelected()){
            externalPlatform = ArtExternalPlatform.getArtExternalPlatformByLabel(
                    mArtistExternalPlatform.getSelectedItem().toString());
        }
        boolean dataIsValid = validateIdentityData(
                artistName,
                artistImageByteArray);
        if(dataIsValid){
            if(moduleManager != null){
                try{
                    if(!isUpdate){
                        moduleManager.createArtistIdentity(artistName,artistImageByteArray,(externalPlatformID == null) ? null : externalPlatformID,externalPlatform);
                    }else{
                        if(updateProfileImage)
                            moduleManager.updateArtistIdentity(artistName,identitySelected.getPublicKey(),artistImageByteArray,(externalPlatformID == null) ? null : externalPlatformID,externalPlatform);
                        else
                            moduleManager.updateArtistIdentity(artistName,identitySelected.getPublicKey(),identitySelected.getProfileImage(),(externalPlatformID == null) ? null : externalPlatformID,externalPlatform);
                    }
                }catch (Exception e){
                    errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                    e.printStackTrace();
                }
                return CREATE_IDENTITY_SUCCESS;
            }
            return CREATE_IDENTITY_FAIL_MODULE_IS_NULL;
        }
        return CREATE_IDENTITY_FAIL_NO_VALID_DATA;
    }



    private boolean validateIdentityData(String ArtistExternalName,  byte[] ArtistImageBytes) {
        if (ArtistExternalName.isEmpty())
            return false;
        if (ArtistImageBytes == null)
            return false;
        if (ArtistImageBytes.length > 0)
            return true;
        return true;
    }

    private byte[] convertImage(int resImage){
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), resImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }



    private void externalPlatformSpinnerListener(){
        mArtistExternalPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    List<String> arraySpinner = new ArrayList<>();
                    arraySpinner.add("Select an Identity...");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity(),
                            android.R.layout.simple_spinner_item,
                            arraySpinner
                    );
                    if (parent.getItemAtPosition(position) != 0) {
                        arraySpinner.addAll(getArtistIdentityByPlatform(ArtExternalPlatform.getArtExternalPlatformByLabel(parent.getItemAtPosition(position).toString())));
                        adapter = new ArrayAdapter<>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                arraySpinner
                        );

                    }

                    mArtistExternalName.setAdapter(adapter);

                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(
                            SubApps.ART_ARTIST_IDENTITY,
                            UnexpectedSubAppExceptionSeverity.DISABLES_THIS_FRAGMENT,
                            e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private List<String> getArtistIdentityByPlatform(ArtExternalPlatform externalPlatform) throws Exception{
        HashMap<UUID, String> artistIdentityByPlatform = moduleManager.listExternalIdentitiesFromCurrentDeviceUser().get(externalPlatform);
        List<String> identityNameList = new ArrayList<>();
        if(!Validate.isObjectNull(artistIdentityByPlatform)){
            for (Map.Entry<UUID, String> entry2 : artistIdentityByPlatform.entrySet()) {
                identityNameList.add(entry2.getValue());
            }
        }

        return identityNameList;
    }

    private List<UUID> getArtistIdentityIdByPlatform(ArtExternalPlatform externalPlatform) throws Exception{
        HashMap<UUID, String> artistIdentityByPlatform =  moduleManager.listExternalIdentitiesFromCurrentDeviceUser().get(externalPlatform);
        List<UUID> identityIdList = new ArrayList<>();
        if(!Validate.isObjectNull(artistIdentityByPlatform)){
            for (Map.Entry<UUID, String> entry2 : artistIdentityByPlatform.entrySet()) {
                identityIdList.add(entry2.getKey());

            }
        }


        return identityIdList;
    }
    private void externalUserSpinnerListener(){
        mArtistExternalName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    externalPlatformID = getArtistIdentityIdByPlatform(ArtExternalPlatform.getArtExternalPlatformByLabel(mArtistExternalPlatform.getSelectedItem().toString())).get(position);
                }catch (IndexOutOfBoundsException e){
                    //Nothing to do here
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(
                            SubApps.ART_ARTIST_IDENTITY,
                            UnexpectedSubAppExceptionSeverity.DISABLES_THIS_FRAGMENT,
                            e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Bitmap to byte[]
     *
     * @param bitmap Bitmap
     * @return byte array
     */
    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void loadImageFromGallery() {
        Intent loadImageIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            ImageView pictureView = artistImage;
            contextMenuInUse = true;
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    updateProfileImage = true;
                    break;
                case REQUEST_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        if (isAttached) {
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
                            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, pictureView.getWidth(), pictureView.getHeight(), true);
                            artistImageByteArray = toByteArray(imageBitmap);
                            updateProfileImage = true;
                            Picasso.with(getActivity()).load(selectedImage).transform(new CircleTransform()).into(artistImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error loading the picture", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            if (pictureView != null && imageBitmap != null)
                pictureView.setImageDrawable(
                        ImagesUtils.getRoundedBitmap(
                                getResources(), imageBitmap));
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose mode");
        menu.setHeaderIcon(getActivity().getResources().getDrawable(R.drawable.ic_camera_green));
        menu.add(Menu.NONE, CONTEXT_MENU_CAMERA, Menu.NONE, "Camera");
        menu.add(Menu.NONE, CONTEXT_MENU_GALLERY, Menu.NONE, "Gallery");

        super.onCreateContextMenu(menu, view, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(!contextMenuInUse) {
            switch (item.getItemId()) {
                case CONTEXT_MENU_CAMERA:
                    dispatchTakePictureIntent();
                    contextMenuInUse = true;
                    return true;
                case CONTEXT_MENU_GALLERY:
                    loadImageFromGallery();
                    contextMenuInUse = true;
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try {
            menu.add(1, 99, 1, "help").setIcon(R.drawable.help_icon)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            final MenuItem action_help = menu.findItem(R.id.action_help);
            menu.findItem(R.id.action_help).setVisible(true);
            action_help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    menu.findItem(R.id.action_help).setVisible(false);
                    return false;
                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == 99){

            }


        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(
                    UISource.ACTIVITY,
                    UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }



}//main