package com.bakota.androidapps.firebasecloudstoredbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TAG = MainActivity.class.getSimpleName();

    TextInputEditText nom, prenom,phone;

    //on le déclare comme variable globale pour pouvoir l'utiliser n'importe où
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation de cloud firestore

         db = FirebaseFirestore.getInstance();

        nom = findViewById(R.id.nom_edit);
        prenom = findViewById(R.id.prenom_edit);
        phone = findViewById(R.id.numero_edit);


        //définition du click sur le bouton
        findViewById(R.id.btn_save).setOnClickListener(this);


    }


    /**
     * Permet de sauvegarder les données saisies
     * */
    private void sendDataToFirestore(){
        String enteredNom = nom.getText().toString(),
                enteredPnom = prenom.getText().toString(),
                enteredPhone = phone.getText().toString();

        //on crée l'objet qui sera e,voyé

        //il faut préciser que le stockage se fait sous forme clé faleur
        //puisqu'il s"agit d"une bs NoSql
        //d'où l'instanciation d"un objet de type Map
        //pour ceux qui ont déjà utilisé Mongo certains termes vont vous semblez
        //familiers
        Map<String, Object> user = new HashMap<>();

        user.put("nom", enteredNom);
        user.put("prenom", enteredPnom);
        user.put("phone", enteredPhone);

        //demain on peut ajouter un attrbut de la sorte user.put("nouvelattribut", valeur);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        //tout s'est bien passé on affiche un message à l'utilisateur
                        showToast(getString(R.string.save_success));

                        //affchage du résultat dans le log
                        Log.d(TAG, "onSuccess: addUser ID "+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //ça a échoué
                        showToast(getString(R.string.save_failed));

                        //affichage du problème dans les logs

                        Log.w(TAG, "onFailure: addUser ", e );
                    }
                });

    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    //définit l'action qui suit le click d'un élément de notre vue

    @Override
    public void onClick(View v) {

        //on n'a qu'un seul élément sur lequel on cliqué donc pas besoin de prendre l'id

        //on se rassure qu'il a entré toutes les infos
        if (TextUtils.isEmpty(nom.getText()) ||
                TextUtils.isEmpty(prenom.getText()) ||
                TextUtils.isEmpty(phone.getText())) {

           showToast(getString(R.string.remplir_tous_les_champs));
        }else {
            // il a remplit tous les champs, on peut envoyer ça a firestore
            sendDataToFirestore();
        }

    }
}
