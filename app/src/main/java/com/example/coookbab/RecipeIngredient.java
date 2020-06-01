package com.example.coookbab;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.List;

public class RecipeIngredient extends AppCompatActivity {
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    private String userUrl="";
    private DatabaseReference myrefrigerator;
    private DatabaseReference RiReference;
    private FirebaseDatabase mDatabase;
    private String ingredientid;
    private RadioButton rdo_coupang, rdo_ssg, rdo_gs, rdo_emart, rdo_kulry, rdo_timon;
    private RadioGroup rdogroup_market;
    String packagename;
    int result_market;
    private int [] table = new int[500];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredient);
        final Intent intent=getIntent();
        String recipe_num=intent.getExtras().getString("recipe_num");//레시피 번호
        String recipe_need=intent.getExtras().getString("i");
        final int rcpneed= Integer.parseInt(recipe_need);//총 몇개의 재료

        rdo_coupang=findViewById(R.id.rdo_coupang);
        rdo_emart=findViewById(R.id.rdo_emart);
        rdo_gs=findViewById(R.id.rdo_gs);
        rdo_ssg=findViewById(R.id.rdo_ssg);
        rdo_timon=findViewById(R.id.rdo_timon);
        rdo_kulry=findViewById(R.id.rdo_kurly);
        rdogroup_market=findViewById(R.id.rdogroup_market);

        rdogroup_market.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rdo_coupang) {
                    result_market=1;
                    packagename="com.coupang.mobile";
                }
                else if(checkedId==R.id.rdo_ssg) {
                    result_market = 2;
                    packagename="kr.co.ssg";
                }
                else if(checkedId==R.id.rdo_timon) {
                    result_market = 3;
                    packagename = "com.tmon";
                }
                else if(checkedId==R.id.rdo_gs) {
                    result_market=4;
                    packagename = "com.gsretail.android.smapp";
                }
                else if(checkedId==R.id.rdo_emart) {
                    result_market=5;
                    packagename = "kr.co.emart.emartmall";
                }
                else {
                    result_market=6;
                    packagename = "com.dbs.kurly.m2&hl=ko";
                    //packagename = "com.dbs.kurly.m2";
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();
//test
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        mDatabase = FirebaseDatabase.getInstance();
        myrefrigerator = mDatabase.getReference().child("user").child(userUrl).child("refrigerator").child("ingredient");
        myrefrigerator.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot igdSnapshot : dataSnapshot.getChildren()){
                    String igd_id  = igdSnapshot.child("ingredientid").getValue().toString();
                    String igd_num = igdSnapshot.child("num").getValue().toString();
                    table[Integer.parseInt(igd_id)] = Integer.parseInt(igd_num);
                    Log.e(this.getClass().getName(), "table["+igd_id+"]="+table[Integer.parseInt(igd_id)]);
                    Log.e(this.getClass().getName(), igd_id + "의 개수는 "+igd_num);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError dtE) {
            }
        });
        RiReference = mDatabase.getReference().child("recipe").child(recipe_num).child("ingredients");
        RiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(int i=1;i<=rcpneed;i++){
                    ingredientid = dataSnapshot.child(String.valueOf(i)).child("ingredientid").getValue().toString();
                    final String ingredientname=dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString();
                    String need = dataSnapshot.child(String.valueOf(i)).child("need").getValue().toString();
                    final Button marketbtn = new Button(getApplicationContext());
                    TextView textView1 = new TextView(getApplicationContext());
                    TextView textView2 = new TextView(getApplicationContext());
                    final TextView have = new TextView(getApplicationContext());
                    final TextView name = new TextView(getApplicationContext());
                    final ImageView img=new ImageView(getApplicationContext());


                    LinearLayout littlelinearlayout=new LinearLayout(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(20,20,20,20);
                    littlelinearlayout.setLayoutParams(layoutParams);
                    linearLayout.addView(littlelinearlayout);


                    LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(100,100);
                    Params.setMargins(5,5,15,5);

                    marketbtn.setBackgroundResource(R.drawable.cart);
                    marketbtn.setLayoutParams(Params);



                    name.setTextColor(Color.parseColor("#000000"));
                    name.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20.0f);

                    textView1.setText(" / "+need+"     ");
                    textView2.setText(" ");

                    have.setText(String.valueOf(table[Integer.parseInt(ingredientid)]));
                    Log.e(this.getClass().getName(), "table["+ingredientid+"]="+table[Integer.parseInt(ingredientid)]);
                    name.setText(ingredientname.toString());
                    littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                    littlelinearlayout.addView(marketbtn);
                    littlelinearlayout.addView(name);
                    littlelinearlayout.addView(textView2);
                    littlelinearlayout.addView(have);
                    littlelinearlayout.addView(textView1);

                    marketbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url;
                            if (getPackageList(packagename)) {
                                if(result_market==1){ //쿠팡
                                    url = "https://m.coupang.com/nm/search?q="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                                else if(result_market==2){ //SSG
                                    url = "http://www.ssg.com/search.ssg?target=all&query="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                                else if(result_market==3){ //티몬
                                    url = "http://search.tmon.co.kr/search/?keyword="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                                else if(result_market==4){//gs샵
                                    url = "https://with.gsshop.com/shop/search/main.gs?lseq=392814&tq="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                                else if(result_market==5){ //이마트몰
                                    url="http://www.ssg.com/search.ssg?target=all&query="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                                else{ //마켓컬리
                                    url="https://www.kurly.com/shop/goods/goods_search.php?searched=Y&sword="+ingredientname;
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                            } else {
                                url = "market://details?id=" + packagename;
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(i);
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    public boolean getPackageList(String appname) {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith(appname)){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

}
