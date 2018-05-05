package ru.prsolution.winstrike.common;
/*
 * Created by oleg on 31.01.2018.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;

public class FavPushDialogActivity extends AppCompatActivity  {
    private Boolean isPushDialog = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bgr);
        ButterKnife.bind(this);

        isPushDialog = getIntent().getBooleanExtra("pushDialog", false);

        if (isPushDialog) {
            launchPushDlg();
        } else {
            launchFavDlg();
        }
    }

    private void launchFavDlg() {

        Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_fav);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView cs_game = dialog.findViewById(R.id.cs_game);
        TextView dota_game = dialog.findViewById(R.id.dota2_game);
        ImageView iv_cs = dialog.findViewById(R.id.iv_cs);
        ImageView iv_dota = dialog.findViewById(R.id.iv_dota);

        cs_game.setOnClickListener(
                it->{
                  iv_cs.setVisibility(View.VISIBLE);
                  iv_dota.setVisibility(View.GONE);
                }
        );

        dota_game.setOnClickListener(
                it->{
                    iv_dota.setVisibility(View.VISIBLE);
                    iv_cs.setVisibility(View.GONE);
                }
        );

        CardView btnOk = dialog.findViewById(R.id.btn_ok);
        CardView btnCancel = dialog.findViewById(R.id.btn_cancel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnOk.setElevation(0f);
            btnCancel.setElevation(0f);
        }

        btnOk.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FavPushDialogActivity.class);
            intent.putExtra("pushDialog", true);
            startActivity(intent);
          dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FavPushDialogActivity.class);
            intent.putExtra("pushDialog", true);
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }

    private void launchPushDlg() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_push);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        CardView btnOk = dialog.findViewById(R.id.btn_ok);
        CardView btnCancel = dialog.findViewById(R.id.btn_cancel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnOk.setElevation(0f);
            btnCancel.setElevation(0f);
        }

        btnOk.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LocationActivity.class));
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LocationActivity.class));
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();

    }
}
