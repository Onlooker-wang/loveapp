package com.wy.lpr.expresslove.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.wy.lpr.expresslove.R;
import com.wy.lpr.expresslove.utils.textstyle.AlignSample;
import com.wy.lpr.expresslove.utils.textstyle.ColorSample;
import com.wy.lpr.expresslove.utils.textstyle.CookieThumperSample;
import com.wy.lpr.expresslove.utils.textstyle.Rotation3DSample;
import com.wy.lpr.expresslove.utils.textstyle.ScaleTextSample;
import com.wy.lpr.expresslove.utils.textstyle.ShapeRevealLoopSample;
import com.wy.lpr.expresslove.utils.textstyle.ShapeRevealSample;
import com.wy.lpr.expresslove.utils.textstyle.SlideSample;
import com.wy.lpr.expresslove.utils.textstyle.SurfaceScaleSample;
import com.wy.lpr.expresslove.utils.textstyle.SurfaceTransSample;

import su.levenetc.android.textsurface.Debug;
import su.levenetc.android.textsurface.TextSurface;

/**
 * Created by Eugene Levenetc.
 */
public class TextSampleActivity extends AppCompatActivity {

	private TextSurface textSurface;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_activity);
		textSurface = (TextSurface) findViewById(R.id.text_surface);

		textSurface.postDelayed(new Runnable() {
			@Override public void run() {
				show();
			}
		}, 1000);

		findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				show();
			}
		});

		CheckBox checkDebug = (CheckBox) findViewById(R.id.check_debug);
		checkDebug.setChecked(Debug.ENABLED);
		checkDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Debug.ENABLED = isChecked;
				textSurface.invalidate();
			}
		});
	}

	private void show() {
		textSurface.reset();
//		CookieThumperSample.play(textSurface, getAssets());
//		AlignSample.play(textSurface);
//		ColorSample.play(textSurface);
//		Rotation3DSample.play(textSurface);//渐渐消失又浮现
//		ScaleTextSample.run(textSurface);// 字体放大复原
//		ShapeRevealLoopSample.play(textSurface);
//		ShapeRevealSample.play(textSurface);
//		SlideSample.play(textSurface);
//		SurfaceScaleSample.play(textSurface);
//		SurfaceTransSample.play(textSurface);
	}

}