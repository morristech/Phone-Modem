package edu.berkeley.cs194.activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.berkeley.cs194.R;
import edu.berkeley.cs194.audio.PitchDetector.FrequencyReceiver;
import edu.berkeley.cs194.audio.PitchDetector;
import edu.berkeley.cs194.audio.SoundPlayer;
import edu.berkeley.cs194.util.Utils;

public class AudioModemActivity extends Activity implements FrequencyReceiver {
	PitchDetector detector;
	SoundPlayer player;
	TextView status;
	EditText frequency, message;
	Button play, stop, test, high, low, send;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		status = (TextView) findViewById(R.id.status);
		frequency = (EditText) findViewById(R.id.frequency);
		play = (Button) findViewById(R.id.play);
		stop = (Button) findViewById(R.id.stop);
		test = (Button) findViewById(R.id.test);
		high = (Button) findViewById(R.id.high);
		low = (Button) findViewById(R.id.low);
		message = (EditText) findViewById(R.id.message);
		send = (Button) findViewById(R.id.send);

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int f = Integer.parseInt(frequency.getText().toString());
				player.end();

				player = new SoundPlayer();
				player.output(new int[] { f });
			}
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player.end();
			}
		});

		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player.end();

				player = new SoundPlayer();
				player.output(new int[] { SoundPlayer.HIGH, SoundPlayer.LOW,
						SoundPlayer.HIGH, SoundPlayer.LOW });
			}
		});

		high.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player.end();

				player = new SoundPlayer();
				player.output(new int[] { SoundPlayer.HIGH });
			}
		});

		low.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player.end();

				player = new SoundPlayer();
				player.output(new int[] { SoundPlayer.LOW });
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				player.end();

				player = new SoundPlayer();
				int[] frequencies = Utils.textToMorse(message.getText()
						.toString());
				Log.d("AudioModemActivity", frequencies.toString());
				player.output(frequencies);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		detector = new PitchDetector(this);
		player = new SoundPlayer();
	}

	@Override
	protected void onPause() {
		super.onPause();

		detector.interrupt();
		player.end();
	}

	@Override
	public void updateFrequency(final double frequency, final double amplitude,
			final HashMap<Double, Double> frequencies) {

		if (amplitude > 500) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// int binary = frequency > 1000 ? 1 : 0;
					// status.setText(String.format("%d (f:%d : a:%d)", binary,
					// frequency, amplitude));
					status.setText(String.format("(f:%f : a:%f)", frequency,
							amplitude));
				}

			});
		}
	}
}