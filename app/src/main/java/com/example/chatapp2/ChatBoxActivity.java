package com.example.chatapp2;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp2.classes.VolleyMultipartRequest;
import com.example.chatapp2.classes.dataReceiverChatBox;
import com.example.chatapp2.classes.imageUtilities;
import com.example.chatapp2.classes.messages;
import com.example.chatapp2.fragments.views.AutoFitTextureView;
import com.example.chatapp2.fragments.views.cameraButton;
import com.example.chatapp2.fragments.views.flashview;
import com.example.chatapp2.fragments.views.flipView;
import com.example.chatapp2.views.CustomEditText;
import com.example.chatapp2.views.chatBar;
import com.example.chatapp2.views.chatBox;
import com.example.chatapp2.views.imageDisplayer;
import com.example.chatapp2.views.messagesenderButton;
import com.example.chatapp2.views.otherFeaturesView;
import com.example.chatapp2.views.videoDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ChatBoxActivity extends AppCompatActivity implements Visualizer.OnDataCaptureListener {

    public static int stateOfActivity; // 0: chatBox; 1: camera; 2: picturePreview; 3: videoPreview
    int screenHeight;
    int screenWidth;
    boolean imeVisible;
    int imeHeight;
    boolean firstLayout;
    Activity activityContext;
    String conversation;

    ConstraintLayout constraintlayout;
    chatBox chatbox;
    chatBar chatbar;
    CustomEditText customEditText;
    messagesenderButton messagesenderButton;
    cameraButton camerabutton;
    Switch switchButton;
    videoDisplayer videodisplayer;
    boolean PicOrVideo;
    boolean isFilming;
    private Toolbar toolbar;
    flipView flipView;
    flashview flashview;
    imageDisplayer imageDisplayer;
    int statusBarHeight;
    int keyBoardHeight;
    Bitmap deleteIcon;

    dataReceiverChatBox datareceiver;
    SQLiteDatabase mydatabase;

    private static final String LOG_TAG = "ChatBoxActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    public MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private Visualizer visualiser;
    private otherFeaturesView otherFeaturesView;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    MediaRecorder mMediaRecorder;
    Button mButtonVideo;
    boolean mIsRecordingVideo;
    private Size mVideoSize;
    private CameraService [] cameraServiceList;
    private int openedCamera;
    private AutoFitTextureView mTextureView;
    private Size mPreviewSize;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private File mFile;
    private int mSensorOrientation;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private int mState = STATE_PREVIEW;
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private boolean mFlashSupported;
    private int flashMode;
    String shortVideoFilePath;
    int lll;
    boolean isCameraActive;
    ArrayList<Integer> listToCharge = new ArrayList<>();
    messages msg2add;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        activityContext = this;
        firstLayout = true;
        isCameraActive = false;
        openedCamera = 0;
        flashMode = 0;

        Intent intent = getIntent();
        conversation = intent.getStringExtra("UserName");

        constraintlayout = findViewById(R.id.constraintlayout);
        chatbox = findViewById(R.id.chatBox);
        chatbar = findViewById(R.id.chatBar);
        customEditText = findViewById(R.id.customEditText2);
        messagesenderButton = findViewById(R.id.messagesenderButton);
        otherFeaturesView = findViewById(R.id.otherFeaturesView);
        mTextureView = findViewById(R.id.autoFitTextureView);
        camerabutton = findViewById(R.id.cameraButton);
        switchButton = findViewById(R.id.switch1);
        videodisplayer = findViewById(R.id.videoDisplayer);
        imageDisplayer = findViewById(R.id.imageDisplayer);
        flipView = findViewById(R.id.flipView);
        flashview = findViewById(R.id.flashview);
        toolbar = findViewById(R.id.toolbar);

        deleteIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_small);

        messagesenderButton.setMessagesenderButtonInterface(new messagesenderButton.messagesenderButtonInterface() {
            @Override
            public void onMicroDown() {
                startRecording();
            }

            @Override
            public void stopRecording() {
                if (recorder != null) {
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                    uploadVideoFile(filename, shortVideoFilePath, "http://192.168.1.50:6661/api/v22/users/upload", ".3gp", 1);
                }
            }

            @Override
            public float sendAmplitude() {
                return recorder != null ? recorder.getMaxAmplitude() : 25;
            }

            @Override
            public Bitmap saveBitmap() {
                byte[] imageData = Arrays.copyOf(byteArrayImage, byteArrayImage.length);
                Bitmap bitmap10 = imageUtilities.decodeSampledBitmapFromByteArr(imageData, chatbox.getHeight() / 10, chatbox.getHeight() / 10);
                bitmap10 = Bitmap.createScaledBitmap(bitmap10, chatbox.getWidth() * 2 / 5 - 45, chatbox.getHeight() / 4 - 5, false);
                if (cameraServiceList[openedCamera].frontOrBackCamera) {
                    bitmap10 = createFlippedBitmap(bitmap10, true, false);
                }
                return bitmap10;
            }

            @Override
            public boolean saveBitmapToFile() throws IOException, JSONException {
                Bitmap bitmap98 = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length, null);
                if (cameraServiceList[openedCamera].frontOrBackCamera) {
                    bitmap98 = createFlippedBitmap(bitmap98, true, false);
                }
                postBitmap(bitmap98, mFile);
                uploadBitmap(bitmap98, "http://192.168.1.50:6661/api/v22/users/upload", shortVideoFilePath);
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public Bitmap saveVideoBitmap() throws IOException {
                Size previewSize = new Size((int) (chatbox.getWidth() * 2 / 5.0 - 45), (int) (chatbox.getHeight() / 4.0 - 5));
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(new File(filename), previewSize, null);
                uploadVideoFile(filename, shortVideoFilePath, "http://192.168.1.50:6661/api/v22/users/upload", ".mp4", 3);
                return thumb;
            }

            @Override
            public void launchRunnable(messages msg) {
                mTextureView.setVisibility(View.GONE);
                videodisplayer.setVisibility(View.GONE);
                chatbar.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                chatbox.setVisibility(View.VISIBLE);
                chatbox.viewSizeWithKeyboard = (int) (toolbar.getY() + toolbar.getHeight());
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(chatbox.getWidth(), chatbox.getHeight());
                chatbox.setLayoutParams(params);
                mBackgroundHandler.post(msg.mFileAndIconLoader);
                otherFeaturesView.setVisibility(View.VISIBLE);
                stateOfActivity = 0;
                customEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(customEditText.scaleEmojiImage, null, customEditText.otherFeatures, null);
                customEditText.invalidate();
            }
        });

        chatbox.setImplementChatbox(new chatBox.implementChatbox() {
            @Override
            public void fetchSqlMsg() {
                chatbox.messagesList = fetchSqlMSG(getApplicationContext().getResources().getDisplayMetrics().widthPixels * 2 / 3);
                Size previewSize = new Size((int) (chatbox.getWidth() * 2 / 5.0 - 45), (int) (chatbox.getHeight() / 4.0 - 5));
                for (int j = 0; j < listToCharge.size(); j++) {
                    int i = listToCharge.get(j);
                    if (chatbox.messagesList.get(i).msgType == 3) {
                        int finalI = i;
                        chatbox.messagesList.get(i).setmFileAndIconLoader(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            @Override
                            public void run() {
                                try {
                                    chatbox.messagesList.get(finalI).iconBitmap = ThumbnailUtils.createVideoThumbnail(new File(chatbox.messagesList.get(finalI).filename), previewSize, null);
                                    chatbox.messagesList.get(finalI).isLoading = false;
                                } catch (IOException e) {
                                    Log.e(LOG_TAG, "Error loading video thumbnail", e);
                                }
                                chatbox.invalidate();
                            }
                        });
                        chatbox.messagesList.get(i).isLoading = true;
                        mBackgroundHandler.post(chatbox.messagesList.get(i).mFileAndIconLoader);
                    } else {
                        int finalI1 = i;
                        chatbox.messagesList.get(i).setmFileAndIconLoader(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bmp = BitmapFactory.decodeFile(chatbox.messagesList.get(finalI1).filename);
                                if (bmp != null) {
                                    bmp = Bitmap.createScaledBitmap(bmp, (int) (chatbox.getWidth() * 2 / 5.0 - 45), (int) (chatbox.getHeight() / 4.0 - 5), false);
                                    chatbox.messagesList.get(finalI1).iconBitmap = bmp;
                                }
                                chatbox.invalidate();
                            }
                        });
                        mBackgroundHandler.post(chatbox.messagesList.get(i).mFileAndIconLoader);
                    }
                }
            }

            @Override
            public void chargeSqlMsg() {
                chatbox.messagesListToLoad = chargeSQLMSG((int) chatbox.messagesList.get(0).YT, getApplicationContext().getResources().getDisplayMetrics().widthPixels * 2 / 3);
            }

            @Override
            public void getScreenSize() {
                chatbox.trueHeigth = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
                chatbox.trueWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            }

            @Override
            public void startPlaying(messages message) {
                try {
                    player = new MediaPlayer();
                    player.setDataSource(message.filename);
                    player.prepare();
                    player.start();
                    if (message.vocalDuration == -1) {
                        message.setVocalDuration(player.getDuration());
                    }
                    startVisualiser(player);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error starting player", e);
                }
            }

            @Override
            public void stopPlaying(messages message) {
                if (visualiser != null) {
                    visualiser.setEnabled(false);
                    visualiser.release();
                    visualiser.setDataCaptureListener(null, 0, false, false);
                    visualiser = null;
                }
                if (player != null) {
                    player.reset();
                    player.release();
                    player = null;
                }
                chatbox.invalidate();
            }

            @Override
            public boolean onStopPlaying(messages message) {
                boolean isPlaying = true;
                if (player != null && !player.isPlaying()) {
                    stopPlaying(message);
                    isPlaying = false;
                    message.isPlaying = false;
                }
                return isPlaying;
            }

            @Override
            public int getVocalCurrentPosition() {
                return player != null ? player.getCurrentPosition() : 0;
            }

            @Override
            public void launchImageDisplayer(int msgClicked33) {
                imageDisplayer.setBitmap(chatbox.messagesList.get(msgClicked33).filename, screenWidth);
                chatbox.setVisibility(View.GONE);
                messagesenderButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                chatbar.setVisibility(View.GONE);
                customEditText.setVisibility(View.GONE);
                imageDisplayer.setVisibility(View.VISIBLE);
            }

            @Override
            public void launchVideoDisplayer(int msgClicked34) {
                videodisplayer.setVideoPath(chatbox.messagesList.get(msgClicked34).filename);
                chatbox.setVisibility(View.GONE);
                messagesenderButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                chatbar.setVisibility(View.GONE);
                customEditText.setVisibility(View.GONE);
                videodisplayer.setVisibility(View.VISIBLE);
                videodisplayer.start();
            }
        });

        otherFeaturesView.setImplementOtherFeaturesView(new otherFeaturesView.implementOtherFeaturesView() {
            @Override
            public void getScreenSize() {
                otherFeaturesView.screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
                otherFeaturesView.screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            }

            @Override
            public void onCameraClick() {
                chatbox.setVisibility(View.GONE);
                messagesenderButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                chatbar.setVisibility(View.GONE);
                customEditText.setVisibility(View.GONE);
                otherFeaturesView.setVisibility(View.GONE);
                mTextureView.setVisibility(View.VISIBLE);
                flashview.setVisibility(View.VISIBLE);
                flipView.setVisibility(View.VISIBLE);
                camerabutton.setVisibility(View.VISIBLE);
                stateOfActivity = 1;
                isCameraActive = true;
                startBackgroundThread();
                if (mTextureView.isAvailable()) {
                    try {
                        openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                }
            }
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        chatbar.setChatBarInterface(new chatBar.chatBarInterface() {
            @Override
            public void getScreenSize() {
                chatbar.trueHeigth = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
                chatbar.trueWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            }
        });

        camerabutton.setCameraButtonInterface(new cameraButton.cameraButtonInterface() {
            @Override
            public void onClick1() {
                if (!switchButton.isChecked()) {
                    shortVideoFilePath = "CHA-" + System.nanoTime();
                    filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + shortVideoFilePath + ".jpg";
                    mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), shortVideoFilePath + ".jpg");
                    takePicture();
                    camerabutton.setVisibility(View.GONE);
                    flashview.setVisibility(View.GONE);
                    flipView.setVisibility(View.GONE);
                    customEditText.setVisibility(View.VISIBLE);
                    messagesenderButton.setVisibility(View.VISIBLE);
                    stateOfActivity = 2;
                } else {
                    if (!mIsRecordingVideo) {
                        stateOfActivity = 3;
                        startRecordingVideo();
                    } else {
                        stopRecordingVideo();
                        videodisplayer.setVideoPath(filename);
                        mTextureView.setVisibility(View.GONE);
                        camerabutton.setVisibility(View.GONE);
                        flashview.setVisibility(View.GONE);
                        flipView.setVisibility(View.GONE);
                        customEditText.setVisibility(View.VISIBLE);
                        messagesenderButton.setVisibility(View.VISIBLE);
                        videodisplayer.setWWWHHH(mTextureView.getWidth(), mTextureView.getHeight());
                        videodisplayer.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void getMeasurements() {
                camerabutton.trueWidth = screenWidth;
                camerabutton.trueHeigth = screenHeight;
            }
        });

        flashview.setFlashInterface(new flashview.flashinterface() {
            @Override
            public void onClick1() {
                flashMode = (flashMode + 1) % 3;
            }
        });

        flipView.setFlipInterface(new flipView.flipInterface() {
            @Override
            public void onClick1() {
                closeCamera();
                openedCamera = (openedCamera + 1) % cameraServiceList.length;
                try {
                    openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraServiceList[openedCamera].CameraIDD);
                    mTextureView.cameraZoomRect1 = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        keyBoardHeight = 0;
        ViewCompat.setWindowInsetsAnimationCallback(customEditText, new WindowInsetsAnimationCompat.Callback(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP) {
            @Override
            public void onPrepare(@NonNull WindowInsetsAnimationCompat animation) {
                chatbox.iskeyboardToUp = !chatbox.iskeyboardToUp;
                if (!chatbox.iskeyboardToUp) {
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(chatbox.getWidth(), (int) (chatbox.trueHeigth * 11 / 13 - keyBoardHeight));
                    chatbox.oldHeight = (int) (chatbox.trueHeigth * 11 / 13 - keyBoardHeight + customEditText.getHeight());
                    chatbox.setLayoutParams(params);
                }
                super.onPrepare(animation);
            }

            @Override
            public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
                super.onEnd(animation);
                chatbox.viewSizeWithKeyboard = (int) (toolbar.getY() + toolbar.getHeight());
                chatbox.isKeyboardHere = !chatbox.isKeyboardHere;
            }

            @NonNull
            @Override
            public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                WindowInsetsAnimationCompat imeAnimation = null;
                for (WindowInsetsAnimationCompat animation : runningAnimations) {
                    if ((animation.getTypeMask() & WindowInsetsCompat.Type.ime()) != 0) {
                        imeAnimation = animation;
                        break;
                    }
                }
                if (imeAnimation != null) {
                    chatbox.firstmeasure = false;
                    float fraction = imeAnimation.getInterpolatedFraction();
                    if (imeHeight > 0) {
                        chatbox.viewSizeWithKeyboard = (int) (toolbar.getY() + toolbar.getHeight());
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(chatbox.getWidth(), (int) (chatbox.trueHeigth * 11 / 13 - imeHeight * fraction) + customEditText.getHeight());
                        chatbox.setLayoutParams(params);
                        customEditText.setTranslationY((-fraction) * (imeHeight - customEditText.getHeight()));
                        otherFeaturesView.setTranslationY((-fraction) * (imeHeight - customEditText.getHeight()));
                        messagesenderButton.setTranslationY((-fraction) * (imeHeight - customEditText.getHeight()));
                        keyBoardHeight = imeHeight;
                    } else {
                        float invFraction = 1 - fraction;
                        if (invFraction != 1.0) {
                            chatbox.viewSizeWithKeyboard = (int) (toolbar.getY() + toolbar.getHeight());
                            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(chatbox.getWidth(), (int) (chatbox.trueHeigth * 11 / 13 - (keyBoardHeight - customEditText.getHeight()) * invFraction));
                            chatbox.setLayoutParams(params);
                            customEditText.setTranslationY(-invFraction * (keyBoardHeight - customEditText.getHeight()));
                            otherFeaturesView.setTranslationY(-invFraction * (keyBoardHeight - customEditText.getHeight()));
                            messagesenderButton.setTranslationY(-invFraction * (keyBoardHeight - customEditText.getHeight()));
                        }
                    }
                }
                return insets;
            }
        });

        mydatabase = openOrCreateDatabase("Chat 1", MODE_PRIVATE, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (datareceiver == null) datareceiver = new dataReceiverChatBox();
        datareceiver.chatFinder(chatbox, conversation);
        registerReceiver(datareceiver, new IntentFilter("Dataxxx"));
    }

    @Override
    protected void onPause() {
        if (datareceiver != null) unregisterReceiver(datareceiver);
        if (isCameraActive) {
            closeCamera();
            stopBackgroundThread();
        }
        super.onPause();
    }

    private void postDataUsingVolley(int typeOfMessage, int urlCode, String Base64encoded) throws JSONException {
        String url = "http://192.168.1.50:6661/api/v22/users";
        RequestQueue queue = Volley.newRequestQueue(this);
        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("usernameSender", "user2");
        jsonBody.put("usernameReceiver", "user2");
        jsonBody.put("base64FileString", Base64encoded);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {}, error -> Log.e(LOG_TAG, "Volley error", error));
        request.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void uploadVideoFile(String filePath, String shortPath, String url, String extension, int msgType) {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, response -> {
            String responseId = new String(response.data);
            Intent msgIntent = new Intent("msgintent");
            msgIntent.putExtra("username", "user2@localhost");
            msgIntent.putExtra("body", "New message");
            msgIntent.putExtra("msgType", msgType + "/" + responseId);
            msgIntent.putExtra("filepath", shortPath);
            sendBroadcast(msgIntent);
        }, error -> Log.e(LOG_TAG, "Upload error", error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userNameReceiver", "user2");
                params.put("userNameSender", "user2");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("pic", new DataPart(System.currentTimeMillis() + extension, fileToByteArray(filePath)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void uploadBitmap(final Bitmap bitmap, String url, String shortFileName) {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, response -> {
            Intent msgIntent = new Intent("msgintent");
            msgIntent.putExtra("username", "user2@localhost");
            msgIntent.putExtra("body", "New message");
            msgIntent.putExtra("msgType", "2/" + new String(response.data));
            msgIntent.putExtra("filepath", shortFileName);
            sendBroadcast(msgIntent);
        }, error -> Log.e(LOG_TAG, "Upload error", error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userNameReceiver", "user2");
                params.put("userNameSender", "user2");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("pic", new DataPart(System.currentTimeMillis() + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] fileToByteArray(String path) {
        byte[] bytes = new byte[0];
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bytes = Files.readAllBytes(Paths.get(path));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error reading file to byte array", e);
        }
        return bytes;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) finish();
    }

    public ArrayList<messages> fetchSqlMSG(int textWidth) {
        startBackgroundThread();
        Cursor resultSet = mydatabase.rawQuery("Select MAX(primaryKey) from " + conversation, null);
        resultSet.moveToFirst();
        String maxKey = resultSet.getString(0);
        ArrayList<messages> MSG = new ArrayList<>();
        if (maxKey != null) {
            int lastKey = resultSet.getInt(0) - 15;
            resultSet = mydatabase.rawQuery("Select * from " + conversation + " where primaryKey >" + lastKey + " order by primaryKey ASC", null);
            int i = 0;
            while (resultSet.moveToPosition(i)) {
                int sender = resultSet.getString(0).equals("Constantine") ? 0 : 1;
                int type = resultSet.getInt(3);
                String path = resultSet.getString(4);
                switch (type) {
                    case 0:
                        msg2add = new messages(resultSet.getString(1), sender, new Paint(), new Paint(), textWidth);
                        msg2add.setYT(MSG.isEmpty() ? 50 : MSG.get(MSG.size() - 1).YB + 50);
                        msg2add.setYB(55 + 55 * msg2add.displayText.size() + msg2add.YT);
                        break;
                    case 1:
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + path + ".3gp";
                        msg2add = new messages(sender, new Paint(), path);
                        msg2add.setPlaying(false);
                        msg2add.setYT(MSG.isEmpty() ? 50 : MSG.get(MSG.size() - 1).YB + 50);
                        msg2add.setYB(55 + chatbox.getWidth() / 8 + msg2add.YT);
                        break;
                    case 2:
                        listToCharge.add(i);
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + path + ".jpg";
                        msg2add = new messages(sender, new Paint(), "Image message", deleteIcon);
                        msg2add.setFilename(path);
                        msg2add.setYT(MSG.isEmpty() ? 50 : MSG.get(MSG.size() - 1).YB + 50);
                        msg2add.setYB(55 + chatbox.getHeight() / 4 + msg2add.YT);
                        break;
                    case 3:
                        listToCharge.add(i);
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + path + ".mp4";
                        msg2add = new messages(sender, new Paint(), "Video message", deleteIcon);
                        msg2add.setMsgType(3);
                        msg2add.setFilename(path);
                        msg2add.setYT(MSG.isEmpty() ? 50 : MSG.get(MSG.size() - 1).YB + 50);
                        msg2add.setYB(55 + chatbox.getHeight() / 4 + msg2add.YT);
                        break;
                }
                msg2add.setMsgType(type);
                if (type != 0) msg2add.setFilename(path);
                chatbox.contentTopYOffset += msg2add.YT - msg2add.YB - 35;
                MSG.add(msg2add);
                i++;
            }
        }
        return MSG;
    }

    public ArrayList<messages> chargeSQLMSG(int yt, int textWidth) {
        ArrayList<messages> MSG = new ArrayList<>();
        Cursor rs1 = mydatabase.rawQuery("Select MAX(primaryKey) from " + conversation, null);
        rs1.moveToFirst();
        int first = rs1.getInt(0) - chatbox.messagesList.size();
        int last = first - 15;
        Cursor rs = mydatabase.rawQuery("Select * from " + conversation + " where primaryKey <" + first + " and primaryKey>" + last + " order by primaryKey ASC", null);
        int i = 0;
        while (rs.moveToPosition(i)) {
            int sender = rs.getString(0).equals("Constantine") ? 0 : 1;
            messages m = new messages(rs.getString(1), sender, new Paint(), new Paint(), textWidth);
            m.setYT(MSG.isEmpty() ? yt + 50 : MSG.get(MSG.size() - 1).YB + 50);
            m.setYB(55 + 55 * m.displayText.size() + m.YT);
            MSG.add(m);
            i++;
        }
        return MSG;
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
        if (chatbox != null) chatbox.setWaveform(bytes);
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {}

    private void startVisualiser(MediaPlayer mp) {
        visualiser = new Visualizer(mp.getAudioSessionId());
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualiser.setCaptureSize(256);
        visualiser.setEnabled(true);
    }

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openedCamera = 0;
            flashMode = 0;
            try {
                openCamera(width, height);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {}
    };

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cd) {
            mCameraOpenCloseLock.release();
            cameraServiceList[openedCamera].mCameraDevice = cd;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cd) {
            mCameraOpenCloseLock.release();
            cd.close();
            cameraServiceList[openedCamera].mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cd, int error) {
            mCameraOpenCloseLock.release();
            cd.close();
            cameraServiceList[openedCamera].mCameraDevice = null;
            finish();
        }
    };

    private byte[] byteArrayImage;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = reader -> {
        Image image = reader.acquireNextImage();
        if (image != null) {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byteArrayImage = new byte[buffer.capacity()];
            buffer.get(byteArrayImage);
            image.close();
        }
    };

    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_WAITING_LOCK:
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null || afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                case STATE_WAITING_PRECAPTURE:
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureResult.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                case STATE_WAITING_NON_PRECAPTURE:
                    aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
            }
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            process(result);
        }
    };

    private Rect cropRect;
    private void openCamera(int width, int height) throws CameraAccessException {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return;
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) throw new RuntimeException("Camera lock timeout");
            manager.openCamera(cameraServiceList[openedCamera].CameraIDD, mStateCallback, mBackgroundHandler);
        } catch (InterruptedException e) {
            throw new RuntimeException("Camera access interrupted", e);
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (cameraServiceList[openedCamera].captureSession != null) {
                cameraServiceList[openedCamera].captureSession.close();
                cameraServiceList[openedCamera].captureSession = null;
            }
            if (cameraServiceList[openedCamera].mCameraDevice != null) {
                cameraServiceList[openedCamera].mCameraDevice.close();
                cameraServiceList[openedCamera].mCameraDevice = null;
            }
            if (mImageReader != null) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Camera close interrupted", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        mIsRecordingVideo = false;
        try {
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(texture);
            mPreviewRequestBuilder = cameraServiceList[openedCamera].mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mPreviewRequestBuilder.addTarget(surface);
            Surface recorderSurface = mMediaRecorder.getSurface();
            mPreviewRequestBuilder.addTarget(recorderSurface);
            cameraServiceList[openedCamera].mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface(), mMediaRecorder.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraServiceList[openedCamera].mCameraDevice == null) return;
                    cameraServiceList[openedCamera].captureSession = session;
                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        setAutoFlash(mPreviewRequestBuilder);
                        mPreviewRequest = mPreviewRequestBuilder.build();
                        cameraServiceList[openedCamera].captureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
            }, null);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (mTextureView == null || mPreviewSize == null) return;
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / mPreviewSize.getHeight(), (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void takePicture() {
        lockFocus();
    }

    private void lockFocus() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mState = STATE_WAITING_LOCK;
            cameraServiceList[openedCamera].captureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void runPrecaptureSequence() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            mState = STATE_WAITING_PRECAPTURE;
            cameraServiceList[openedCamera].captureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void captureStillPicture() {
        try {
            if (cameraServiceList[openedCamera].mCameraDevice == null) return;
            final CaptureRequest.Builder cb = cameraServiceList[openedCamera].mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            cb.addTarget(mImageReader.getSurface());
            cb.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            cb.set(CaptureRequest.SCALER_CROP_REGION, cropRect);
            setAutoFlash(cb);
            cb.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(getWindowManager().getDefaultDisplay().getRotation()));
            cameraServiceList[openedCamera].captureSession.stopRepeating();
            cameraServiceList[openedCamera].captureSession.abortCaptures();
            cameraServiceList[openedCamera].captureSession.capture(cb.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    runOnUiThread(() -> Toast.makeText(ChatBoxActivity.this, "Saved: " + mFile, Toast.LENGTH_SHORT).show());
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + cameraServiceList[openedCamera].sensorOrientation + 270) % 360;
    }

    private void setAutoFlash(CaptureRequest.Builder builder) {
        if (cameraServiceList[openedCamera].flashSupported) {
            switch (flashMode) {
                case 1: builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH); break;
                case 2: builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH); break;
            }
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/audio.3gp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Recorder prepare failed", e);
        }
    }

    public class CameraService {
        String CameraIDD;
        CameraDevice mCameraDevice;
        boolean frontOrBackCamera;
        boolean flashSupported;
        CameraCaptureSession captureSession;
        int sensorOrientation;

        public CameraService(String id, boolean front) {
            this.CameraIDD = id;
            this.frontOrBackCamera = front;
        }

        public void setSensorOrientation(int so) { this.sensorOrientation = so; }
        public void setFlashSupported(boolean fs) { this.flashSupported = fs; }
    }

    private void setUpMediaRecorder() throws IOException {
        shortVideoFilePath = "CHA-" + System.nanoTime();
        filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/" + shortVideoFilePath + ".mp4";
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(filename);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        mMediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation) + (cameraServiceList[openedCamera].frontOrBackCamera ? 180 : 0));
        mMediaRecorder.prepare();
    }

    private void startRecordingVideo() {
        try {
            mIsRecordingVideo = true;
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "Recording video start failed", e);
        }
    }

    private void stopRecordingVideo() {
        mIsRecordingVideo = false;
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Toast.makeText(this, "Video saved: " + filename, Toast.LENGTH_SHORT).show();
    }

    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) return size;
        }
        return choices[choices.length - 1];
    }

    private void setUpCameraOutputs(int width, int height) throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraServiceList = new CameraService[manager.getCameraIdList().length];
        mTextureView.setGetZoomCaracteristics(new AutoFitTextureView.getZoomCaracteristics() {
            @Override
            public Rect giveRectZoom() throws CameraAccessException {
                return manager.getCameraCharacteristics(cameraServiceList[openedCamera].CameraIDD).get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            }

            @Override
            public float giveMaxZoom() throws CameraAccessException {
                return manager.getCameraCharacteristics(cameraServiceList[openedCamera].CameraIDD).get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) * 10;
            }

            @Override
            public void previewRequestINT(Rect rect) {
                mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, rect);
                cropRect = rect;
            }

            @Override
            public void captureSession() throws CameraAccessException {
                cameraServiceList[openedCamera].captureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
            }
        });
        for (String id : manager.getCameraIdList()) {
            CameraCharacteristics c = manager.getCameraCharacteristics(id);
            Integer facing = c.get(CameraCharacteristics.LENS_FACING);
            cameraServiceList[Integer.parseInt(id)] = new CameraService(id, facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT);
            cameraServiceList[Integer.parseInt(id)].setSensorOrientation(c.get(CameraCharacteristics.SENSOR_ORIENTATION));
            cameraServiceList[Integer.parseInt(id)].setFlashSupported(c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) != null);
            StreamConfigurationMap map = c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map != null) {
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
                mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
                mMediaRecorder = new MediaRecorder();
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT, largest);
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int tw, int th, int mw, int mh, Size aspect) {
        List<Size> big = new ArrayList<>(), small = new ArrayList<>();
        int w = aspect.getWidth(), h = aspect.getHeight();
        for (Size s : choices) {
            if (s.getWidth() <= mw && s.getHeight() <= mh && s.getHeight() == s.getWidth() * h / w) {
                if (s.getWidth() >= tw && s.getHeight() >= th) big.add(s);
                else small.add(s);
            }
        }
        if (!big.isEmpty()) return Collections.min(big, new CompareSizesByArea());
        if (!small.isEmpty()) return Collections.max(small, new CompareSizesByArea());
        return choices[0];
    }

    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    private Bitmap createFlippedBitmap(Bitmap src, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.postScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private void postBitmap(Bitmap bmp, File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
    }
}
