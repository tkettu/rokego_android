package com.teroki.rokego_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teroki.rokego_db.DBHelper;


/**
 * Created by Tero on 6.11.2017.
 */

public class EditExercise extends Activity {

    TextView exercise;
    String exerciseStr;
    Button deleteBtn;
    long id;
    private String LOG_TAG = "EditExercise";
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        db = new DBHelper(this);
        Intent intent = getIntent();
        exercise = (TextView) findViewById(R.id.single_exercise);

        id = intent.getLongExtra(Exercises.EXERCISE_MSG, -1);
        if ( id == -1){
            Log.e(LOG_TAG, "Not iterable item");
            exerciseStr = "ERROR, WRONG EXERCISE";
        }else{
            exerciseStr = db.getExercise((int)id).toString();
        }


        exercise.setText(exerciseStr);

        deleteBtn = (Button) findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteExercise(db.getExercise((int)id));
                Intent intent = new Intent(EditExercise.this, Exercises.class);
                startActivity(intent);
            }
        });
    }
}
