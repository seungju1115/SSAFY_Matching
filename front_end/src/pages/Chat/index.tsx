// src/pages/Chat/index.tsx
import { useEffect, useMemo, useRef, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import useUserStore from '@/stores/userStore';
import { chatAPI } from '@/api/chat';

export default function Chat() {
  const myId = useUserStore((s) => s.user?.id);

  // /chat?userId=2
  const [search] = useSearchParams();
  // const peerId = useMemo(() => {
  //   const v = search.get('userId');
  //   const n = v ? Number(v) : NaN;
  //   return Number.isFinite(n) ? n : undefined;
  // }, [search]);
  const peerId = 3
  const [status, setStatus] = useState<'idle'|'creating'|'success'|'error'>('idle');
  const [roomId, setRoomId] = useState<number | null>(null);
  const [error, setError] = useState<string>('');
  const runOnceRef = useRef(false);

  const createPrivate = async () => {
    if (!myId) { setStatus('error'); setError('로그인이 필요합니다.'); return; }
    if (!peerId) { setStatus('error'); setError('상대 userId가 없습니다. /chat?userId=2 로 접근하세요.'); return; }
    if (myId === peerId) { setStatus('error'); setError('상대 ID가 본인입니다.'); return; }

    try {
      setStatus('creating');
      const res = await chatAPI.createPrivateRoom({
        roomType: 'PRIVATE',
        user1Id: myId,
        user2Id: peerId,
      } as any);
      const created = res.data?.data as { roomId: number } | undefined;
      if (created?.roomId) {
        setRoomId(created.roomId);
        setStatus('success');
        console.log('%c[chat] ✅ 방 생성/입장 완료', 'color:#16a34a', { roomId: created.roomId, myId, peerId });
      } else {
        setStatus('error'); setError('응답에 roomId 없음');
      }
    } catch (e) {
      setStatus('error'); setError('방 생성 실패');
    }
  };

  // ✅ 컴포넌트 "내부"에서만 Hook 사용!
  useEffect(() => {
    if (runOnceRef.current) return;
    if (!myId || !peerId || myId === peerId) return;
    runOnceRef.current = true;
    createPrivate();
  }, [myId, peerId]);

  // 최소 상태 표시 (빈 화면 방지)
  return (
    <div style={{ padding: 16 }}>
      {status === 'idle' && <div>준비 중… (내 ID: {myId ?? '-'}, 상대 ID: {peerId ?? '-'})</div>}
      {status === 'creating' && <div>방 생성 중…</div>}
      {status === 'success' && <div>✅ 방 생성 완료! roomId: <b>{roomId}</b></div>}
      {status === 'error' && <div style={{ color: '#b00' }}>❌ {error}</div>}
    </div>
  );
}
