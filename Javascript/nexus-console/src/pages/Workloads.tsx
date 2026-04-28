import { useMemo, useState } from 'react'
import { keepPreviousData, useQuery } from '@tanstack/react-query'
import {
  ArrowDown,
  ArrowUp,
  ArrowUpDown,
} from 'lucide-react'
import { motion } from 'framer-motion'
import { fetchShards, type ShardRow } from '@/data/api'
import { Badge } from '@/components/ui/Badge'
import { Surface } from '@/components/ui/Surface'
import { cn } from '@/lib/cn'

type SortKey = 'region' | 'saturation' | 'queueDepth' | 'version'

export function WorkloadsPage() {
  const { data, error, isPending } = useQuery({
    queryKey: ['shards'],
    queryFn: fetchShards,
    placeholderData: keepPreviousData,
  })

  const [sort, setSort] = useState<{ key: SortKey; dir: 'asc' | 'desc' }>({
    key: 'saturation',
    dir: 'desc',
  })

  const rows = useMemo(() => {
    const list = data ?? []
    const mult = sort.dir === 'asc' ? 1 : -1
    return [...list].sort((a, b) => {
      if (sort.key === 'region') return a.region.localeCompare(b.region) * mult
      if (sort.key === 'version') return a.version.localeCompare(b.version) * mult
      if (sort.key === 'queueDepth') return (a.queueDepth - b.queueDepth) * mult
      return (a.saturation - b.saturation) * mult
    })
  }, [data, sort])

  const toggle = (key: SortKey) => {
    setSort((s) =>
      s.key === key ? { key, dir: s.dir === 'asc' ? 'desc' : 'asc' } : { key, dir: 'desc' },
    )
  }

  if (error) {
    return (
      <Surface>
        <p className="text-danger text-sm font-medium">Could not load shard inventory.</p>
      </Surface>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <p className="text-xs uppercase tracking-[0.2em] text-muted font-medium">Capacity graph</p>
        <h1 className="mt-1 text-3xl font-semibold tracking-tight">Regional shards</h1>
        <p className="mt-2 max-w-2xl text-sm text-muted leading-relaxed">
          Live saturation and queue depth across execution cells — sortable. Data is mocked for high-fidelity UI practice.
        </p>
      </div>

      <Surface bleed className="overflow-x-auto">
        <table className="min-w-[760px] w-full text-sm">
          <thead className="bg-surface-elevated/60 border-b border-border">
            <tr className="text-left text-xs uppercase tracking-[0.1em] text-muted">
              <th className="px-6 py-3 font-medium">
                <SortButton
                  sortKey="region"
                  sort={sort}
                  label="Shard"
                  onClick={() => toggle('region')}
                />
              </th>
              <th className="px-6 py-3 font-medium">
                Role
              </th>
              <th className="px-6 py-3 font-medium">
                <SortButton
                  sortKey="saturation"
                  sort={sort}
                  label="Saturation"
                  onClick={() => toggle('saturation')}
                />
              </th>
              <th className="px-6 py-3 font-medium">
                <SortButton
                  sortKey="queueDepth"
                  sort={sort}
                  label="Queue"
                  onClick={() => toggle('queueDepth')}
                />
              </th>
              <th className="px-6 py-3 font-medium">
                <SortButton
                  sortKey="version"
                  sort={sort}
                  label="Release train"
                  onClick={() => toggle('version')}
                />
              </th>
            </tr>
          </thead>
          <tbody>
            {isPending
              ? Array.from({ length: 4 }).map((_, idx) => (
                  <tr key={`sk-${idx}`} className="border-t border-border">
                    <td colSpan={5} className="px-6 py-4">
                      <div className="h-11 rounded-xl bg-surface/60 animate-pulse" />
                    </td>
                  </tr>
                ))
              : rows.map((row) => (
                  <motion.tr key={row.id} layout initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
                    <td className="px-6 py-4 font-medium text-foreground">{row.id}</td>
                    <td className="px-6 py-4">
                      <RoleBadge role={row.role} />
                    </td>
                    <td className="px-6 py-4">
                      <SaturationMeter value={row.saturation} />
                    </td>
                    <td className="px-6 py-4 tabular-nums text-muted">{row.queueDepth}</td>
                    <td className="px-6 py-4 font-mono text-xs text-muted">{row.version}</td>
                  </motion.tr>
                ))}
          </tbody>
        </table>
      </Surface>
    </div>
  )
}

function SortButton({
  label,
  onClick,
  sortKey,
  sort,
}: {
  label: string
  onClick: () => void
  sortKey: SortKey
  sort: { key: SortKey; dir: 'asc' | 'desc' }
}) {
  const active = sort.key === sortKey
  const Icon = !active ? ArrowUpDown : sort.dir === 'asc' ? ArrowUp : ArrowDown
  return (
    <button
      type="button"
      onClick={onClick}
      className={cn(
        'inline-flex items-center gap-2 rounded-lg px-2 py-1 -mx-2 -my-1',
        'hover:bg-surface/80 transition-colors',
      )}
    >
      {label}
      <Icon className={cn('h-3.5 w-3.5', active ? 'opacity-100' : 'opacity-35')} aria-hidden />
    </button>
  )
}

function RoleBadge({ role }: { role: ShardRow['role'] }) {
  const label =
    role === 'primary' ? 'Primary' : role === 'replica' ? 'Replica' : 'Staging'
  const variant =
    role === 'primary' ? ('accent' as const) : role === 'replica' ? ('success' as const) : ('warning' as const)
  return <Badge variant={variant}>{label}</Badge>
}

function SaturationMeter({ value }: { value: number }) {
  const pct = Math.round(value * 100)
  return (
    <div className="flex items-center gap-3">
      <div className="h-2 w-44 rounded-full bg-surface-elevated overflow-hidden border border-border">
        <motion.div
          className="h-full rounded-full bg-gradient-to-r from-accent to-fuchsia-500"
          initial={{ width: 0 }}
          animate={{ width: `${pct}%` }}
          transition={{ type: 'spring', stiffness: 180, damping: 24 }}
        />
      </div>
      <span className="tabular-nums text-xs text-muted w-10 text-right">{pct}%</span>
    </div>
  )
}
