package in.co.madhur.chatbubblesdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import in.co.madhur.chatbubblesdemo.model.ChatMessage;
import in.co.madhur.chatbubblesdemo.model.MessageType;
import in.co.madhur.chatbubblesdemo.model.Status;
import in.co.madhur.chatbubblesdemo.model.UserType;
import in.co.madhur.chatbubblesdemo.widgets.Emoji;
import in.co.madhur.chatbubblesdemo.widgets.EmojiView;
import in.co.madhur.chatbubblesdemo.widgets.SizeNotifierRelativeLayout;

import static in.co.madhur.chatbubblesdemo.R.id.dialog;


public class MainActivity extends Activity implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate {

    private ListView chatListView;
    private EditText chatEditText1;
    private ArrayList<ChatMessage> chatMessages;
    private TextView enterChatView1;
    private ImageView emojiButton;
    private ChatListAdapter listAdapter;
    private EmojiView emojiView;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean showingEmoji;
    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;

    int replyCount;
    ChatMessage[] replies;

    static String DIALOG_NEED_DIR = "do you need direction to your gate?";
    static String DIALOG_MEAL_YES_NO = "do you want to select your meal now?";
    static String DIALOG_MEAL = "DIALOG_MEAL";
    static String DIALOG_DRINK = "DIALOG_DRINK";
    static String DIALOG_CONFIRM_MEAL_AND_DRINK = "DIALOG_CONFIRM_MEAL_AND_DRINK";
    static String DIALOG_NEED_BLANKET = "DIALOG_NEED_BLANKET";
    static String DIALOG_TRAVEL_GUIDE = "DIALOG_TRAVEL_GUIDE";
    static String NEW_PAGE_MAP = "NEW_PAGE_MAP";
    static String DIALOG_CALL_CS = "DIALOG_CALL_CS";
    static String DIALOG_CONFIRM_LOCATION_GUIDE = "DIALOG_CONFIRM_LOCATION_GUIDE";
    static String DIALOG_LOCATION_GUIDE_SELECT_PPL = "DIALOG_LOCATION_GUIDE_SELECT_PPL";
    static String DIALOG_LOCATION_GUIDE_DETAIL = "DIALOG_LOCATION_GUIDE_DETAIL";

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if (v == chatEditText1) {
                    sendMessage(editText.getText().toString(), UserType.OTHER);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == enterChatView1) {
                sendMessage(chatEditText1.getText().toString(), UserType.OTHER);
                chatListView.setSelection(listAdapter.getCount() - 1);
            }

