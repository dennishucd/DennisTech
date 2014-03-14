package cn.dennishucd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class AndroidCameraActivity extends Activity {
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;  
      
    private Intent intent  = null;  
    private Uri fileUri    = null;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
          
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//create a intent to record video  
        fileUri = getOutputMediaFileUri(); // create a file Uri to save the video
        
        // set the video file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);   
        
        // set the video quality high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); 

        // start the video capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);  
    }  
  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
          
        if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {  
            if (resultCode == RESULT_OK) {  
                // video captured and saved to fileUri specified in the Intent  
                Toast.makeText(this, "Video saved to:\n" +  
                         data.getData(),   
                         Toast.LENGTH_LONG).show();  
            } else if (resultCode == RESULT_CANCELED) {  
                // User cancelled the video capture  
            }  
        }  
    }  
      
    /** Create a File Uri for saving a video */  
    private static Uri getOutputMediaFileUri(){  
    	//get the mobile Pictures directory
    	File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //get the current time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
        
        File videoFile = new File(picDir.getPath() + File.separator + "VIDEO_"+ timeStamp + ".mp4");  
  
        return Uri.fromFile(videoFile);
    }  
}
