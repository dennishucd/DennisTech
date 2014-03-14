package cn.dennishucd;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class AndroidCameraAPIVideoActivity extends Activity implements OnClickListener {
	private final String      TAG = "Dennis";

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
    private Camera mCamera               = null;
    private MediaRecorder mMediaRecorder = null;
    private SurfacePreview mPreview     = null;
    
    private boolean isRecording   = false;
    private Button mCaptureButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Enter CameraAPIActivity.onCreate()...");
        
        setContentView(R.layout.main);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new SurfacePreview(this);
        
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
     // Add a listener to the Capture button
        mCaptureButton = (Button) findViewById(R.id.button_capture);
        mCaptureButton.setOnClickListener(this);
        
        initCamera();
    }
    
    public void initCamera() {
        getCameraInstance();
        mPreview.setCamera(mCamera);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		Log.d(TAG, "Enter CameraAPIActivity.onResume().......");
		
		initCamera();
	}

	@Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Enter CameraAPIActivity.onPause().......");
        
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }
	
	@Override
	public void onClick(View v) {
		if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            
            Log.d(TAG, "Enter onClick(), mMediaRecorder.stop() is executed........");
            
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            //setCaptureButtonText("Capture");
            isRecording = false;
        } else {
        	mCamera.unlock();
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                Log.d(TAG, "Enter onClick(), mMediaRecorder.start() is executed........");
                
                // inform the user that recording has started
                //setCaptureButtonText("Stop");
                isRecording = true;
            } else {
            	Log.e(TAG, "Enter onClick(), else is entered........");
            	
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
	}
    
    /** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance(){
	    try {
	    	if (mCamera == null) {
	    		mCamera = Camera.open(); // attempt to get a Camera instance
	    		
	    		Log.d(TAG, "Camera.open() is executed...");
	    	}
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	        // Camera is not available (in use or does not exist)
	    }
	    
	    return mCamera; // returns null if camera is unavailable
	}
	
	public boolean prepareVideoRecorder() {
		Log.d(TAG, "Enter CameraAPIActivity.prepareVideoRecorder()...");
		
		mMediaRecorder = new MediaRecorder();
		
		//1. setCamera
		//mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);
		
		//2. setAudioSource  
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		//3. setVideoSource
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		
		//4. set video output format
		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		
		//Prior to Android 2.2 (API Level 8)
//		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		
		//5. set the output file
		String path = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
		mMediaRecorder.setOutputFile(path);
		
		Log.d(TAG, path+" will be written.");
		
		//6. set the SurfaceView preview layout element
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
		
		
		//7. prepare the MediaRecorder
		 try {
			 mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			
			releaseMediaRecorder();
			
			return false;
		}
		 
		 return true;
	}
	
	 private void releaseCamera(){
		 if (mCamera != null){
	            mCamera.release();        // release the camera for other applications
	            
	            Log.d("Dennis", "mCamera.release() is executed.");
	            
	            mCamera = null;
		 }
	 }
	 
	private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
        }
    }
	
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "DennisVideo");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
}