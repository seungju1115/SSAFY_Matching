import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import { 
  Mail, 
  Calendar, 
  BookOpen, 
  Edit3
} from 'lucide-react'
import useUserStore from '@/stores/userStore'

// 프로필 정보 페이지
export default function ProfileInfo() {
  const { user } = useUserStore()

  // 전공/비전공 라벨 반환
  const getMajorLabel = (isMajor: boolean, major?: string) => {
    if (isMajor) {
      return major === 'java' ? 'Java 전공' : '임베디드 전공'
    }
    return major === 'python' ? 'Python 비전공' : 'Java 비전공'
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">개인 정보</h2>
        <p className="text-gray-600">회원님의 기본 정보를 확인하고 수정할 수 있습니다.</p>
      </div>
      
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <span>기본 정보</span>
            <Button variant="outline" size="sm">
              <Edit3 className="h-4 w-4 mr-2" />
              수정
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center space-x-3">
            <Mail className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-sm text-gray-500">이메일</p>
              <p className="font-medium">{user?.email || 'user@ssafy.com'}</p>
            </div>
          </div>
          <Separator />
          <div className="flex items-center space-x-3">
            <Calendar className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-sm text-gray-500">기수 및 반</p>
              <p className="font-medium">2학기 5반</p>
            </div>
          </div>
          <Separator />
          <div className="flex items-center space-x-3">
            <BookOpen className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-sm text-gray-500">전공</p>
              <p className="font-medium">{getMajorLabel(true, 'java')}</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
