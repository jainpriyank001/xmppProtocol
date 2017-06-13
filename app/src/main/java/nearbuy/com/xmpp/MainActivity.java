package nearbuy.com.xmpp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "email: priyank.jain@nearbuy.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MyLoginTask task = new MyLoginTask();
        task.execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyLoginTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            // Create a connection to the jabber.org server.
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("user1", "12345")
                    .setHost("172.16.102.155")
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setServiceName("localhost")
                    .setPort(5222)
                    .setDebuggerEnabled(true) // to view what's happening in detail
                    .build();

            final AbstractXMPPConnection conn1 = new XMPPTCPConnection(config);

            try {
                conn1.connect();
                System.out.println("Start");

                if(conn1.isConnected()) {
                    //System.out.println("Conn Done");
                    Log.i("app", "conn done");
                }
                conn1.login();

                if(conn1.isAuthenticated()) {
                    Log.i("app", "Auth done");
                    ChatManager chatManager = ChatManager.getInstanceFor(conn1);


                    chatManager.addChatListener(
                            new ChatManagerListener() {
                                @Override
                                public void chatCreated(Chat chat, boolean createdLocally)
                                {
                                    chat.addMessageListener(new ChatMessageListener()
                                    {
                                        @Override
                                        public void processMessage(Chat chat, Message message) {
                                            System.out.println("Received message: "
                                                    + (message != null ? message.getBody() : "NULL"));
                                        }
                                    });

                                    Log.i("app", chat.toString());
                                }
                            });
                }
            }
            catch (Exception e) {
                Log.i("app", e.toString());
            }

            return "Chat Okay";
        }


        @Override
        protected void onPostExecute(String result) {
        }

    }
}
