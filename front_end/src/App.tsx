import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Setup from './pages/Setup'
import ProfileSetup from './pages/ProfileSetup'
import MakeTeam from './pages/MakeTeam/make'
import AuthCallback from './pages/Auth/Callback'

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
      </Routes>
    </BrowserRouter>
  )
}

export default App 