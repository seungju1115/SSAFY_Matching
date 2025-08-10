import { useState, useEffect, FormEvent } from 'react';
import { useSocket } from '@/hooks/useSocket';
import { IMessage } from '@stomp/stompjs';

// A simple type for our chat messages
interface ChatMessage {
  sender: string;
  content: string;
  timestamp: string;
}

export default function Chat() {
  const { isConnected, subscribe, publish } = useSocket();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  
  // Let's assume a static room ID for this example.
  // In a real app, this would likely come from URL params or component props.
  const roomId = 'general'; 

  useEffect(() => {
    if (isConnected) {
      const unsubscribe = subscribe(
        `/topic/chat/room/${roomId}`,
        (message: IMessage) => {
          const receivedMessage: ChatMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        }
      );

      // Cleanup subscription on component unmount
      return () => {
        if (unsubscribe) {
          unsubscribe();
        }
      };
    }
  }, [isConnected, roomId, subscribe]);

  const handleSendMessage = (e: FormEvent) => {
    e.preventDefault();
    if (newMessage.trim() && isConnected) {
      const chatMessage = {
        roomId,
        sender: 'CurrentUser', // This should be dynamically set based on logged-in user
        content: newMessage,
      };
      publish(`/app/chat/message`, chatMessage);
      setNewMessage('');
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <h2>Chat Room: {roomId}</h2>
      <div style={{ marginBottom: '10px' }}>
        Connection Status: {isConnected ? 'Connected' : 'Disconnected'}
      </div>
      <div
        style={{
          height: '400px',
          border: '1px solid #ccc',
          overflowY: 'scroll',
          padding: '10px',
          marginBottom: '10px',
        }}
      >
        {messages.map((msg, index) => (
          <div key={index}>
            <strong>{msg.sender}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <form onSubmit={handleSendMessage}>
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Type a message..."
          style={{ width: '80%', padding: '8px' }}
          disabled={!isConnected}
        />
        <button type="submit" style={{ width: '18%', padding: '8px' }} disabled={!isConnected}>
          Send
        </button>
      </form>
    </div>
  );
} 