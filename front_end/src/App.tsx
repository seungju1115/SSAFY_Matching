import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Setup from './pages/Setup'
import ProfileSetup from './pages/ProfileSetup'
import MakeTeam from './pages/MakeTeam/make'
import AuthCallback from './pages/Auth/Callback'
import Team from './pages/Team'
import ChatPage from './pages/Chat'
import TeamChatPage from './pages/Team/Chat'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/setup" element={<Setup />} />
        <Route path="/profile-setup" element={<ProfileSetup />} />
        <Route path="/make-team" element={<MakeTeam />} />
        <Route path="/oauth/callback" element={<AuthCallback />} />
        <Route path="/team/:teamId" element={<Team />} />
        <Route path="/chat" element={<ChatPage />} />
        <Route path="/team/:teamId/chat" element={<TeamChatPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App