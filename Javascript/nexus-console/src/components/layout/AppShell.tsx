import { AnimatePresence, motion } from 'framer-motion'
import {
  Bell,
  Cpu,
  GitBranch,
  LayoutDashboard,
  Menu,
  Search,
  Settings2,
} from 'lucide-react'
import { type PropsWithChildren, useMemo } from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import { useUI } from '@/stores/ui'
import { Badge } from '@/components/ui/Badge'
import { cn } from '@/lib/cn'

const nav = [
  { to: '/', label: 'Command', icon: LayoutDashboard },
  { to: '/workloads', label: 'Workloads', icon: Cpu },
  { to: '/pipelines', label: 'Pipelines', icon: GitBranch },
  { to: '/settings', label: 'Control', icon: Settings2 },
] as const

export function AppShell({
  children,
}: PropsWithChildren) {
  const location = useLocation()
  const { sidebarOpen, setSidebarOpen, toggleSidebar } = useUI()
  const title = useMemo(() => {
    const hit = nav.find((n) => n.to === location.pathname)
    return hit?.label ?? 'Nexus'
  }, [location.pathname])

  return (
    <div className="min-h-svh flex text-foreground">
      {/* Desktop rail */}
      <aside
        className={cn(
          'relative z-40 hidden lg:flex lg:flex-col border-r border-border bg-surface/45 backdrop-blur-xl transition-[width] duration-300',
          sidebarOpen ? 'w-64' : 'w-[72px]',
        )}
      >
        <div
          className={cn(
            'flex items-center gap-3 px-4 py-6 border-b border-border',
            sidebarOpen ? 'justify-start' : 'justify-center',
          )}
        >
          <div className="h-11 w-11 rounded-xl bg-gradient-to-br from-accent via-fuchsia-500 to-violet-500 shadow-[var(--shadow-glow-accent)] flex items-center justify-center text-accent-foreground font-semibold shrink-0">
            NX
          </div>
          {sidebarOpen ? (
            <div className="leading-tight">
              <p className="text-sm font-semibold tracking-tight">Nexus Console</p>
              <p className="text-xs text-muted">Global control plane</p>
            </div>
          ) : null}
        </div>

        <nav className="flex-1 px-2 py-4 space-y-1">
          {nav.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                cn(
                  'group flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-accent/15 text-foreground ring-1 ring-accent/25'
                    : 'text-muted hover:text-foreground hover:bg-surface-elevated/70',
                  !sidebarOpen && 'justify-center',
                )
              }
            >
              <item.icon className="h-[18px] w-[18px] shrink-0" />
              {sidebarOpen ? <span>{item.label}</span> : null}
            </NavLink>
          ))}
        </nav>

        <div className="p-4 border-t border-border text-[11px] text-muted leading-relaxed font-mono">
          {sidebarOpen ? (
            <>
              Release <span className="text-accent">REL-9264</span>
              <br />
              <span className="opacity-80">Integrity chain verified</span>
            </>
          ) : (
            <span className="block text-center text-[10px]">9264</span>
          )}
        </div>
      </aside>

      {/* Mobile sidebar */}
      <AnimatePresence initial={false}>
        {sidebarOpen ? (
          <motion.aside
            key="mob"
            initial={{ x: -320, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            exit={{ x: -320, opacity: 0 }}
            transition={{ type: 'spring', stiffness: 320, damping: 36 }}
            className="fixed inset-y-0 left-0 z-50 w-[min(88vw,320px)] border-r border-border bg-surface/90 backdrop-blur-2xl lg:hidden"
          >
            <div className="flex items-center justify-between px-4 py-5 border-b border-border">
              <div className="flex items-center gap-3">
                <div className="h-10 w-10 rounded-xl bg-gradient-to-br from-accent via-fuchsia-500 to-violet-500 flex items-center justify-center font-semibold text-accent-foreground">
                  NX
                </div>
                <div className="leading-tight">
                  <p className="text-sm font-semibold tracking-tight">Nexus Console</p>
                  <Badge variant="muted">Observability surface</Badge>
                </div>
              </div>
            </div>
            <nav className="px-2 py-3 space-y-1">
              {nav.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  onClick={() => setSidebarOpen(false)}
                  className={({ isActive }) =>
                    cn(
                      'flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-colors',
                      isActive ? 'bg-accent/15 ring-1 ring-accent/25' : 'text-muted hover:text-foreground',
                    )
                  }
                >
                  <item.icon className="h-[18px] w-[18px]" />
                  {item.label}
                </NavLink>
              ))}
            </nav>
          </motion.aside>
        ) : null}
      </AnimatePresence>

      {sidebarOpen ? (
        <button
          type="button"
          aria-label="Close menu"
          className="fixed inset-0 z-40 bg-black/40 backdrop-blur-[2px] lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      ) : null}

      <div className="flex-1 flex flex-col min-w-0">
        <header className="sticky top-0 z-30 border-b border-border bg-canvas/70 backdrop-blur-xl">
          <div className="flex items-center gap-3 px-4 sm:px-6 py-3.5">
            <button
              type="button"
              className="inline-flex h-10 w-10 items-center justify-center rounded-xl border border-border bg-surface/60 hover:bg-surface text-foreground lg:hidden"
              onClick={() => toggleSidebar()}
              aria-label="Toggle navigation"
            >
              <Menu className="h-5 w-5" />
            </button>
            <div className="hidden lg:flex items-center gap-2 text-sm text-muted">
              <span className="font-mono text-xs tracking-wide">GLOBAL</span>
              <span className="opacity-40">/</span>
              <span className="text-foreground font-medium">{title}</span>
            </div>
            <div className="flex-1 max-w-xl hidden sm:block">
              <label className="relative block">
                <Search className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted" />
                <input
                  className="w-full rounded-xl border border-border bg-surface/60 pl-10 pr-3 py-2.5 text-sm outline-none focus:ring-2 focus:ring-accent/40 placeholder:text-muted"
                  placeholder="Search cells, releases, guardrails…"
                  type="search"
                />
              </label>
            </div>
            <div className="ml-auto flex items-center gap-2">
              <Badge variant="success">SLO healthy</Badge>
              <button
                type="button"
                className="relative inline-flex h-10 w-10 items-center justify-center rounded-xl border border-border bg-surface/60 hover:bg-surface"
                aria-label="Notifications"
              >
                <Bell className="h-[18px] w-[18px]" />
                <span className="absolute top-2 right-2 h-2 w-2 rounded-full bg-accent shadow-[0_0_0_4px_var(--color-canvas)]" />
              </button>
            </div>
          </div>
        </header>

        <main className="flex-1 px-4 sm:px-6 py-6 lg:py-8 max-w-[1400px] w-full mx-auto">
          {children}
        </main>
      </div>
    </div>
  )
}
