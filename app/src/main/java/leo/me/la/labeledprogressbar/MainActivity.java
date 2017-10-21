package leo.me.la.labeledprogressbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

import leo.me.la.labeledprogressbarlib.LabeledProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LabeledProgressBar pb4 = (LabeledProgressBar) findViewById(R.id.pb4);
        final LabeledProgressBar pb1 = (LabeledProgressBar) findViewById(R.id.pb1);
        final LabeledProgressBar pb = (LabeledProgressBar) findViewById(R.id.pb);
        final LabeledProgressBar pb2 = (LabeledProgressBar) findViewById(R.id.pb2);
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb4.setIndeterminate(true);
                ha.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pb4.setIndeterminate(false);
                        pb4.animateToValue(100,2000);
                        pb4.setLabelBackgroundColor(Color.parseColor("#fc00bd"));
                        pb4.setTextColor(Color.parseColor("#ffffff"));
                        pb4.setProgressStartColor(Color.parseColor("#ffc4fb"));
                        pb4.setProgressCompleteColor(Color.parseColor("#fc00bd"));
                        pb4.setLabelValueType(LabeledProgressBar.LabelValueType.Percentage);
                    }
                },4000);
            }
        }, 4000);

        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb1.setValue(pb1.getValue()+(pb1.getMaxValue() - pb1.getMinValue()) / 10);
                if (pb1.getValue() < pb1.getMaxValue())
                    ha.postDelayed(this,1000);
            }
        }, 1000);
        final Random random = new Random();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.animateToValue(random.nextInt(pb.getMaxValue() - pb.getMinValue() + 1) + pb.getMinValue(), 1400);
                ha.postDelayed(this, 2500);
            }
        }, 2500);

        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb2.animateToValue(pb2.getValue()+(pb2.getMaxValue() - pb2.getMinValue()) / 10, 500);
                if (pb2.getValue() < pb2.getMaxValue())
                    ha.postDelayed(this,2000);
            }
        }, 1000);
    }
}