            chatEditText1.setText("");

        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {

            } else {
//                enterChatView1.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
//                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            } else {
//                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidUtilities.statusBarHeight = getStatusBarHeight();

//        CustomDialogManager cdManager = new CustomDialogManager();
//        cdManager.constructYourDialog(MainActivity.this, getLayoutInflater(), R.layout.activity_login).show();

        chatMessages = new ArrayList<>();

        chatListView = (ListView) findViewById(R.id.chat_list_view);

        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (TextView) findViewById(R.id.enter_chat1);

        // Hide the emoji on click of edit text
        chatEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showingEmoji)
                    hideEmojiPopup();
            }
        });

        emojiButton = (ImageView) findViewById(R.id.emojiButton);

        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmojiPopup(!showingEmoji);
            }
        });

        listAdapter = new ChatListAdapter(chatMessages, this);

        chatListView.setAdapter(listAdapter);

        chatEditText1.setOnKeyListener(keyListener);

        enterChatView1.setOnClickListener(clickListener);

        chatEditText1.addTextChangedListener(watcher1);

        sizeNotifierRelativeLayout = (SizeNotifierRelativeLayout) findViewById(R.id.chat_layout);
        sizeNotifierRelativeLayout.delegate = this;

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.emojiDidLoaded);


        replies = new ChatMessage[]{
// auto
                new ChatMessage(MessageType.TEXT, "Dear Mr Ho, my name is Cathay. I'm your connoisseur for your journey. Welcome to CX243. Let me know if you need any help."),
// Arrive airport (invisible button)
                new ChatMessage(MessageType.TEXT, "You already checkin. Please proceed to baggage counter."),
// 30 mins b4 boarding time (+30min, invisible button)
                new ChatMessage(MessageType.IMG, DIALOG_NEED_DIR),
// clicked YES
// show mp4
                new ChatMessage(MessageType.TEXT, NEW_PAGE_MAP),
// back

// before boarding (invisible button)
                new ChatMessage(MessageType.TEXT, DIALOG_MEAL_YES_NO),
// clicked yes
                new ChatMessage(MessageType.TEXT, DIALOG_MEAL),
// selected meal
                new ChatMessage(MessageType.TEXT, DIALOG_DRINK),
// selected drink
                new ChatMessage(MessageType.TEXT, DIALOG_CONFIRM_MEAL_AND_DRINK),
// selected confirm button
                new ChatMessage(MessageType.TEXT, "Your choice has been confirmed"),


// input sth...
                new ChatMessage(MessageType.TEXT, DIALOG_NEED_BLANKET),

// clicked yes
                new ChatMessage(MessageType.TEXT, "請等等，我們將馬上為您服務"),

// 90 mins b4 landing (invisible button)
                new ChatMessage(MessageType.TEXT, "Hi, we have almost arrived at Changi International Airport‎.\nThe weather is 89 F, sunny. Remember to drink enough water before you disembark.\n Lavatories maybe occupied during descend. Please go earlier."),

// input sth... // Oh dear... Do you want to call our customer service online?
                new ChatMessage(MessageType.TEXT, DIALOG_CALL_CS),

// clicked NO

// input sth... //
                new ChatMessage(MessageType.TEXT, DIALOG_CONFIRM_LOCATION_GUIDE),

// clicked YES
                new ChatMessage(MessageType.TEXT, DIALOG_LOCATION_GUIDE_SELECT_PPL),

// selected 1
                new ChatMessage(MessageType.TEXT, DIALOG_LOCATION_GUIDE_DETAIL),

// clicked empty to dimiss dialog

        };

        // Set initial message
        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Status.SENT);
        String reply = replies[replyCount++].getMessageText();
        message.setMessageText(reply);
        message.setUserType(UserType.SELF);
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);
    }

    private void showPopup() {
        // reply
        String reply = replies[replyCount].getMessageText();

// Show?
        if (reply.equals(DIALOG_NEED_DIR)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this,
                            getLayoutInflater(),
                            R.drawable.meals, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    dialog.show();
                }
            });

        } else if (reply.equals(DIALOG_MEAL_YES_NO)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this,
                            getLayoutInflater(),
                            R.drawable.meals, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    dialog.show();
                }
            });

        } else if (reply.equals(DIALOG_MEAL)) {
            // TODO show dialog

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this,
                            getLayoutInflater(),
                            R.drawable.meals, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //go to next.
                                    replyCount++;
                                    showPopup();
                                }
                            });
                    dialog.show();
                }
            });

        } else if (reply.equals(DIALOG_DRINK)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomDialogManager.instance.listviewDialog(MainActivity.this, getLayoutInflater(), Arrays.asList(
                            "Warm Water", "Cold Water", "Coke", "Sprite", "Orange Juice", "Apple Juice", "Beer"
                    )).show();
                }
            });

        } else if (reply.equals(DIALOG_CONFIRM_MEAL_AND_DRINK)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.confirmed_selected_meal, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    }).show();
                }
            });

        } else if (reply.equals(DIALOG_NEED_BLANKET)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.do_you_a_blanket, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            replyCount++;
                            showPopup();
                        }
                    }).show();
                }
            });

        } else if (reply.equals(DIALOG_TRAVEL_GUIDE)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.travel_guide_review, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            replyCount++;
                            showPopup();
                        }
                    });
                }
            });

        } else if (reply.equals(NEW_PAGE_MAP)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.travel_guide_review, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            replyCount++;
                            showPopup();
                        }
                    });
                }
            });

        } else if (reply.equals(DIALOG_CALL_CS)) {

            //NA

        } else if (reply.equals(DIALOG_CONFIRM_LOCATION_GUIDE)) {

            //would you like
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.customDialog(MainActivity.this, getLayoutInflater(), R.drawable.travel_guide_review, "Do you mean Gardens by the Bay", "", "Yes", "No", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Yes

                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //No
                        }
                    });
                }
            });

        } else if (reply.equals(DIALOG_LOCATION_GUIDE_SELECT_PPL)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.selected_cruise, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            });

        } else if (reply.equals(DIALOG_LOCATION_GUIDE_DETAIL)) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Dialog dialog = CustomDialogManager.instance.imageDialog(MainActivity.this, getLayoutInflater(), R.drawable.travel_guide_review, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            });

        }
    }

    private void sendMessage(final String messageText, final UserType userType) {
        if (messageText.trim().length() == 0)
            return;

        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Status.SENT);
        message.setMessageText(messageText);
        message.setUserType(userType);
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);

        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();

