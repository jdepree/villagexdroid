package org.villagex.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.villagex.R;
import org.villagex.model.Project;

public class PaymentView extends LinearLayout {
    private Dialog mDialog;
    private CardInputWidget mCardWidget;
    private CheckBox mAnonymousCheck;
    private EditText mFullNameText;
    private EditText mEmailText;
    private EditText mAmountText;
    private Project mProject;
    private Button mDonateButton;

    public PaymentView(Dialog dialog, Project project) {
        super(dialog.getContext());
        mDialog = dialog;
        mProject = project;

        setBackground(ContextCompat.getDrawable(dialog.getContext(), R.drawable.transparent_background));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
        mAmountText = findViewById(R.id.payment_amount);
        mCardWidget = findViewById(R.id.payment_card_input_widget);
        mDonateButton = findViewById(R.id.submit_donation_button);

        mAnonymousCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            mFullNameText.setVisibility(b ? GONE : VISIBLE);
        });
        findViewById(R.id.payment_anonymous_label).setOnClickListener(v -> mAnonymousCheck.performClick());

        mDonateButton.setOnClickListener(v -> {
            Card cardToSave = mCardWidget.getCard();
            if (cardToSave == null) {
            } else {
                Stripe stripe = new Stripe(getContext(), "pk_test_6pRNASCoBOKtIshFeQd4XMUh");
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                mDialog.dismiss();
                            }
                            public void onError(Exception error) {
                                Toast.makeText(getContext(),
                                        error.getLocalizedMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
            }
        });

        mAmountText.setOnFocusChangeListener((v, hasFocus) -> {
            CharSequence text = mAmountText.getText();
            if (hasFocus && (text.length() == 0 || text.charAt(0) != '$')) {
                mAmountText.setText("$" + text);
                mAmountText.setSelection(1);
            }
        });

        mFullNameText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mFullNameText, InputMethodManager.SHOW_IMPLICIT);
    }
}