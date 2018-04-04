package is.hi.hbv601.pubquiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * QrCode activity
 * Created by Fannar 1.4.2018
 */

public class QRCodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView tvQrCode;
    private BarcodeDetector barcodeDetector;
    private CameraSource appCamera;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String qrTeam = "";
    String teamName ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr );
        tvQrCode = findViewById(R.id.tvQrCode );
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {

        //Barcode detector set
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //camera set with barcode detector
        appCamera = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        //get team name if it has been registered
        teamName = getIntent().getStringExtra( "teamName" );

        //accessing phone permissions for app
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        appCamera.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRCodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                appCamera.stop();
            }
        });

        //run Barcode detector.
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            //Scan for barcode while camera is on
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode.size() != 0) {

                    tvQrCode.post( new Runnable() {
        
                        //getting scanned QrCode and display coded string, and sending over to RegisterTeamActivity
                        @Override
                        public void run() {
                            qrTeam = qrCode.valueAt(0).displayValue;
                            tvQrCode.setText(qrTeam);
                            Intent intent = new Intent( QRCodeActivity.this, RegisterTeamActivity.class );
                            intent.putExtra( "roomName", qrTeam );
                            intent.putExtra( "teamName", teamName  );
                            startActivity( intent );
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        appCamera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
