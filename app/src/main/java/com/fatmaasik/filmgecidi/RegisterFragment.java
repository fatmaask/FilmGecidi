package com.fatmaasik.filmgecidi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;
import java.util.HashMap;


public class RegisterFragment extends Fragment {
    Activity titleChange;
    EditText username_input;
    EditText password_input;
    EditText name_input;
    EditText email_input;
    ImageView image_input;
    String username, password, name, email;
    String error_message= "";
    Boolean error = false;
    Button signUp;
    SessionManager session;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String URL = "http://fatmaasik.hol.es/api/v1/register";

    public RegisterFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment
        username_input = (EditText) v.findViewById(R.id.r_username);
        name_input = (EditText) v.findViewById(R.id.r_name);
        email_input = (EditText) v.findViewById(R.id.r_email);
        password_input = (EditText) v.findViewById(R.id.r_password);
        image_input = (ImageView) v.findViewById(R.id.r_image);


        image_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        signUp = (Button) v.findViewById(R.id.email_register_button);
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                username = username_input.getText().toString();
                password = password_input.getText().toString();
                name = name_input.getText().toString();
                email = email_input.getText().toString();

                if(username.matches("")){
                    error_message += "Kullanıcı Adı Boş Olamaz\n";
                    error = true;
                }
                if(name.matches("")){
                    error_message += "İsim Alanı Boş Olamaz\n";
                    error = true;
                }
                if(!isValidEmail(email)){
                    error_message += "Email Adresi Geçerli Değil\n";
                    error = true;
                }
                int sifre_karakter = password.length();
                if(sifre_karakter<6 || sifre_karakter==0){
                    error_message += "Şifre 6 Karakterden Az Olamaz\n";
                    error=true;
                }

                if(error){//hata varsa AlertDialog ile kullanıcıyı uyarıyoruz.
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Hata");
                    alertDialog.setMessage(error_message);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password_input.setText("");
                            error_message = "";
                            error = false;
                        }
                    });
                    alertDialog.show();
                }else{//Hata yoksa Asynctask classı çağırıyoruz.İşlemlere orda devam ediyoruz
                    new JSONAsyncTask().execute(username, name, email, password );
                }
            }


        });

        titleChange.setTitle("Kaydol");
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            image_input.setImageURI(selectedImage);
        }
    }
    public void onAttach(Context context) {
        super.onAttach(context);

        titleChange = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        JSONParser jsonParser = new JSONParser();
        ProgressDialog dialog;
        JSONObject register;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Yüklenirken lütfen bekleyin");
            dialog.setTitle("Sunucuya bağlanıyor");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... args) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", args[0]);
                params.put("name", args[1]);
                params.put("email", args[2]);
                params.put("password", args[3]);

                Log.d("request", "starting");
                register = jsonParser.makeLogin(URL, "POST", params);



                return true;
                //------------------>>

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            dialog.cancel();
            try {
                if (register.getString("MESSAGE").equals("success")){
                    session.createLoginSession(register.getJSONObject("DATA").getString("name"),
                                               register.getJSONObject("DATA").getString("email"),
                                               register.getJSONObject("DATA").getString("user_id"),
                                               register.getJSONObject("DATA").getString("avatar"));
                    Toast.makeText(getContext(),"Kullanıcı eklendi", Toast.LENGTH_LONG).show();
                }
                else{
                    ((MainActivity) getActivity()).displaySelectedScreen(0, null);
                    Toast.makeText(getContext(),"Kullanıcı var", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
