package peertopeer;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;

public class SimpleChatUI {
	private LocalChatPeer chatPeer;
	List chatPeerList;
	StyledText chatText;
	  private final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	protected Shell shlSimpleChatExample;
	private Text txtDisplayName;
	private ChatRoom selectedPeer;
	private  ArrayList<ChatRoom> chatPeers = new ArrayList<>();
	
	private Executor swtExecutor = new Executor() {
		@Override
		public synchronized void execute(Runnable command) {
			Display.getDefault().syncExec(command);
		}
	};
	private Text txtMessage;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SimpleChatUI window = new SimpleChatUI();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		chatPeer = new LocalChatPeer(SimpleChatUI.this, swtExecutor);

		shlSimpleChatExample.open();
		shlSimpleChatExample.layout();
		while (!shlSimpleChatExample.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void startLocalPeer() {
		chatPeer.start(txtDisplayName.getText());
		
	}

	public void addChatPeer(ChatRoom chatPeer) {
		this.chatPeers.add(chatPeer);
		
		this.chatPeerList.add(chatPeer.getDisplayName());
		for(ChatRoom peer :chatPeers) {
			peer.sendMessage("New peer");
		}
	}
	public void removeChatPeer(ChatRoom chatPeer) {
		this.chatPeers.remove(chatPeer);
		this.chatPeerList.removeAll();
		
		for (ChatRoom peer : this.chatPeers) {
			this.chatPeerList.add(peer.getDisplayName());
		}
	}
	
	
	public void selectChatPeer(int index) {
		if (index == -1 || index >= this.chatPeers.size()) {
			this.selectedPeer = null;
		} else {
			this.selectedPeer = this.chatPeers.get(index);
			this.updateChatData();
		}
	}
	
	public void updateChatData() {
		if (this.selectedPeer == null) return;
		
		this.chatText.setText(this.selectedPeer.getChatText());
	}
	
public void test() {

	
    final long maxSleepTime=20000L;
    for (int i = 0; i < 10; i++) {
       
    	
        Runnable command=new Runnable() {
            @Override public void run() {
            	for(ChatRoom peer :chatPeers) {
        			peer.sendMessage("fake data");
        		}
            }
        };
        executor.scheduleAtFixedRate(command,maxSleepTime,maxSleepTime, TimeUnit.MILLISECONDS);
    }
}
	
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSimpleChatExample = new Shell();
		shlSimpleChatExample.setSize(621, 496);
		shlSimpleChatExample.setText("Simple Chat Example");
		shlSimpleChatExample.setLayout(null);
		
		this.chatPeerList = new List(shlSimpleChatExample, SWT.BORDER);
		chatPeerList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectChatPeer(chatPeerList.getSelectionIndex());
			}
		});
		chatPeerList.setBounds(10, 66, 243, 332);
		
		txtDisplayName = new Text(shlSimpleChatExample, SWT.BORDER);
		txtDisplayName.setText("Enter a Display Name");
		txtDisplayName.setBounds(10, 10, 148, 24);
		
		Button btnNewButton = new Button(shlSimpleChatExample, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startLocalPeer();
				test();
			}
			
		});
		btnNewButton.setBounds(168, 6, 243, 34);
		btnNewButton.setText("Start Advertising and Browsing");
		
		this.chatText = new StyledText(shlSimpleChatExample, SWT.BORDER);
		chatText.setDoubleClickEnabled(false);
		chatText.setEnabled(false);
		chatText.setEditable(false);
		chatText.setBounds(259, 66, 348, 332);
		
		txtMessage = new Text(shlSimpleChatExample, SWT.BORDER);
		txtMessage.setText("Type a message");
		txtMessage.setBounds(259, 404, 269, 23);
		
		Button btnSend = new Button(shlSimpleChatExample, SWT.NONE);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedPeer != null) {
					selectedPeer.sendMessage(txtMessage.getText());
				}
			}
		});
		btnSend.setBounds(534, 404, 73, 24);
		btnSend.setText("Send");
		
		Label lblPeers = new Label(shlSimpleChatExample, SWT.NONE);
		lblPeers.setBounds(10, 46, 243, 14);
		lblPeers.setText("Discovered Peers");
		
		Label lblChat = new Label(shlSimpleChatExample, SWT.NONE);
		lblChat.setEnabled(false);
		lblChat.setBounds(259, 46, 348, 14);
		lblChat.setText("Chat with Selected Peer");  
		
			  


	}
	public class MyRunnable implements Runnable {

	    private int var;

	    public MyRunnable(int var) {
	        this.var = var;
	    }

	    public void run() {
	        // code in the other thread, can reference "var" variable
	    }
	}

}
