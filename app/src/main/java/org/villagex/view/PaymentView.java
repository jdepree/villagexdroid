package org.villagex.view;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stripe.android.view.CardInputWidget;

import org.villagex.R;

public class PaymentView extends LinearLayout {
    private SeekBar mDonationAmountBar;
    private TextView mDonationAmountText;
    private CardInputWidget mCardWidget;
    private CheckBox mAnonymousCheck;
    private TextView mFullNameText;
    private TextView mEmailText;


    public PaymentView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setPadding(20, 20, 20, 20);
        inflate(getContext(), R.layout.payment_layout, this);

        mAnonymousCheck = (CheckBox) findViewById(R.id.payment_anonymous);
        mFullNameText = (TextView) findViewById(R.id.payment_full_name);
        mEmailText = (TextView) findViewById(R.id.payment_email);
        mDonationAmountBar = (SeekBar)findViewById(R.id.payment_amount_slider);
        mDonationAmountText = (TextView)findViewById(R.id.payment_amount_text);
        mCardWidget = (CardInputWidget)findViewById(R.id.payment_card_input_widget);

        mAnonymousCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFullNameText.setVisibility(b ?  GONE : VISIBLE);
                mEmailText.setVisibility(b ? GONE : VISIBLE);
            }
        });
        mDonationAmountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mDonationAmountText.setText("$" + (i + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }
}