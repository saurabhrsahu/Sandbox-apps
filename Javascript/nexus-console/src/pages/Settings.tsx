import { Cpu, Gauge, Palette, Shield } from 'lucide-react'
import { useUI } from '@/stores/ui'
import type { ThemeMode } from '@/stores/ui'
import { Surface } from '@/components/ui/Surface'
import { cn } from '@/lib/cn'

const themes: { id: ThemeMode; label: string; detail: string }[] = [
  { id: 'system', label: 'System', detail: 'Match OS appearance' },
  { id: 'light', label: 'Light', detail: 'Glass-bright canvas' },
  { id: 'dark', label: 'Dark', detail: 'Low-glare operations' },
]

export function SettingsPage() {
  const { theme, setTheme } = useUI()

  return (
    <div className="space-y-8 max-w-3xl">
      <div>
        <p className="text-xs uppercase tracking-[0.2em] text-muted font-medium">Control plane</p>
        <h1 className="mt-1 text-3xl font-semibold tracking-tight">Console preferences</h1>
        <p className="mt-2 text-sm text-muted leading-relaxed">
          Visual density and fidelity checks — mirrored for CI screenshots and onboarding walkthroughs.
        </p>
      </div>

      <Surface>
        <div className="flex items-start gap-4">
          <div className="h-11 w-11 rounded-xl border border-border flex items-center justify-center bg-accent/10 text-accent">
            <Palette className="h-5 w-5" />
          </div>
          <div className="min-w-0 flex-1">
            <h2 className="text-base font-semibold tracking-tight">Appearance</h2>
            <p className="text-xs text-muted mt-1 mb-5">Themes use OKLCH tokens with calibrated contrast on both ramps.</p>
            <div className="grid sm:grid-cols-3 gap-3">
              {themes.map((t) => (
                <button
                  key={t.id}
                  type="button"
                  onClick={() => setTheme(t.id)}
                  className={cn(
                    'rounded-2xl border px-4 py-3 text-left transition-all',
                    'hover:border-accent/40 hover:bg-surface-elevated/60 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-accent/40',
                    theme === t.id
                      ? 'border-accent shadow-[var(--shadow-glow-accent)] bg-accent/5'
                      : 'border-border bg-surface/35',
                  )}
                >
                  <span className="block text-sm font-medium">{t.label}</span>
                  <span className="block text-[11px] text-muted mt-1">{t.detail}</span>
                </button>
              ))}
            </div>
          </div>
        </div>
      </Surface>

      <Surface>
        <div className="grid sm:grid-cols-3 gap-4">
          <div className="sm:col-span-2 space-y-2">
            <div className="flex items-center gap-2 font-medium text-sm">
              <Gauge className="h-4 w-4 text-accent" aria-hidden />
              Rendering profile
            </div>
            <p className="text-xs text-muted leading-relaxed">
              High-fidelity defaults: backdrop blur, gradient mesh, and animated saturation meters. Safe for local dev and
              storybook-quality demos.
            </p>
          </div>
          <div className="rounded-2xl border border-border bg-surface/40 px-4 py-3 flex items-center justify-between gap-3">
            <Shield className="h-9 w-9 text-accent/80" aria-hidden />
            <div className="text-right">
              <p className="font-mono text-xs text-muted">signed</p>
              <p className="text-sm font-medium">sandbox-apps</p>
            </div>
          </div>
        </div>
      </Surface>

      <Surface>
        <div className="flex items-start gap-4">
          <Cpu className="h-8 w-8 text-accent shrink-0 mt-1" aria-hidden />
          <div>
            <h2 className="text-base font-semibold tracking-tight">Runtime notes</h2>
            <p className="text-xs text-muted mt-2 leading-relaxed">
              Data on Command and Workloads is mocked with artificial latency to exercise loading states — swap the
              adapters in `src/data/api.ts` when wiring PulseBoard or other backends.
            </p>
          </div>
        </div>
      </Surface>
    </div>
  )
}
