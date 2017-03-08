package com.fatmaasik.filmgecidi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONObject;
import java.util.HashMap;


public class LoginFragment extends Fragment {
    Activity titleChange;
    EditText username_input;
    EditText password_input;
    String username, password;
    String error_message= "";
    Boolean error = false;
    Button signIn;
    Button signUp;
    SessionManager session;
    private static final String URL = "http://fatmaasik.hol.es/api/v1/login";

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment

        // Session Manager
        session = new SessionManager(getActivity().getApplicationContext());

        username_input = (EditText) v.findViewById(R.id.username);
        password_input = (EditText) v.findViewById(R.id.password);
        signIn = (Button) v.findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                username = username_input.getText().toString();
                password = password_input.getText().toString();


                if(username.matches("")){
                    error_message += "Üye No ya da E-Mail Alanı Boş Olamaz\n";
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
                    new JSONAsyncTask().execute(username, password);
                }
            }


        });

        signUp = (Button) v.findViewById(R.id.email_register_button);
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((MainActivity) getActivity()).displaySelectedScreen(R.id.email_register_button, null);
            }
        });
        titleChange.setTitle("Giriş Yap");
        return v;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        titleChange = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        JSONParser jsonParser = new JSONParser();
        ProgressDialog dialog;
        JSONObject login;

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
                params.put("password", args[1]);

                Log.d("request", "starting");
                login = jsonParser.makeLogin(URL, "POST", params);


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
                if (login.getString("MESSAGE").equals("success")){
                    session.createLoginSession(login.getJSONObject("DATA").getString("name"),
                                               login.getJSONObject("DATA").getString("email"),
                                               login.getJSONObject("DATA").getString("id"),
                                               login.getJSONObject("DATA").getString("avatar"));
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Hata");
                    alertDialog.setMessage("Kullanıcı adı veya şifre yanlış");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            password_input.setText("");

                        }
                    });
                    alertDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
