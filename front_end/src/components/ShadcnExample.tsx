import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"

export function ShadcnExample() {
  return (
    <div className="p-8 space-y-8">
      <h1 className="text-3xl font-bold">shadcn/ui 컴포넌트 테스트</h1>
      
      <div className="space-y-4">
        <h2 className="text-2xl font-semibold">버튼들</h2>
        <div className="flex gap-4">
          <Button>기본 버튼</Button>
          <Button variant="secondary">보조 버튼</Button>
          <Button variant="outline">아웃라인 버튼</Button>
          <Button variant="destructive">위험 버튼</Button>
          <Button variant="ghost">고스트 버튼</Button>
          <Button variant="link">링크 버튼</Button>
        </div>
      </div>

      <div className="space-y-4">
        <h2 className="text-2xl font-semibold">입력 필드</h2>
        <div className="max-w-md space-y-2">
          <Input placeholder="이메일을 입력하세요" type="email" />
          <Input placeholder="비밀번호를 입력하세요" type="password" />
          <Input placeholder="비활성화된 입력" disabled />
        </div>
      </div>

      <div className="space-y-4">
        <h2 className="text-2xl font-semibold">카드</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Card>
            <CardHeader>
              <CardTitle>카드 제목</CardTitle>
              <CardDescription>
                이것은 카드 설명입니다. shadcn/ui의 Card 컴포넌트를 보여주고 있습니다.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p>카드의 주요 내용이 여기에 들어갑니다.</p>
            </CardContent>
            <CardFooter>
              <Button>액션</Button>
            </CardFooter>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>또 다른 카드</CardTitle>
              <CardDescription>
                다양한 스타일의 카드를 만들 수 있습니다.
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                <Input placeholder="카드 안의 입력 필드" />
                <Button variant="outline" className="w-full">
                  전체 너비 버튼
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
} 