//        message.setMessageStatus(Status.DELIVERED);

        // reply
        final String reply = replies[replyCount++].getMessageText();

        // Mark message as delivered after one second

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (reply.equals(DIALOG_NEED_DIR) ||
                        reply.equals(DIALOG_MEAL_YES_NO) ||
                        reply.equals(DIALOG_MEAL) ||
                        reply.equals(DIALOG_DRINK) ||
                        reply.equals(DIALOG_CONFIRM_MEAL_AND_DRINK) ||
                        reply.equals(DIALOG_NEED_BLANKET) ||
                        reply.equals(DIALOG_TRAVEL_GUIDE) ||
                        reply.equals(NEW_PAGE_MAP) ||
                        reply.equals(DIALOG_CALL_CS) ||
                        reply.equals(DIALOG_CONFIRM_LOCATION_GUIDE) ||
                        reply.equals(DIALOG_LOCATION_GUIDE_SELECT_PPL) ||
                        reply.equals(DIALOG_LOCATION_GUIDE_DETAIL)) {

                    showPopup();

                    return;

                } else {

                    //
                    final ChatMessage message = new ChatMessage();
                    message.setMessageStatus(Status.SENT);

                    message.setMessageText(reply);

                    message.setUserType(UserType.SELF);
                    message.setMessageTime(new Date().getTime());
                    chatMessages.add(message);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                            chatListView.setSelection(listAdapter.getCount() - 1);
                        }
                    });
                }
            }
        }, 500);

    }

    private Activity getActivity() {
        return this;
    }


    /**
     * Show or hide the emoji popup
     *
     * @param show
     */
    private void showEmojiPopup(boolean show) {
        showingEmoji = show;

        if (show) {
            if (emojiView == null) {
                if (getActivity() == null) {
                    return;
                }
                emojiView = new EmojiView(getActivity());

                emojiView.setListener(new EmojiView.Listener() {
                    public void onBackspace() {
                        chatEditText1.dispatchKeyEvent(new KeyEvent(0, 67));
                    }

                    public void onEmojiSelected(String symbol) {
                        int i = chatEditText1.getSelectionEnd();
                        if (i < 0) {
                            i = 0;
                        }
                        try {
                            CharSequence localCharSequence = Emoji.replaceEmoji(symbol, chatEditText1.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20));
                            chatEditText1.setText(chatEditText1.getText().insert(i, localCharSequence));
                            int j = i + localCharSequence.length();
                            chatEditText1.setSelection(j, j);
                        } catch (Exception e) {
                            Log.e(Constants.TAG, "Error showing emoji");
                        }
                    }
                });


                windowLayoutParams = new WindowManager.LayoutParams();
                windowLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
                if (Build.VERSION.SDK_INT >= 21) {
                    windowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
                } else {
                    windowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                    windowLayoutParams.token = getActivity().getWindow().getDecorView().getWindowToken();
                }
                windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            }

            final int currentHeight;

            if (keyboardHeight <= 0)
                keyboardHeight = App.getInstance().getSharedPreferences("emoji", 0).getInt("kbd_height", AndroidUtilities.dp(200));

            currentHeight = keyboardHeight;

            WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);

            windowLayoutParams.height = currentHeight;
            windowLayoutParams.width = AndroidUtilities.displaySize.x;

            try {
                if (emojiView.getParent() != null) {
                    wm.removeViewImmediate(emojiView);
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, e.getMessage());
            }

            try {
                wm.addView(emojiView, windowLayoutParams);
            } catch (Exception e) {
                Log.e(Constants.TAG, e.getMessage());
                return;
            }

            if (!keyboardVisible) {
                if (sizeNotifierRelativeLayout != null) {
                    sizeNotifierRelativeLayout.setPadding(0, 0, 0, currentHeight);
                }

                return;
            }

        } else {
            removeEmojiWindow();
            if (sizeNotifierRelativeLayout != null) {
                sizeNotifierRelativeLayout.post(new Runnable() {
                    public void run() {
                        if (sizeNotifierRelativeLayout != null) {
                            sizeNotifierRelativeLayout.setPadding(0, 0, 0, 0);
                        }
                    }
                });
            }
        }


    }


    /**
     * Remove emoji window
     */
    private void removeEmojiWindow() {
        if (emojiView == null) {
            return;
        }
        try {
            if (emojiView.getParent() != null) {
                WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
                wm.removeViewImmediate(emojiView);
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
        }
    }


    /**
     * Hides the emoji popup
     */
    public void hideEmojiPopup() {
        if (showingEmoji) {
            showEmojiPopup(false);
        }
    }

    /**
     * Check if the emoji popup is showing
     *
     * @return
     */
    public boolean isEmojiPopupShowing() {
        return showingEmoji;
    }


    /**
     * Updates emoji views when they are complete loading
     *
     * @param id
     * @param args
     */
    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.emojiDidLoaded) {
            if (emojiView != null) {
                emojiView.invalidateViews();
            }

            if (chatListView != null) {
                chatListView.invalidateViews();
            }
        }
    }

    @Override
    public void onSizeChanged(int height) {

        Rect localRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);

        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null) {
            return;
        }


        if (height > AndroidUtilities.dp(50) && keyboardVisible) {
            keyboardHeight = height;
            App.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }


        if (showingEmoji) {
            int newHeight = 0;

            newHeight = keyboardHeight;

            if (windowLayoutParams.width != AndroidUtilities.displaySize.x || windowLayoutParams.height != newHeight) {
                windowLayoutParams.width = AndroidUtilities.displaySize.x;
                windowLayoutParams.height = newHeight;

                wm.updateViewLayout(emojiView, windowLayoutParams);
                if (!keyboardVisible) {
                    sizeNotifierRelativeLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sizeNotifierRelativeLayout != null) {
                                sizeNotifierRelativeLayout.setPadding(0, 0, 0, windowLayoutParams.height);
                                sizeNotifierRelativeLayout.requestLayout();
                            }
                        }
                    });
                }
            }
        }


        boolean oldValue = keyboardVisible;
        keyboardVisible = height > 0;
        if (keyboardVisible && sizeNotifierRelativeLayout.getPaddingBottom() > 0) {
            showEmojiPopup(false);
        } else if (!keyboardVisible && keyboardVisible != oldValue && showingEmoji) {
            showEmojiPopup(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    /**
     * Get the system status bar height
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideEmojiPopup();
    }
}
