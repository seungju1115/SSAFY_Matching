import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent } from '@/components/ui/card'

import { 
  Trash2, 
  Plus, 
  Award, 
  AlertCircle 
} from 'lucide-react'

interface Certification {
  id: string
  name: string
}

interface CertificationInputProps {
  certifications: Certification[]
  onChange: (certifications: Certification[]) => void
  maxCertifications?: number
}

export default function CertificationInput({ 
  certifications, 
  onChange, 
  maxCertifications = 3 
}: CertificationInputProps) {
  const [name, setName] = useState('')

  // 자격증 추가
  const addCertification = () => {
    if (!name.trim()) return
    if (certifications.length >= maxCertifications) return

    const newCertification: Certification = {
      id: Date.now().toString(),
      name: name.trim(),
    }

    onChange([...certifications, newCertification])
    resetForm()
  }

  // 자격증 제거
  const removeCertification = (id: string) => {
    onChange(certifications.filter(cert => cert.id !== id))
  }

  // 폼 초기화
  const resetForm = () => {
    setName('')
  }

  // 자격증 추가 가능 여부
  const canAddMore = certifications.length < maxCertifications
  
  // 폼 유효성 검사
  const isFormValid = name.trim()

  return (
    <div className="space-y-6">
      {/* 자격증 목록 */}
      <div className="space-y-4">
        {certifications.map((cert) => (
          <Card key={cert.id} className="bg-white border border-gray-200">
            <CardContent className="p-4">
              <div className="flex justify-between items-start">
                <div className="space-y-2">
                  <div className="flex items-center gap-2">
                    <Award className="h-5 w-5 text-orange-500" />
                    <h3 className="font-medium">{cert.name}</h3>
                  </div>
                </div>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => removeCertification(cert.id)}
                  className="text-red-500 hover:text-red-700 hover:bg-red-50"
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}

        {certifications.length === 0 && (
          <div className="text-center py-8 border border-dashed rounded-md bg-gray-50">
            <Award className="h-10 w-10 text-gray-300 mx-auto mb-2" />
            <p className="text-gray-500">등록된 자격증이 없습니다</p>
            <p className="text-sm text-gray-400 mt-1">최대 {maxCertifications}개까지 등록 가능합니다</p>
          </div>
        )}
      </div>

      {/* 자격증 추가 폼 */}
      {canAddMore ? (
        <div className="space-y-4 bg-orange-50 p-4 rounded-md">
          <h3 className="font-medium text-orange-800 flex items-center gap-2">
            <Award className="h-5 w-5" />
            자격증 추가
          </h3>
          
          <div className="space-y-3">
            <Card>
              <CardContent className="p-4 space-y-4">
                <Input 
                  placeholder="자격증명 (예: 정보처리기사)"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </CardContent>
            </Card>
          </div>
          
          <div className="pt-2">
            <Button
              onClick={addCertification}
              disabled={!isFormValid}
              className="w-full bg-orange-500 hover:bg-orange-600"
            >
              <Plus className="h-4 w-4 mr-2" />
              자격증 추가
            </Button>
          </div>
        </div>
      ) : (
        <div className="bg-orange-50 p-4 rounded-md flex items-center gap-3">
          <AlertCircle className="h-5 w-5 text-orange-500" />
          <p className="text-sm text-orange-700">
            자격증은 최대 {maxCertifications}개까지 등록 가능합니다.
          </p>
        </div>
      )}

      {/* 도움말 */}
      <div className="bg-gray-50 p-4 rounded-md">
        <h3 className="text-sm font-medium text-gray-700 mb-2">자격증 등록 팁</h3>
        <ul className="list-disc pl-5 space-y-1">
          <li className="text-sm text-gray-600">
            개발 관련 자격증이나 수료증을 등록해보세요.
          </li>
          <li className="text-sm text-gray-600">
            정확한 자격증명과 발급기관을 입력해주세요.
          </li>
          <li className="text-sm text-gray-600">
            최신 취득 자격증부터 등록하는 것이 좋습니다.
          </li>
        </ul>
      </div>
    </div>
  )
}