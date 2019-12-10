package cn.com.startai.testaccessibilityservice;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.startai.common.utils.CShellUtils;
import cn.com.startai.common.utils.CThreadPoolUtils;
import cn.com.startai.common.utils.TAndL;
import cn.com.startai.common.utils.permission.CPermissionHelper;
import cn.com.startai.common.utils.permission.PermissionConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CThreadPoolUtils.getInstance().getWorkThreadPool().schedule(() -> CPermissionHelper.request(() -> TAndL.TL("onPermissionGranted"), () -> {
        }, PermissionConstants.STORAGE, PermissionConstants.LOCATION), 500, TimeUnit.MILLISECONDS);

        CThreadPoolUtils.getInstance().getWorkThreadPool().schedule(new Runnable() {
            @Override
            public void run() {
                CShellUtils.CommandResult commandResult = CShellUtils.execCmd("input tap 540 1884", false, true);
                System.out.println("commandResult = " + commandResult);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        int pointerCount = event.getPointerCount();
        List<Point> ps = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("点击了:");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0; i < pointerCount; i++) {
                    float x = event.getX(i);
                    float y = event.getY(i);
                    TAndL.L("action = " + action + " x = " + x + " y = " + y + " i = " + i);
                    ps.add(i, new Point(((int) x), ((int) y)));
                    sb.append("[" + (int) x + "," + (int) y + "]");
                }
                break;
        }

            TAndL.TL(sb.toString());
        return super.onTouchEvent(event);
    }

}
