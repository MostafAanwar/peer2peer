# Peer2Peer Chat Application

A lightweight peer-to-peer chat system built in Java. This project demonstrates the fundamentals of socket programming, object serialization, and multithreading while providing a simple Swing-based user interface for local communication between peers.

## 📌 Features

- Local peer-to-peer chat over sockets
- Real-time message exchange using serialized objects
- Multithreaded handling of multiple peer connections
- Basic GUI using Java Swing
- Modular architecture for easy extension and maintenance

## 📁 Project Structure

src/ └── peertopeer/ 
├── ChatRoom.java # Handles message exchange between peers 
├── LocalChatPeer.java # Entry point, starts peer and listens for connections 
├── SimpleChatUI.java # Swing UI for message input/output 
  └── TextMessage.java # Serializable message data class

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/MostafAanwar/peer2peer.git
   cd peer2peer/src
2. Compile the project:
Run a peer instance:
  javac peertopeer/*.java
3. Run a peer instance:
  java peertopeer.LocalChatPeer
