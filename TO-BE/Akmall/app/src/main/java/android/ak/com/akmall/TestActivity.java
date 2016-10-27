package android.ak.com.akmall;

import android.app.Activity;
import android.widget.MediaController;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_test)
public class TestActivity extends Activity {

    @ViewById
    VideoView TEST_VIDEO;

    @AfterViews
    private void afterView(){
        TEST_VIDEO.setVideoPath("url");
        final MediaController mediaController =
                new MediaController(this);
        TEST_VIDEO.setMediaController(mediaController);
    }
}
