export interface ChatMessageResponse {
  id: number;
  chatRoomId: number;
  senderId: number;
  message: string;
  createdAt: string; // ISO 8601 format date string, e.g., "2024-01-15T10:30:00"
}
 