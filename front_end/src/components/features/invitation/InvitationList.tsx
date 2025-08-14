import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Bell, Users } from "lucide-react"
import InvitationTeamCard from "./InvitationTeamCard"
import type { Team } from "../home/TeamSection"

interface InvitationListProps {
  invitations: Team[]
  onAccept: (teamId: number) => void
  onReject: (teamId: number) => void
  isLoading?: boolean
  onViewTeam?: (teamId: number) => void
  onNavigateToTeams?: () => void
  onNavigateToMakeTeam?: () => void
}

export default function InvitationList({
  invitations,
  onAccept,
  onReject,
  isLoading = false,
  onViewTeam,
  onNavigateToTeams,
  onNavigateToMakeTeam
}: InvitationListProps) {
  return (
    <div className="space-y-4">
      {invitations.length > 0 ? (
        <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4 sm:gap-6">
          {invitations.map((invitation) => (
            <InvitationTeamCard
              key={invitation.id}
              team={invitation}
              onAccept={onAccept}
              onReject={onReject}
              isLoading={isLoading}
              onViewTeam={onViewTeam}
            />
          ))}
        </div>
      ) : (
        <Card className="bg-white border border-gray-100 rounded-lg shadow-sm">
          <CardContent className="py-12 text-center">
            <Bell className="h-12 w-12 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">받은 초대가 없습니다</h3>
            <p className="text-gray-500 mb-6">
              아직 받은 팀 초대가 없어요. 팀을 찾아보거나 직접 팀을 만들어보세요!
            </p>
            <div className="flex flex-col sm:flex-row gap-3 justify-center">
              <Button onClick={onNavigateToTeams}>
                <Users className="h-4 w-4 mr-2" />
                팀 찾기
              </Button>
              <Button variant="outline" onClick={onNavigateToMakeTeam}>
                팀 만들기
              </Button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
