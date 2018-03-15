package org.villagex.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stripe.android.view.CardInputWidget;

import org.villagex.R;
import org.villagex.model.Project;

public class PaymentView extends LinearLayout {
    private SeekBar mDonationAmountBar;
    private TextView mDonationAmountText;
    private CardInputWidget mCardWidget;
    private CheckBox mAnonymousCheck;
    private TextView mFullNameText;
    private TextView mEmailText;
    private Project mProject;

    public PaymentView(Context context, Project project) {
        super(context);
        setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_transparent_background));
        mProject = project;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setPadding(25, 25, 25, 25);
        inflate(getContext(), R.layout.payment_layout, this);

        TextView paymentTitleText = findViewById(R.id.payment_title);
        paymentTitleText.setText(getResources().getString(R.string.payment_title, mProject.getVillage().getName()));

        mAnonymousCheck = findViewById(R.id.payment_anonymous);
        mFullNameText = findViewById(R.id.payment_full_name);
        mEmailText = findViewById(R.id.payment_email);
        mDonationAmountBar = findViewById(R.id.payment_amount_slider);
        mDonationAmountText = findViewById(R.id.payment_amount_text);
        mCardWidget = findViewById(R.id.payment_card_input_widget);

        mAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            mFullNameText.setVisibility(b ? GONE : VISIBLE);
            mEmailText.setVisibility(b ? GONE : VISIBLE);
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