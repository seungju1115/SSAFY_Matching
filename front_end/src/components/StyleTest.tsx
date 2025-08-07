export function StyleTest() {
  return (
    <div className="p-4 space-y-4">
      <h1 className="text-4xl font-bold text-blue-600">
        🎨 스타일 테스트
      </h1>
      
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        ✅ 이 박스가 빨간 배경에 테두리가 있다면 <strong>Tailwind CSS가 작동</strong>하고 있습니다!
      </div>
      
      <div className="bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 text-white p-4 rounded-lg shadow-lg">
        🌈 그라데이션과 그림자가 보인다면 <strong>Tailwind CSS 완벽 작동</strong>!
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-blue-500 text-white p-4 rounded">박스 1</div>
        <div className="bg-green-500 text-white p-4 rounded">박스 2</div>
        <div className="bg-yellow-500 text-white p-4 rounded">박스 3</div>
      </div>
    </div>
  )
} 