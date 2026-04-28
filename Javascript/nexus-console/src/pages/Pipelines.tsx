import { useQuery } from '@tanstack/react-query'
import { CheckCircle2, Circle, Loader2, XCircle } from 'lucide-react'
import { Badge } from '@/components/ui/Badge'
import { Surface } from '@/components/ui/Surface'
import { fetchPipelines, type PipelineRun } from '@/data/api'
import { cn } from '@/lib/cn'

function relativeAgo(iso: string): string {
  const diffMs = Date.now() - new Date(iso).getTime()
  const mins = Math.max(1, Math.floor(diffMs / 60000))
  if (mins < 60) return `${mins}m ago`
  const hrs = Math.floor(mins / 60)
  if (hrs < 48) return `${hrs}h ago`
  return new Date(iso).toLocaleString(undefined, {
    dateStyle: 'medium',
    timeStyle: 'short',
  })
}

export function PipelinesPage() {
  const { data, error, isPending } = useQuery({
    queryKey: ['pipelines'],
    queryFn: fetchPipelines,
  })

  if (error) {
    return (
      <Surface>
        <p className="text-danger text-sm font-medium">Could not load pipeline activity.</p>
      </Surface>
    )
  }

  const runs = data ?? []

  return (
    <div className="space-y-6">
      <div>
        <p className="text-xs uppercase tracking-[0.2em] text-muted font-medium">Progressive delivery</p>
        <h1 className="mt-1 text-3xl font-semibold tracking-tight">Lane execution</h1>
        <p className="mt-2 max-w-2xl text-sm text-muted leading-relaxed">
          Multi-stage gates with live state — designed for incident review and change boards.
        </p>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-5">
        {isPending
          ? Array.from({ length: 3 }).map((_, i) => (
              <Surface key={`sk-${i}`} className="h-56 animate-pulse bg-surface-elevated/40" bleed />
            ))
          : runs.map((run) => <PipelineCard key={run.id} run={run} />)}
      </div>
    </div>
  )
}

function PipelineCard({ run }: { run: PipelineRun }) {
  const statusBadge =
    run.status === 'healthy' ? (
      <Badge variant="success">Healthy</Badge>
    ) : run.status === 'degraded' ? (
      <Badge variant="warning">Degraded</Badge>
    ) : (
      <Badge variant="danger">Failed</Badge>
    )

  return (
    <Surface className="flex flex-col gap-5">
      <div className="flex flex-wrap items-start justify-between gap-3">
        <div>
          <p className="font-mono text-xs text-muted">{run.id}</p>
          <h2 className="text-lg font-semibold tracking-tight mt-1">{run.name}</h2>
          <p className="text-xs text-muted mt-1">Started {relativeAgo(run.startedAt)}</p>
        </div>
        {statusBadge}
      </div>

      <div>
        <ol className="grid grid-cols-2 sm:grid-cols-4 gap-6">
          {run.stages.map((stage, idx) => (
            <li key={`${run.id}-${stage.name}-${idx}`}>
              <div className="flex flex-col items-center text-center gap-2">
                <StageIcon state={stage.state} />
                <span className="text-[11px] uppercase tracking-[0.12em] text-muted font-medium">
                  {stage.name}
                </span>
                <Badge variant={stage.state === 'failed' ? 'danger' : stage.state === 'active' ? 'accent' : 'muted'}>
                  {stage.state}
                </Badge>
              </div>
            </li>
          ))}
        </ol>
      </div>
    </Surface>
  )
}

function StageIcon({ state }: { state: PipelineRun['stages'][number]['state'] }) {
  const Icon =
    state === 'done'
      ? CheckCircle2
      : state === 'active'
        ? Loader2
        : state === 'failed'
          ? XCircle
          : Circle
  const className =
    state === 'done'
      ? 'text-success'
      : state === 'active'
        ? 'text-accent'
        : state === 'failed'
          ? 'text-danger'
          : 'text-muted'
  const spin = state === 'active'
  return (
    <div className="inline-flex rounded-full bg-surface-elevated border border-border p-1 shadow-[var(--shadow-glass)]">
      <Icon className={cn('h-8 w-8', className, spin && 'animate-spin [animation-duration:1.8s]')} />
    </div>
  )
}
