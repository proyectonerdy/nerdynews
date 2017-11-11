package org.proyecto.nerdynews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class OlvidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido);
    }


    public void resetField(View v){
        if(v.getId() == R.id.crossemail){
            AutoCompleteTextView ac = (AutoCompleteTextView) this.findViewById(R.id.email);
            ac.setText("");
        }
        if(v.getId() == R.id.crosspassword){
            EditText e = (EditText) this.findViewById(R.id.password);
            e.setText("");
        }
        //si es otro se ignora
    }

    public void irLogin(View v){
        finish();
    }

    public void olvidocontrasena(View v){
        //mostramos un toast y finalizamos la actividad
        AutoCompleteTextView r = (AutoCompleteTextView) this.findViewById(R.id.email);
        if(r.getText()==null || r.getText().length()==0){
            Toast.makeText(this,R.string.faltaemail,Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this,String.format(R.string.ejecutadoolvidocontrasena,r.getText()),Toast.LENGTH_LONG).show();
        finish();
    }

}
