package dev.testcode.room;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private ImageAdapter imageAdapter;
    private Button button;
    private Button xoa;
    private Random random;
    private int[] resource = new int[]{R.drawable.test, R.drawable.test1, R.drawable.test2, R.drawable.test3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRecyclerView = findViewById(R.id.recycler_view);
        button = findViewById(R.id.btn_random);
        xoa = findViewById(R.id.Xoa);
        button.setOnClickListener(this);
        random = new Random();
        xoa.setOnClickListener(this);
        mRecyclerView.setItemAnimator(new NoAnimation());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        imageAdapter = new ImageAdapter(this);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(imageAdapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_random:
                imageAdapter.add(resource[random.nextInt(resource.length)]);
                imageAdapter.clearSelect();
                break;
            case R.id.Xoa:
                imageAdapter.remove();
                break;
        }
    }
}
