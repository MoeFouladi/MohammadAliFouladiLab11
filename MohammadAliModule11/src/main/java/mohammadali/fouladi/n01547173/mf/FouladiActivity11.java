package mohammadali.fouladi.n01547173.mf;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class FouladiActivity11 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Moe());
        fragmentList.add(new Fouladi());
        fragmentList.add(new N01547173());
        fragmentList.add(new Mf());

        ViewPager2 viewPager = findViewById(R.id.MoeviewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this, fragmentList));

        TabLayout tabLayout = findViewById(R.id.MoetabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Customize the tab name based on position/index

                    if(position== 0)
                    { tab.setText("Moe");
                    tab.setIcon(R.drawable.moe);}

                    else if(position== 1)


                    {   tab.setText("Fouladi");
                    tab.setIcon(R.drawable.fouladi);}
                    else if(position== 2)
                        {   tab.setText("N01547173");
                        tab.setIcon(R.drawable.n01547173);}
                    else if(position== 3)
                    {   tab.setText("Mf");
                    tab.setIcon(R.drawable.mf);
                    }


                }
        ).attach();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_contact) {
            Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}