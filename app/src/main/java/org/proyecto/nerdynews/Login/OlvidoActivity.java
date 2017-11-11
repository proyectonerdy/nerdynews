package org.proyecto.nerdynews.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.proyecto.nerdynews.R;

public class OlvidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido);
    }


    public void resetField(View v){
        if(v.getId() == R.id.crossemailolvido){
            AutoCompleteTextView ac = (AutoCompleteTextView) this.findViewById(R.id.emailolvido);
            ac.setText("");
        }
        //si es otro se ignora
    }

    public void irLogin(View v){
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
    }

    public void olvidocontrasena(View v){
        //mostramos un toast y finalizamos la actividad
        AutoCompleteTextView r = (AutoCompleteTextView) this.findViewById(R.id.emailolvido);
        if(r.getText()==null || r.getText().length()==0){
            Toast.makeText(this,R.string.faltaemail,Toast.LENGTH_LONG).show();
            return;
        }
        String email = getString(R.string.ejecutadoolvidocontrasena, r.getText());
        Toast.makeText(this,email,Toast.LENGTH_LONG).show();
        View view = this.getCurrentFocus();
        if(view!=null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
    }

}
