import { create } from 'zustand'
import { persist } from 'zustand/middleware'

export type ThemeMode = 'dark' | 'light' | 'system'

type UIState = {
  sidebarOpen: boolean
  theme: ThemeMode
  setSidebarOpen: (open: boolean) => void
  toggleSidebar: () => void
  setTheme: (mode: ThemeMode) => void
}

export const useUI = create<UIState>()(
  persist(
    (set) => ({
      sidebarOpen: false,
      theme: 'system',
      setSidebarOpen: (open) => set({ sidebarOpen: open }),
      toggleSidebar: () => set((s) => ({ sidebarOpen: !s.sidebarOpen })),
      setTheme: (theme) => set({ theme }),
    }),
    { name: 'nexus-console-ui' },
  ),
)
