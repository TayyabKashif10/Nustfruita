package com.nustfruta.misc;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nustfruta.R;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivPopupImage, ivBackIcon;
    TextView tvTayyab, tvSabih, tvAhmed;
    PopupWindow popupWindow;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTayyab = findViewById(R.id.tvTayyab);
        tvSabih = findViewById(R.id.tvSabih);
        tvAhmed = findViewById(R.id.tvAhmed);
        ivBackIcon = findViewById(R.id.backIcon);

        ivBackIcon.setOnClickListener(this);
        tvTayyab.setOnClickListener(this);
        tvSabih.setOnClickListener(this);
        tvAhmed.setOnClickListener(this);

        parent = findViewById(R.id.main);
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_image, parent, false);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ivPopupImage = popupView.findViewById(R.id.ivPopupImage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvTayyab)
            ivPopupImage.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.tayyab));
        else if (v.getId() == R.id.tvSabih)
            ivPopupImage.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sabih));
        else if (v.getId() == R.id.tvAhmed)
            ivPopupImage.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ahmed));
        else if (v.getId() == R.id.backIcon)
            finish();
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}