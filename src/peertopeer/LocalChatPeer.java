package peertopeer;

import de.tum.in.www1.jReto.LocalPeer;
import de.tum.in.www1.jReto.RemotePeer;
//import de.tum.in.www1.jReto.module.remoteP2P.RemoteP2PModule;
import de.tum.in.www1.jReto.module.wlan.WlanModule;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;



public class LocalChatPeer {

	private LocalPeer localPeer;

	private Map<RemotePeer, ChatRoom> chatPeers = new HashMap<>();

	private String displayName;
	private SimpleChatUI chatUI;
		
	public LocalChatPeer(SimpleChatUI chatUI, Executor mainThreadExecutor) {
		this.chatUI = chatUI;
		
		try {
			this.initializeLocalPeer(mainThreadExecutor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeLocalPeer(Executor executor) throws Exception {
        /**
        * Create a local peer with a WlanModule. To use the RemoteP2PModule, the RemoteP2P server needs to be deployed locally.
        */
		//RemoteP2PModule remoteModule = new RemoteP2PModule(new URI("ws://localhost:8080/"));
		WlanModule wlanModule = new WlanModule("SimpleP2PChat");
		this.localPeer = new LocalPeer(Arrays.asList(wlanModule), executor);
	}
	
    /**
    * Starts the local peer. 
    * When a peer is discovered, a ChatRoom with that peer is created, when one is lost, the corresponding ChatRoom is removed.
    */
	public void start(String displayName) {
		this.displayName = displayName;
		
		this.localPeer.start(
			peer -> createChatPeer(peer), 
			peer -> removeChatPeer(peer)
		);
	}
	
	public void createChatPeer(RemotePeer peer) {
		if (this.chatPeers.get(peer) != null) {
			System.err.println("We already have a chat peer for this peer!");
			return;
		}
		
		ChatRoom chatPeer = new ChatRoom(peer, this.displayName, this.chatUI);
		this.chatPeers.put(peer, chatPeer);
	}
	public void removeChatPeer(RemotePeer peer) {
		this.chatUI.removeChatPeer(this.chatPeers.get(peer));
		this.chatPeers.remove(peer);
	}
}