package com.example.deduyo_turnbasedgame;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import java.util.Objects;
import java.util.Random;



@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    //hp and mp bars
    ProgressBar heroHpBar,heroMpBar,monsHpBar,monsMpBar;
    //buttons
    Button basicAttack,fullSlash,chargedAttack,stun,pokedex;
    //text
    TextView mheroHp,mheroMp,mheroName,mmonsHp,mmonsMp, mmonsName,menuText, mwinIndicator;
    //layout
    ConstraintLayout menuBox;
    FrameLayout infoBox,heroStat,monsStat;
    //Animation
    Animation leftRight,rightLeft;
    //image button





    //Hero Class
    int heroHP = 274;
    int heroMaxHP = heroHP;
    int heroMP =48;
    int heroMaxMP = heroMP;
    int heroDamage;
    //HeroHp
    int heroHpPercent,heroMpPercent;
    String heroName = "Pikachu";

    //Monster Class
    int monsHP = 300;
    int monsMaxHP = monsHP;
    int monsMP = 30;
    int monsMaxMP = monsMP;
    int monsDamage;
    //MonsHp
    int monsHpPercent,monsMpPercent;
    String monsName = "Ditto";

    ////////////////Skill data////////////////
    //Basic Attack
    int basicAttackMin = 8;
    int basicAttackMax = 20;
    int basicAttackChance = 100;
    int basicAttackManaCharge = 15;
    //Full Slash
    int fullSlashMin = 80;
    int fullSlashMax = 100;
    int fullSlashChance = 70;
    int fullSlashManaCost = 10;
    //Charged Attack
    int chargedAttackMin = 40;
    int chargedAttackMax = 150;
    int chargedAttackChance = 100;
    int chargedAttackManaCost = 24;
    //Stun
    int heroStunned, monsStunned;
    int stunMin = 20;
    int stunMax = 45;
    int stunChance = 50;
    int stunManaCost = 24;
    //random number
    int random;

    //Button state
    boolean heroBasicAttack = false;
    boolean heroFullSlash = false;
    boolean heroChargedaAttack = false;
    boolean heroStun = false;

    //change the counter system into a speed system
    int heroSpeed = 306;
    int heroCurrentSpeed;
    int monSpeed = 214;
    int monsCurrentSpeed;
    //the number needed for a character to take a turn
    int speedLine = 520;

    //for the speed of the dialogue
    int time = 700;
    boolean timerRunning = false;
    CountDownTimer cdTimer;
    //for auto turn or manual
    boolean auto = true; //set to true for auto
    //who will be the winner
    boolean heroWin = false;




    public void startStop() {
        if (timerRunning && auto) {
            stopTimer();
            //Log.d(TAG, "stop Timer");
        } else if (auto) {
            startTimer();
            //Log.d(TAG, "start Timer");
        }
    }

    public void startTimer() {
        cdTimer = new CountDownTimer(time, time) {

            public void onTick(long l) {
                //Log.d(TAG, "onTick: tick");
            }

            public void onFinish() {
                speed();
                turnCheck();
                battlePhase();
                startStop();
                //Log.d(TAG, "onFinish: asdf");
            }
        }.start();
        timerRunning = true;
    }

    public void stopTimer () {
        cdTimer.cancel();
        timerRunning = false;
    }

    //Speed System
    public void speed(){
        while (heroCurrentSpeed <= speedLine && monsCurrentSpeed <= speedLine) {
            heroCurrentSpeed += heroSpeed;
            monsCurrentSpeed += monSpeed;
        }
        if (heroCurrentSpeed == monsCurrentSpeed) {
            Random randomizer = new Random();
            int rando = randomizer.nextInt(99);
            if (rando >= 50) {
                heroCurrentSpeed -=10;
            } else {
                monsCurrentSpeed -=10;
            }
        }
    }

    public void showButton(){
        //enables and shows the buttons
        basicAttack.setVisibility(View.VISIBLE);
        fullSlash.setVisibility(View.VISIBLE);
        chargedAttack.setVisibility(View.VISIBLE);
        stun.setVisibility(View.VISIBLE);
        basicAttack.setClickable(true);
        fullSlash.setClickable(true);
        chargedAttack.setClickable(true);
        stun.setClickable(true);
        //disables the menuText
        menuText.setVisibility(View.GONE);
        //disables layout click
        menuBox.setClickable(false);
    }

    public void hideButton(){
        //disables and shows the buttons
        basicAttack.setVisibility(View.GONE);
        fullSlash.setVisibility(View.GONE);
        chargedAttack.setVisibility(View.GONE);
        stun.setVisibility(View.GONE);
        basicAttack.setClickable(false);
        fullSlash.setClickable(false);
        chargedAttack.setClickable(false);
        stun.setClickable(false);
        //enables the menuText
        menuText.setVisibility(View.VISIBLE);
        //enables layout click
        menuBox.setClickable(true);
    }

    //Checks which character would go
    public void turnCheck(){
        if (heroCurrentSpeed >= speedLine){
            showButton();
        } else {
            hideButton();
        }
    }

    //battle phase
    public void battlePhase() {
        Random randomizer = new Random();
        random = randomizer.nextInt(100) + 1;
        if (heroCurrentSpeed >= speedLine && heroCurrentSpeed > monsCurrentSpeed) {
            if (heroStunned > 0) {
                menuText.setText(heroName + " is paralyzed for " + heroStunned + " turns");
                heroStunned--;
                heroCurrentSpeed -= speedLine;
                hideButton();
                bar();
                startStop();
            }else{
                if (heroBasicAttack) {
                    //checks for the attack chance
                    if (random < basicAttackChance) {
                        heroDamage = randomizer.nextInt(basicAttackMax - basicAttackMin) + basicAttackMin;
                        monsHP -= heroDamage;
                        menuText.setText(heroName + "'s Quick Attack dealt " + heroDamage + " to " + monsName + "!");
                    } else {
                        menuText.setText(heroName + "'s Quick Attack has missed!");
                    }
                    //checks if MP is max
                    if (heroMP != heroMaxMP && heroMP < heroMaxMP) {
                        heroMP += basicAttackManaCharge;
                    }
                    heroCurrentSpeed -= speedLine;
                    heroBasicAttack = false;
                    hideButton();
                    bar();
                    startStop();
                }
                if (heroFullSlash) {
                    if (random < fullSlashChance) {
                        heroDamage = randomizer.nextInt(fullSlashMax - fullSlashMin) + fullSlashMin;
                        monsHP -= heroDamage;
                        menuText.setText(heroName + "'s Iron Tail dealt " + heroDamage + " to " + monsName+ "!");
                    } else {
                        menuText.setText(heroName + "'s Iron Tail has missed!");
                    }
                    heroMP -= fullSlashManaCost;
                    heroCurrentSpeed -= speedLine;
                    heroFullSlash = false;
                    hideButton();
                    bar();
                    startStop();
                }
                if (heroChargedaAttack) {
                    if (random < chargedAttackChance) {
                        heroDamage = randomizer.nextInt(chargedAttackMax - chargedAttackMin) + chargedAttackMin;
                        monsHP -= heroDamage;
                        menuText.setText(heroName + "'s Electro Ball dealt " + heroDamage + " to " + monsName+ "!");
                    } else {
                        menuText.setText(heroName + "'s Electro Ball has missed!");
                    }
                    heroMP -= chargedAttackManaCost;
                    heroCurrentSpeed -= speedLine;
                    heroChargedaAttack = false;
                    hideButton();
                    bar();
                    startStop();
                }
                if (heroStun) {
                    if (random < stunChance) {
                        heroDamage = randomizer.nextInt(stunMax - stunMin) + stunMin;
                        monsHP -= heroDamage;
                        monsStunned = 2;
                        menuText.setText(heroName + "'s Thunderbolt dealt " + heroDamage + " to " + monsName+ "!");
                    } else {
                        menuText.setText(heroName + "'s Thunderbolt has missed!");
                    }
                    heroMP -= stunManaCost;
                    heroCurrentSpeed -= speedLine;
                    heroStun = false;
                    hideButton();
                    bar();
                    startStop();
                }
                if (monsHP <= 0) {
                    heroWin = true;
                    reset();
                    bar();
                }
            }
        } else {
            hideButton();
            if (monsStunned > 0) {
                menuText.setText(monsName + " is paralyzed for " + monsStunned + " turns"+ "!");
                monsStunned--;
                monsCurrentSpeed -= speedLine;
                hideButton();
                bar();
                startStop();
            } else {
                if (monsMP <= 0) {
                    if (random < basicAttackChance) {
                        monsBasicAttack();
                    }
                } else {
                    int randomAttack = randomizer.nextInt(4);
                    switch (randomAttack) {
                        case 0:
                            if (random < basicAttackChance) {
                                monsBasicAttack();
                            }
                            break;
                        case 1:
                            if (monsMP - fullSlashManaCost >= 0) {
                                if (random < fullSlashChance) {
                                    monsDamage = randomizer.nextInt(fullSlashMax - fullSlashMin) + fullSlashMin;
                                    heroHP -= monsDamage;
                                    menuText.setText(monsName + "'s Iron Tail dealt " + monsDamage + " to " + heroName+ "!");
                                } else {
                                    menuText.setText(monsName + "'s Iron Tail has missed!");
                                }
                                monsMP -= fullSlashManaCost;
                                monsCurrentSpeed -= speedLine;
                                bar();
                                startStop();
                            } else {
                                if (random < basicAttackChance) {
                                    monsBasicAttack();
                                }
                            }
                            break;
                        case 2:
                            if (monsMP - chargedAttackManaCost >= 0) {
                                if (random < chargedAttackChance) {
                                    monsDamage = randomizer.nextInt(chargedAttackMax - chargedAttackMin) + chargedAttackMin;
                                    heroHP -= monsDamage;
                                    menuText.setText(monsName + "'s Electro Ball dealt " + monsDamage + " to " + heroName+ "!");
                                } else {
                                    menuText.setText(monsName + "'s Electro Ball has missed!");
                                }
                                monsMP -= chargedAttackManaCost;
                                monsCurrentSpeed -= speedLine;
                                bar();
                                startStop();
                            } else {
                                if (random < basicAttackChance) {
                                    monsBasicAttack();
                                }
                            }
                            break;
                        case 3:
                            if (monsMP - stunManaCost >= 0) {
                                if (random < stunChance) {
                                    monsDamage = randomizer.nextInt(stunMax - stunMin) + stunMin;
                                    heroHP -= monsDamage;
                                    heroStunned = 2;
                                    menuText.setText(monsName + "'s Thunderbolt dealt " + monsDamage + " to the " + heroName+ "!");
                                } else {
                                    menuText.setText(monsName + "'s Thunderbolt has missed!");
                                }
                                monsMP -= stunManaCost;
                                monsCurrentSpeed -= speedLine;
                                bar();
                                startStop();
                            } else {
                                if (random < basicAttackChance) {
                                    monsBasicAttack();
                                }
                            }
                            break;
                    }
                }
                if (heroHP <= 0) {
                    reset();
                    bar();
                }
            }
        }

    }

    public void monsBasicAttack() {
        Random randomizer = new Random();
        monsDamage = randomizer.nextInt(basicAttackMax - basicAttackMin) + basicAttackMin;
        heroHP -= monsDamage;
        if(monsMP != monsMaxMP && monsMP < monsMaxMP) {
            monsMP += basicAttackManaCharge;
        } else {
            menuText.setText(monsName + "'s Quick Attack has missed!");
        }
        monsCurrentSpeed -= speedLine;
        menuText.setText(monsName + "'s Quick Attack dealt " + monsDamage +" to " + heroName+ "!");
        bar();
        startStop();
    }

    //resets the game
    public void reset() {
        if(heroWin) {
            mwinIndicator.setVisibility(View.VISIBLE);
            mwinIndicator.setText("The wild Ditto has fainted, Pikachu wins!");
            heroWin = false;
        } else {
            mwinIndicator.setVisibility(View.VISIBLE);
            mwinIndicator.setText("Pikachu fainted! You ran away and fled.");
        }
        heroHP = heroMaxHP;
        heroMP = heroMaxMP;
        monsHP = monsMaxHP;
        monsMP = monsMaxMP;
        heroDamage = 0;
        heroCurrentSpeed = 0;
        heroStunned = 0;
        monsDamage = 0;
        monsCurrentSpeed = 0;
        monsStunned = 0;
    }

    //manual movement of turns
    public void next(View v) {
        speed();
        turnCheck();
        battlePhase();
    }

    public void info(View v) {
        if(infoBox.getVisibility() == View.GONE){
            infoBox.setVisibility(View.VISIBLE);
        } else {
            infoBox.setVisibility(View.GONE);
        }
    }


    public void bar() {
        //sets the mp and hp
        mheroHp.setText(String.valueOf(heroHP));
        mheroMp.setText(String.valueOf(heroMP));
        mmonsHp.setText(String.valueOf(monsHP));
        mmonsMp.setText(String.valueOf(monsMP));
        // this is the formula to get the health/mp percentage
        heroHpPercent = heroHP * 100 / heroMaxHP;
        heroMpPercent = heroMP * 100 / heroMaxMP;
        monsHpPercent = monsHP * 100 / monsMaxHP;
        monsMpPercent = monsMP * 100 / monsMaxMP;
        //Setting the hp/mp bar
        heroHpBar.setProgress(heroHpPercent, true);
        heroMpBar.setProgress(heroMpPercent, true);
        monsHpBar.setProgress(monsHpPercent,true);
        monsMpBar.setProgress(monsMpPercent, true);




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fullscreen code
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        //Animation Call
        leftRight = AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        leftRight.setStartOffset(500);
        rightLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        rightLeft.setStartOffset(500);

        //Button call
        basicAttack = findViewById(R.id.basicAttack);
        fullSlash = findViewById(R.id.fullSlash);
        chargedAttack = findViewById(R.id.chargedAttack);
        stun = findViewById(R.id.stun);



        //Progress bar call
        heroHpBar = findViewById(R.id.heroHpBar);
        heroHpBar.setMax(100);
        heroMpBar = findViewById(R.id.heroMpBar);
        heroMpBar.setMax(100);
        monsHpBar = findViewById(R.id.monsHpBar);
        monsHpBar.setMax(100);
        monsMpBar = findViewById(R.id.monsMpBar);
        monsMpBar.setMax(100);

        //Textview Call
        mheroHp = findViewById(R.id.heroHp);
        mheroMp = findViewById(R.id.heroMp);
        mheroName = findViewById(R.id.heroName);
        mmonsHp = findViewById(R.id.monsHp);
        mmonsMp = findViewById(R.id.monsMp);
        mmonsName = findViewById(R.id.monsName);
        menuText = findViewById(R.id.menuText);
        mwinIndicator = findViewById(R.id.winIndicator);

        //setting the textviews
        mheroHp.setText(String.valueOf(heroHP));
        mheroMp.setText(String.valueOf(heroMP));
        mheroName.setText(heroName);
        mmonsHp.setText(String.valueOf(monsHP));
        mmonsMp.setText(String.valueOf(monsMP));
        mmonsName.setText(monsName);

        //Button Listeners
        basicAttack.setOnClickListener(this);
        fullSlash.setOnClickListener(this);
        chargedAttack.setOnClickListener(this);
        stun.setOnClickListener(this);

        //Layout
        menuBox = findViewById(R.id.menuBox);
        infoBox = findViewById(R.id.infoTextBox);
        heroStat = findViewById(R.id.heroStatsLayout);
        monsStat = findViewById(R.id.monsStatsLayout);


        pokedex = findViewById(R.id.viewInfoPokemon);
        pokedex.setOnClickListener(this);


        //runs the speed, turnCheck, and battlePhase
        speed();
        turnCheck();
    }

    @Override
    public void onClick(View v){
        //Sets the winning text to gone
        Log.d(TAG, "onClick: ");
        mwinIndicator.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.basicAttack:
                Log.d(TAG, "onClick: ");
                heroBasicAttack = true;
                battlePhase();
                break;
            case R.id.fullSlash:
                //Checks if the hero has enough MP
                if (heroMP-fullSlashManaCost >= 0) {
                    heroFullSlash = true;
                    battlePhase();
                } else {
                    menuText.setText("You don't have enough power points for this move");
                    hideButton();
                    startStop();
                }
                break;
            case R.id.chargedAttack:
                //Checks if the hero has enough MP
                if (heroMP-chargedAttackManaCost >= 0) {
                    heroChargedaAttack = true;
                    battlePhase();
                } else {
                    menuText.setText("You don't have enough power points for this move");
                    hideButton();
                    startStop();
                }
                break;
            case R.id.stun:
                //Checks if the hero has enough MP
                if (heroMP-stunManaCost >= 0) {
                    heroStun = true;
                    battlePhase();
                } else {
                    menuText.setText("You don't have enough power points for this move");
                    hideButton();
                    startStop();
                }
                break;
            case R.id.viewInfoPokemon:

                openActivity2();
                break;
        }
    }


    public void openActivity2() {
        Intent intent = new Intent(this, info.class);
        startActivity(intent);
    }



}