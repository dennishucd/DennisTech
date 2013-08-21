package cn.dennishucd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AndroidCameraActivity extends Activity implements OnClickListener, PictureCallback {
    private CameraSurfacePreview mCameraSurPreview = null;
    private Button mCaptureButton = null;
    private String TAG = "Dennis";
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        
        // Create our Preview view and set it as the content of our activity.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraSurPreview = new CameraSurfacePreview(this);
        preview.addView(mCameraSurPreview);  
        
     // Add a listener to the Capture button
        mCaptureButton = (Button) findViewById(R.id.button_capture);
        mCaptureButton.setOnClickListener(this);
    }
    
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    	//save the picture to sdcard
    	File pictureFile = getOutputMediaFile();
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            
            Toast.makeText(this, "Image has been saved to "+pictureFile.getAbsolutePath(), 
            		Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
        	Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        
        // Restart the preview and re-enable the shutter button so that we can take another picture
 		camera.startPreview();
 		
 		//See if need to enable or not
 		mCaptureButton.setEnabled(true);
    }
    
    @Override
    public void onClick(View v) {
    	mCaptureButton.setEnabled(false);
    	
        // get an image from the camera
    	mCameraSurPreview.takePicture(this);
    }
    
    private File getOutputMediaFile(){  
    	//get the mobile Pictures directory  
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);  
  
        //get the current time  
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());   
          
        return new File(picDir.getPath() + File.separator + "IMAGE_"+ timeStamp + ".jpg");    
    }
}
