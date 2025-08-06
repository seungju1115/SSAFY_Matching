import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Setup from './pages/Setup'
import ProfileSetup from './pages/ProfileSetup'
import MakeTeam from './pages/MakeTeam/make'
import OAuthCallback from './pages/OAuthCallback'

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
        <Route path="/oauth/callback" element={<OAuthCallback />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App 