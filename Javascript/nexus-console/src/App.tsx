import { ThemeSync } from '@/components/ThemeSync'
import { AppShell } from '@/components/layout/AppShell'
import { Dashboard } from '@/pages/Dashboard'
import { PipelinesPage } from '@/pages/Pipelines'
import { SettingsPage } from '@/pages/Settings'
import { WorkloadsPage } from '@/pages/Workloads'
import { Navigate, Route, Routes } from 'react-router-dom'

export default function App() {
  return (
    <>
      <ThemeSync />
      <AppShell>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/workloads" element={<WorkloadsPage />} />
          <Route path="/pipelines" element={<PipelinesPage />} />
          <Route path="/settings" element={<SettingsPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AppShell>
    </>
  )
}
