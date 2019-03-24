package customfonts;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by one on 3/12/15.
 */
@SuppressLint("AppCompatCustomView")
public class MyHelvaticaAutoComplete extends AutoCompleteTextView {
    public MyHelvaticaAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyHelvaticaAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHelvaticaAutoComplete(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            setTypeface(tf);
        }
    }

}