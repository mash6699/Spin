package mx.spin.mobile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.singleton.SpingApplication;

public class AnalizeResultActivity extends AppCompatActivity {

    private final static String TAG = AnalizeResultActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.tv_pool_name)
    TextView pool_name;
    @Nullable
    @Bind(R.id.tv_pool_date)
    TextView pool_date;

    @Nullable
    @Bind(R.id.txtAnalizeResultBalance)
    TextView txtSegmentOne;
    @Nullable
    @Bind(R.id.txtAnalizeResultDesinf)
    TextView txtSegmentTwo;
    @Nullable
    @Bind(R.id.txtAnalizeResultIndiceSaturacion)
    TextView txtIndiceSaturacion;
    @Nullable
    @Bind(R.id.txtAnalizeResultStateWater)
    TextView txtStateWater;

    @Nullable
    @Bind(R.id.contentBalance)
    LinearLayout containerBalance;
    @Nullable
    @Bind(R.id.contentDesinfeccion)
    LinearLayout containerDesinfeccion;

    @Nullable
    @Bind(R.id.cv_resultados)
    View cv_btn_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize_result);
        ButterKnife.bind(this);

        txt_titleToolbar.setText(R.string.title_activity_analize_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());


        txtSegmentOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                containerBalance.setVisibility(View.VISIBLE);
                containerDesinfeccion.setVisibility(View.GONE);

                txtIndiceSaturacion.setVisibility(View.VISIBLE);
                txtStateWater.setVisibility(View.VISIBLE);

                cv_btn_result.setVisibility(View.GONE);
            }
        });

        txtSegmentTwo.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                txtSegmentOne.setBackground(getResources().getDrawable(R.drawable.bottom_border_line_light));
                txtSegmentTwo.setBackground(getResources().getDrawable(R.drawable.bottom_border_line_strong));

                containerBalance.setVisibility(View.GONE);
                containerDesinfeccion.setVisibility(View.VISIBLE);

                txtIndiceSaturacion.setVisibility(View.GONE);
                txtStateWater.setVisibility(View.GONE);

                cv_btn_result.setVisibility(View.VISIBLE);
            }
        });
    }


    @Nullable
    @OnClick(R.id.btnAnalizeResultInitMantenimiento)
    public void gotoMantenimiento(View view){
        startActivity(new Intent(AnalizeResultActivity.this, MantenimientoActivity.class));
    }

    void setValuesResult(){
      Log.d(TAG, "setValuesResult");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
