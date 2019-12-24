package com.osama.daif.bloodbank.view.fragment.homeCycle.staticScreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.osama.daif.bloodbank.R;
import com.osama.daif.bloodbank.data.model.contactUs.ContactUs;
import com.osama.daif.bloodbank.data.model.settings.Settings;
import com.osama.daif.bloodbank.view.activity.HomeCycleActivity;
import com.osama.daif.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static com.osama.daif.bloodbank.data.api.RetrofitClient.getClient;
import static com.osama.daif.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.osama.daif.bloodbank.helper.HelperMethods.showSnackBar;

public class ContactUsFragment extends BaseFragment {

    @BindView(R.id.contact_us_fragment_tv_phone_number)
    TextView contactUsFragmentTvPhoneNumber;
    @BindView(R.id.contact_us_fragment_tv_email)
    TextView contactUsFragmentTvEmail;
    @BindView(R.id.contact_us_fragment_btn_facebook)
    ImageView contactUsFragmentBtnFacebook;
    @BindView(R.id.contact_us_fragment_btn_instagram)
    ImageView contactUsFragmentBtnInstagram;
    @BindView(R.id.contact_us_fragment_btn_twitter)
    ImageView contactUsFragmentBtnTwitter;
    @BindView(R.id.contact_us_fragment_btn_youtube)
    ImageView contactUsFragmentBtnYoutube;
    @BindView(R.id.contact_us_fragment_txt_subject)
    EditText contactUsFragmentTxtSubject;
    @BindView(R.id.contact_us_fragment_txt_content)
    EditText contactUsFragmentTxtContent;
    @BindView(R.id.contact_us_fragment_btn_send)
    Button contactUsFragmentBtnSend;
    @BindView(R.id.contact_us_fragment_progress_bar)
    ProgressBar contactUsFragmentProgressBar;
    private Unbinder unbinder = null;

    HomeCycleActivity homeCycleActivity;

    private String facebookUrl;
    private String youtubeUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String apiToken;
    private String phoneNumber;
    private String email;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind (this, view);
        homeCycleActivity = (HomeCycleActivity) getActivity ( );
        assert homeCycleActivity != null;
        homeCycleActivity.editToolbarTxtSup (R.string.contact_us);
        homeCycleActivity.appbarVisibility (View.VISIBLE);
        homeCycleActivity.setBackBtnVisibility (View.VISIBLE);
        homeCycleActivity.bottomNavigationVisibility (View.GONE);
        apiToken = loadUserData (getActivity ( )).getApiToken ( );
        // Inflate the layout for this fragment
        initFragment ( );
        getContactUs ( );
        contactUsFragmentBtnSend.setOnClickListener (v -> {
            String subject = contactUsFragmentTxtSubject.getText ( ).toString ( ).trim ( );
            String message = contactUsFragmentTxtContent.getText ( ).toString ( ).trim ( );
            if (subject.length ( ) == 0 || subject.contentEquals ("")) {
                return;
            }
            contactUsFragmentProgressBar.setVisibility (View.VISIBLE);
            getClient ( ).setContactUsMessage (apiToken, subject, message).enqueue (new Callback<ContactUs> ( ) {
                @Override
                public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                    if (response.body ( ).getStatus ( ) == 1) {
                        contactUsFragmentProgressBar.setVisibility (View.GONE);
                        showSnackBar (v, response.body ( ).getMsg ( ));
                    }
                }

                @Override
                public void onFailure(Call<ContactUs> call, Throwable t) {

                }
            });
        });

        return view;
    }

    private void getContactUs() {
        contactUsFragmentProgressBar.setVisibility (View.VISIBLE);
        getClient ( ).getSetting (apiToken).enqueue (new Callback<Settings> ( ) {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                if (response.body ( ).getStatus ( ) == 1) {
                    contactUsFragmentProgressBar.setVisibility (View.GONE);
                    phoneNumber =  response.body ( ).getData ( ).getPhone ( );
                    email = response.body ( ).getData ( ).getEmail ( );
                    contactUsFragmentTvPhoneNumber.setText (phoneNumber);
                    contactUsFragmentTvEmail.setText (email);
                    facebookUrl = response.body ( ).getData ( ).getFacebookUrl ( );
                    youtubeUrl = response.body ( ).getData ( ).getYoutubeUrl ( );
                    instagramUrl = response.body ( ).getData ( ).getInstagramUrl ( );
                    twitterUrl = response.body ( ).getData ( ).getTwitterUrl ( );
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
                contactUsFragmentProgressBar.setVisibility (View.GONE);
                showNoConnectionDialog ();
            }
        });
    }

    private void showNoConnectionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ( ));
        builder.setMessage (R.string.no_connection);
        builder.setPositiveButton (R.string.done, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss ( );
                homeCycleActivity.setSelection (R.id.nav_home);
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create ( );
        alertDialog.show ( );
    }

    private void startBrowsing(String url) {
        Intent i = new Intent (Intent.ACTION_VIEW);
        i.setData (Uri.parse (url));
        startActivity (i);
    }

    @Override
    public void onBack() {
        super.onBack ( );
    }

    @OnClick({R.id.contact_us_fragment_btn_facebook, R.id.contact_us_fragment_btn_instagram, R.id.contact_us_fragment_btn_twitter, R.id.contact_us_fragment_btn_youtube, R.id.contact_us_fragment_tv_phone_number, R.id.contact_us_fragment_tv_email})
    public void onViewClicked(View view) {
        switch (view.getId ( )) {
            case R.id.contact_us_fragment_btn_facebook:
                startBrowsing (facebookUrl);
                break;
            case R.id.contact_us_fragment_btn_instagram:
                startBrowsing (instagramUrl);
                break;
            case R.id.contact_us_fragment_btn_twitter:
                startBrowsing (twitterUrl);
                break;
            case R.id.contact_us_fragment_btn_youtube:
                startBrowsing (youtubeUrl);
                break;
            case R.id.contact_us_fragment_tv_phone_number:

                if (phoneNumber != null) {
                    Intent callIntent = new Intent (Intent.ACTION_CALL);
                    callIntent.setData (Uri.parse ("tel:" + phoneNumber));
                    if (ContextCompat.checkSelfPermission (getActivity ( ), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity (callIntent);
                    } else {
                        requestPermissions (new String[]{CALL_PHONE}, 1);
                    }
                }
                break;
            case R.id.contact_us_fragment_tv_email:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email});
//                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Email Subject");
//                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My email content");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                break;
        }
    }



}
