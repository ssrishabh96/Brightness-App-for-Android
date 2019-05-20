package androbright.brightness.rishabh.androbright.customView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DummyView extends View {
    int alpha = 0;

    public DummyView(Context context) {
        super(context);
    }

    public DummyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DummyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public DummyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.argb(this.alpha, 0, 0, 0));
        canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), p);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidate();
    }
}
