package com.example.a1to50db;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    View[] include = new View[3];
    RelativeLayout[] button = new RelativeLayout[25];
    TextView[] button_text = new TextView[25];
    RelativeLayout ok_button;
    Button Rank;
    Button View;
    TextView now_number;
    EditText edit_name;
    TextView edit_View;
    //타이머
    TextView myOutput;
    TextView myRec;
    Button myBtnStart;
//DB
    dbHelper helper;
    SQLiteDatabase db;
    EditText editText, edit_tel;
    private static final String DATABASE_NAME = "mycontacts6.db";
    private static final int DATABASE_VERSION = 2;
    private Activity mActivity;


    final static int Init =0;
    final static int Run =1;
    final static int Pause =2;

    int cur_Status = Init;
    int myCount=1;
    long myBaseTime;
    long myPauseTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //타이머
        myOutput = (TextView) findViewById(R.id.time_out);

        //재시작버튼
        final  Button btn_start = (Button) findViewById(R.id.btn_Restart);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }

        });
        final  Button btn_Visible = (Button) findViewById(R.id.btn_Visible);
        btn_Visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                now_number.setVisibility(View.GONE);
            }

        });

        final  Button btn_inVisible = (Button) findViewById(R.id.btn_inVisible);
        btn_inVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                now_number.setVisibility(View.VISIBLE);
            }

        });

        final  Button End = (Button) findViewById(R.id.End);
        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }

        });

        final  Button Rank = (Button) findViewById(R.id.Rank);
        Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), RankActivity.class);
              startActivity(intent);
            }

        });

        //DB
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);
        startManagingCursor(cursor);
        String[] from = {"name", "tel" };
        int[] to = { android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,cursor, from, to);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        all_view();


        //setVisibility 레이아웃 활성화
        include[0].setVisibility(View.VISIBLE);
        include[1].setVisibility(View.GONE);
        include[2].setVisibility(View.GONE);
        game(); all_button_click(); //메서드실행
    }
    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            myOutput.setText(getTimeOut());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송
            myTimer.sendEmptyMessage(0);
        }
    };
    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        return easy_outTime;

    }

    int number_group1, number_group2;
    private void game() {
        now_number.setText(Integer.toString(match_number_int));
        for(number_group1 = 1; number_group1 <= 2; number_group1++) {
            for(number_group2 = 1; number_group2 <= 25; number_group2++) {
                random();
            }
        }
        for(int print_button_num = 0; print_button_num <= 24; print_button_num++) {
            button_text[print_button_num].setText(Integer.toString(button_number_01[print_button_num]));
        }
    }
    int match_number_int = 1;
    int random_button_number_int;
    int[] button_number_01 = new int[25];
    int[] button_number_02 = new int[25];
    private void random() {
        Random random = new Random();
        random_button_number_int = random.nextInt(25);
        if(number_group1 == 1 && button_number_01[(random_button_number_int)] == 0) {
            button_number_01[(random_button_number_int)] = number_group2;
        }else if(number_group1 == 1 && number_group2 <= 25){
            random();
        }
        if(number_group1 == 2 && button_number_02[(random_button_number_int)] == 0) {
            button_number_02[(random_button_number_int)] = (number_group2+25);
        }else if(number_group1 == 2 && number_group2 <= 25){
            random();
        }
    }
    private void all_view() {
        include[0] = findViewById(R.id.include_game_view);
        include[1] = findViewById(R.id.include_success_view);
        include[2] = findViewById(R.id.include_list_view);
        now_number = (TextView)findViewById(R.id.now_number);
        button[0] = (RelativeLayout)findViewById(R.id.button_1);
        button[1] = (RelativeLayout)findViewById(R.id.button_2);
        button[2] = (RelativeLayout)findViewById(R.id.button_3);
        button[3] = (RelativeLayout)findViewById(R.id.button_4);
        button[4] = (RelativeLayout)findViewById(R.id.button_5);
        button[5] = (RelativeLayout)findViewById(R.id.button_6);
        button[6] = (RelativeLayout)findViewById(R.id.button_7);
        button[7] = (RelativeLayout)findViewById(R.id.button_8);
        button[8] = (RelativeLayout)findViewById(R.id.button_9);
        button[9] = (RelativeLayout)findViewById(R.id.button_10);
        button[10] = (RelativeLayout)findViewById(R.id.button_11);
        button[11] = (RelativeLayout)findViewById(R.id.button_12);
        button[12] = (RelativeLayout)findViewById(R.id.button_13);
        button[13] = (RelativeLayout)findViewById(R.id.button_14);
        button[14] = (RelativeLayout)findViewById(R.id.button_15);
        button[15] = (RelativeLayout)findViewById(R.id.button_16);
        button[16] = (RelativeLayout)findViewById(R.id.button_17);
        button[17] = (RelativeLayout)findViewById(R.id.button_18);
        button[18] = (RelativeLayout)findViewById(R.id.button_19);
        button[19] = (RelativeLayout)findViewById(R.id.button_20);
        button[20] = (RelativeLayout)findViewById(R.id.button_21);
        button[21] = (RelativeLayout)findViewById(R.id.button_22);
        button[22] = (RelativeLayout)findViewById(R.id.button_23);
        button[23] = (RelativeLayout)findViewById(R.id.button_24);
        button[24] = (RelativeLayout)findViewById(R.id.button_25);
        button_text[0] = (TextView)findViewById(R.id.button_text_1);
        button_text[1] = (TextView)findViewById(R.id.button_text_2);
        button_text[2] = (TextView)findViewById(R.id.button_text_3);
        button_text[3] = (TextView)findViewById(R.id.button_text_4);
        button_text[4] = (TextView)findViewById(R.id.button_text_5);
        button_text[5] = (TextView)findViewById(R.id.button_text_6);
        button_text[6] = (TextView)findViewById(R.id.button_text_7);
        button_text[7] = (TextView)findViewById(R.id.button_text_8);
        button_text[8] = (TextView)findViewById(R.id.button_text_9);
        button_text[9] = (TextView)findViewById(R.id.button_text_10);
        button_text[10] = (TextView)findViewById(R.id.button_text_11);
        button_text[11] = (TextView)findViewById(R.id.button_text_12);
        button_text[12] = (TextView)findViewById(R.id.button_text_13);
        button_text[13] = (TextView)findViewById(R.id.button_text_14);
        button_text[14] = (TextView)findViewById(R.id.button_text_15);
        button_text[15] = (TextView)findViewById(R.id.button_text_16);
        button_text[16] = (TextView)findViewById(R.id.button_text_17);
        button_text[17] = (TextView)findViewById(R.id.button_text_18);
        button_text[18] = (TextView)findViewById(R.id.button_text_19);
        button_text[19] = (TextView)findViewById(R.id.button_text_20);
        button_text[20] = (TextView)findViewById(R.id.button_text_21);
        button_text[21] = (TextView)findViewById(R.id.button_text_22);
        button_text[22] = (TextView)findViewById(R.id.button_text_23);
        button_text[23] = (TextView)findViewById(R.id.button_text_24);
        button_text[24] = (TextView)findViewById(R.id.button_text_25);
        ok_button = (RelativeLayout)findViewById(R.id.ok_button);
    }

    int button_num;
    private void all_button_click() {
        for (int i = 0; i < 25; i++) {
            final int j = i;
            button[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(match_number_int == 50) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                        ad.setTitle("기록 저장");       // 제목 설정
                        ad.setMessage("이름을 입력해주세요");   // 내용 설정

// EditText 삽입하기
                        final EditText et = new EditText(MainActivity.this);
                        ad.setView(et);
                        final EditText editname1 = (EditText) findViewById(R.id.editname1);

                        // 확인 버튼 설정
                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v(TAG, "Yes Btn Click");
                                // Text 값 받아서 로그 남기기
                                editname1.setText(et.getText().toString());
                                String value = editname1.getText().toString();
                                Log.v(TAG, value);
                                dialog.dismiss();     //닫기
                                TextView textView = (TextView)findViewById(R.id.result);

                                textView.setText("       기록\n" + myOutput.getText().toString() + "\n다시 하겠습니까?");
                                TextView TextView = (TextView) findViewById(R.id.time_out);
                                //날짜구하기
                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String formatDate = sdfNow.format(date);
                                db.execSQL("INSERT INTO contacts VALUES (null, " + "'" + TextView.getText().toString() + "  " + editname1.getText().toString() + "'" + ", '" + formatDate + "');");
                                myTimer.removeMessages(0);
                                myOutput.setText("00:00:00");
                                cur_Status = Init;
                                myCount = 1;
                                include[1].setVisibility(View.VISIBLE);
                                include[0].setVisibility(View.GONE);
                            }
                        });
                            // 취소 버튼 설정
                        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v(TAG, "No Btn Click");
                                dialog.dismiss();     //닫기
                                TextView textView = (TextView)findViewById(R.id.result);

                                textView.setText("       기록\n" + myOutput.getText().toString() + "\n다시 하겠습니까?");
                                TextView TextView = (TextView) findViewById(R.id.time_out);
                                //날짜구하기
                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String formatDate = sdfNow.format(date);
                                myTimer.removeMessages(0);
                                myOutput.setText("00:00:00");
                                cur_Status = Init;
                                myCount = 1;
                                include[1].setVisibility(View.VISIBLE);
                                include[0].setVisibility(View.GONE);
                            }
                        });
                        // 창 띄우기
                        ad.show();
                    }
                    if (match_number_int == 1)
                    {
                        switch(cur_Status){
                            case Init:
                                myBaseTime = SystemClock.elapsedRealtime();
                                System.out.println(myBaseTime);
                                //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                                myTimer.sendEmptyMessage(0);
                                cur_Status = Run; //현재상태를 런상태로 변경
                                break;
                            case Run:
                                myTimer.removeMessages(0); //핸들러 메세지 제거
                                myPauseTime = SystemClock.elapsedRealtime();
                                cur_Status = Pause;
                                break;
                            case Pause:
                                myTimer.removeMessages(0);
                                myOutput.setText("00:00:00");
                                cur_Status = Init;
                                myCount = 1;
                                break;
                        }
                    }
                    int number = j;
                    if(match_number_int == button_number_01[number]) {
                        match_number_int += 1;

                        button_text[number].setText(Integer.toString(button_number_02[number]));
                    }
                    if(match_number_int == button_number_02[number]) {
                        match_number_int += 1;
                        button_text[number].setVisibility(View.GONE);
                    }
                    now_number.setText(Integer.toString(match_number_int));
                }
            });
        }
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                match_number_int = 1;
                for(int reset_button_number = 0; reset_button_number <= 24; reset_button_number++) {
                    button_number_01[reset_button_number] = 0;
                    button_number_02[reset_button_number] = 0;
                    button_text[reset_button_number].setVisibility(View.VISIBLE);
                }
                include[0].setVisibility(View.VISIBLE);
                include[1].setVisibility(View.GONE);
                game();
            }
        });
    }
}
