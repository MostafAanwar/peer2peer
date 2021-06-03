package peertopeer;

import de.tum.in.www1.jReto.Connection;
import de.tum.in.www1.jReto.RemotePeer;
import de.tum.in.www1.jReto.connectivity.InTransfer;
import de.tum.in.www1.jReto.connectivity.OutTransfer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
Each ChatRoom represents a chat conversation with the peer it represents.
For simplicity, when a chat room is created, a connection is created to the remote peer, which is used to send chat messages.
This means that both participating peers create a connection, i.e. there are two connections between the peers, even though one would be enough.
Files can be exchanged in using the ChatRoom class. To transmit a file, a new connection is established. First, the file name is transmitted, then the actual file.
After transmission, the connection is closed.
Therefore, the first incoming connection from a remote peer is used to receive chat messages; any further connections are used to receive files.
*/
public class ChatRoom {
	private SimpleChatUI chatUI;
	
    /** The display name of the local peer in the chat */
	private String localDisplayName;
    /** The display name of the remote peer in the chat */
	private String remoteDisplayName;
    /** The full text in the chat room; contains all messages. */
	private String chatText = "";
    /** The progress of a file if it one is being transmitted. */

    /** The remotePeer object representing the other peer in the chat room (besides the local peer) */
	private RemotePeer remotePeer;
	/** The Connection used to receive chat messages. */
	private Connection incomingConnection;
    /** The Connection used to send chat messages. */
	private Connection outgoingConnection;
	
	public ChatRoom(RemotePeer remotePeer, String localDisplayName, SimpleChatUI chatUI) {
		this.localDisplayName = localDisplayName;
		this.chatUI = chatUI;
		this.remotePeer = remotePeer;
		
        // When an incoming connection is available, call acceptConnection.
		this.remotePeer.setIncomingConnectionHandler((peer, connection) -> this.acceptIncomingConnection(connection));
		
        // Create a connection to the remote peer
		this.outgoingConnection = remotePeer.connect();
        // The first message sent through the outgoing connection contains the display name that should be used, so it is sent here.
		this.outgoingConnection.send(new TextMessage(localDisplayName).serialize());
	}
	
	private void acceptIncomingConnection(Connection connection) {
		if (this.incomingConnection == null) {
            // If this is the first connection, we use it to receive message data. Therefore we call handleMessageData when data was received.
			this.incomingConnection = connection;
			this.incomingConnection.setOnData((t, data) -> handleMessageData(data));
		}
	}
	public void sendMessage(String message) {
		this.outgoingConnection.send(new TextMessage(message).serialize());
		appendChatMessage(this.localDisplayName, message);
	}
	private void handleMessageData(ByteBuffer data) {	
		String message = new TextMessage(data).text;
		
		if (this.remoteDisplayName == null) {
			this.setDisplayName(message);
		} else {
			appendChatMessage(this.remoteDisplayName, message);
		}
	}
	private void appendChatMessage(String displayName, String message) {
		this.chatText += displayName+": "+ message+"\n";
		chatUI.updateChatData();
	}
	


	
	private void setDisplayName(String displayName) {
		this.remoteDisplayName = displayName;
		chatUI.addChatPeer(this);
	}
	public String getDisplayName() {
		return this.remoteDisplayName;
	}
	public String getChatText() {
		return this.chatText;
	}
	
}