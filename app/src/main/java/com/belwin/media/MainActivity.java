package com.belwin.media;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    EditText et0,et1,et2,et3,et4,et5,et6,et7;
    Button b1,b2,b3;
    AudioFile af;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);

        et0=(EditText)findViewById(R.id.et0);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        et6=(EditText)findViewById(R.id.et6);
        et7=(EditText)findViewById(R.id.et7);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tag tag=af.getTag();
                try {
                    tag.setField(FieldKey.ALBUM  , et0.getText().toString());
                    tag.setField(FieldKey.ARTIST , et1.getText().toString());
                    tag.setField(FieldKey.TITLE  , et2.getText().toString());
                    tag.setField(FieldKey.TRACK  , et3.getText().toString());
                    tag.setField(FieldKey.COMMENT, et4.getText().toString());
                    tag.setField(FieldKey.GENRE  , et5.getText().toString());
                    tag.setField(FieldKey.MEDIA  , et6.getText().toString());
                    tag.setField(FieldKey.YEAR   , et7.getText().toString());
                    af.setTag(tag);
                    af.commit();
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                } catch (FieldDataInvalidException e) {
                    e.printStackTrace();
                } catch (CannotWriteException e) {
                    e.printStackTrace();
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                af=null;
                et0.setText("");
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                et7.setText("");
                b2.setVisibility(View.GONE);
                b3.setVisibility(View.GONE);
            }
        });
    }
    @Override
     protected void onActivityResult(int reqCode, int resCode, Intent data) {
         if(reqCode==1 && resCode==RESULT_OK ) {
             Uri selectedUri = data.getData();
             String[] projection = {MediaStore.Audio.Media.DATA};
             try{
                 Cursor cursor = getContentResolver().query(selectedUri, projection, null, null, null);
                 cursor.moveToFirst();
                 int columnIndex = cursor.getColumnIndex(projection[0]);
                 String picturePath = cursor.getString(columnIndex);
                 cursor.close();
                 Log.d("Picture Path", picturePath);
                 File f=new File(picturePath);
                 data.putExtra(MediaStore.EXTRA_MEDIA_ALBUM,"");
                 af = AudioFileIO.read(f);
                 Tag tag = af.getTag();
                 et0.setText(tag.getFirst(FieldKey.ALBUM));
                 et1.setText(tag.getFirst(FieldKey.ARTIST));
                 et2.setText(tag.getFirst(FieldKey.TITLE));
                 et3.setText(tag.getFirst(FieldKey.TRACK));
                 et4.setText(tag.getFirst(FieldKey.COMMENT));
                 et5.setText(tag.getFirst(FieldKey.GENRE));
                 et6.setText(tag.getFirst(FieldKey.MEDIA));
                 et7.setText(tag.getFirst(FieldKey.YEAR));
                 b2.setVisibility(View.VISIBLE);
                 b3.setVisibility(View.VISIBLE);
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (TagException e) {
                 e.printStackTrace();
             } catch (ReadOnlyFileException e) {
                 e.printStackTrace();
             } catch (InvalidAudioFrameException e) {
                 e.printStackTrace();
             } catch (CannotReadException e) {
                 e.printStackTrace();
             }
         }
        super.onActivityResult(reqCode,resCode,data);
     }
}