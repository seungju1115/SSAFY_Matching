import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Setup from './pages/Setup'
import ProfileSetup from './pages/ProfileSetup'
import MakeTeam from './pages/MakeTeam/make'

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
      </Routes>
    </BrowserRouter>
  )
}

export default App 